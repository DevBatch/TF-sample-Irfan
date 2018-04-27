package com.taskforce.app.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.onesignal.OneSignal;
import com.taskforce.app.Application;
import com.taskforce.app.R;
import com.taskforce.app.communication.event.BusProvider;
import com.taskforce.app.communication.event.Event;
import com.taskforce.app.communication.event.EventRunnable;
import com.taskforce.app.communication.event.MessageReceiveEvent;
import com.taskforce.app.communication.event.MessageReplaceEvent;
import com.taskforce.app.communication.event.RefreshInboxEvent;
import com.taskforce.app.communication.network.Api;
import com.taskforce.app.communication.network.CallbacksManager;
import com.taskforce.app.communication.network.rersponse.Task;
import com.taskforce.app.communication.network.rersponse.TaskResponse;
import com.taskforce.app.communication.network.rersponse.User;
import com.taskforce.app.fragment.BaseFragment;
import com.taskforce.app.messaging.listeners.LoadMoreMessagesListener;
import com.taskforce.app.messaging.listeners.UserClicksAvatarPictureListener;
import com.taskforce.app.messaging.listeners.UserSendsMessageListener;
import com.taskforce.app.messaging.message.Message;
import com.taskforce.app.messaging.message.MessageConversationResponse;
import com.taskforce.app.messaging.message.MessageSource;
import com.taskforce.app.messaging.message.SpinnerMessage;
import com.taskforce.app.messaging.message.TextMessage;
import com.taskforce.app.messaging.message.messageItem.MessageItem;
import com.taskforce.app.messaging.message.messageItem.MessageRecyclerAdapter;
import com.taskforce.app.messaging.utils.CustomSettings;
import com.taskforce.app.messaging.utils.MessageJob;
import com.taskforce.app.messaging.utils.Refresher;
import com.taskforce.app.messaging.utils.ScrollUtils;
import com.taskforce.app.messaging.utils.asyncTasks.AddNewMessageTask;
import com.taskforce.app.messaging.utils.asyncTasks.ReplaceMessagesTask;
import com.taskforce.app.utils.AccountUtils;
import com.taskforce.app.utils.CommonKeys;
import com.taskforce.app.utils.DateUtils;
import com.taskforce.app.utils.IntentBuilder;
import com.taskforce.app.utils.InternetUtils;
import com.taskforce.app.utils.LogUtils;
import com.taskforce.app.utils.StringUtils;
import com.taskforce.app.widgets.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.taskforce.app.messaging.message.MessageSource.EXTERNAL_USER;
import static com.taskforce.app.messaging.message.MessageSource.LOCAL_USER;
import static com.taskforce.app.notifications.Actions.ACTION_MESSAGE;
import static com.taskforce.app.utils.DateUtils.FORMAT_LONG_INPUT;
import static com.taskforce.app.utils.DateUtils.TIME_ZONE_Default;
import static com.taskforce.app.utils.DateUtils.getCurrDate;
import static com.taskforce.app.utils.DateUtils.getDeadLineDate;

/**
 * Created by John C. Hunchar on 1/12/16.
 */
public class SlyceMessagingFragment extends BaseFragment implements OnClickListener, UserClicksAvatarPictureListener {

    private static final String TAG = SlyceMessagingFragment.class.getSimpleName();
    private static final int START_RELOADING_DATA_AT_SCROLL_VALUE = 5000;

    private EditText mEntryField;
    private List<Message> mMessages;
    private List<MessageItem> mMessageItems;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private LoadMoreMessagesListener loadMoreMessagesListener;
    private UserSendsMessageListener listener;
    private CustomSettings customSettings;
    private Refresher mRefresher;
    private User defaultUser;
    private long toUserId;
    private long taskId;
    private int startHereWhenUpdate;
    private long recentUpdatedTime;
    private boolean moreMessagesExist;
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mBroadcastReceiver;

    /**
     * Called by the system every time a {@link android.support.v4.app.Fragment} load in to {@link android.app.Activity}
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultUser(AccountUtils.getUser());
        Bundle bundle = getArguments();
        toUserId = bundle.getLong(CommonKeys.USER);
        taskId = bundle.getLong(CommonKeys.ID);
        int androidNotificationId = bundle.getInt(CommonKeys.ANDROID_NOTIFICATION_ID, 0);
        OneSignal.cancelNotification(androidNotificationId);
        addEventRunnable(MessageReceiveEvent.class, new EventRunnable() {
            @Override
            public void run(Event event) {
                if (mRecyclerView != null && mRecyclerAdapter != null) {
                    ScrollUtils.scrollToBottomAfterDelay(mRecyclerView, mRecyclerAdapter);
                    MessageReceiveEvent ev = (MessageReceiveEvent) event;
                    addNewMessage(ev.message);
                    if (listener != null)
                        listener.onUserSendsTextMessage(ev.message.message);
                }
//                mAdapter.addItem(ev.message);
//
//                mListView.smoothScrollToPosition(mAdapter.getCount() - 1);
            }
        });
        addEventRunnable(MessageReplaceEvent.class, new EventRunnable() {
            @Override
            public void run(Event event) {
                MessageReplaceEvent ev = (MessageReplaceEvent) event;
//                mAdapter.replaceItem(ev.newMessage, ev.oldMessage);
            }
        });
        BusProvider.getInstance().register(this);
    }

    //used by Event
    @SuppressWarnings("unused")
    public void onEventMainThread(MessageReceiveEvent event) {
        handleEvent(event);
    }

    //used by Event
    @SuppressWarnings("unused")
    public void onEventMainThread(MessageReplaceEvent event) {
        handleEvent(event);
    }

    private void addSpinner() {
        mMessages.add(0, new SpinnerMessage());
        replaceMessages(mMessages, -1);
    }

    private void removeSpinner() {
        if (mMessages.get(0) instanceof SpinnerMessage) {
            mMessages.remove(0);
            mMessageItems.remove(0);
            mRecyclerAdapter.notifyItemRemoved(0);
        }
    }

    public void setMoreMessagesExist(boolean moreMessagesExist) {
        if (this.moreMessagesExist == moreMessagesExist)
            return;
        this.moreMessagesExist = moreMessagesExist;
        if (moreMessagesExist)
            addSpinner();
        else
            removeSpinner();
        loadMoreMessagesIfNecessary();
    }

    public void setLoadMoreMessagesListener(LoadMoreMessagesListener loadMoreMessagesListener) {
        this.loadMoreMessagesListener = loadMoreMessagesListener;
        loadMoreMessagesIfNecessary();
    }

    public void setUserClicksAvatarPictureListener(UserClicksAvatarPictureListener userClicksAvatarPictureListener) {
        this.customSettings.userClicksAvatarPictureListener = userClicksAvatarPictureListener;
    }

    @Override
    public void userClicksAvatarPhoto(long userId) {
        if (userId != AccountUtils.getUserId() && !getArguments().getBoolean(CommonKeys.ROLE, false)) {
            startActivity(IntentBuilder.buildOtherUserProfileFragmentIntent(getBaseActivity(), userId));
        }
    }

    public void setDefaultUser(User user) {
        defaultUser = user;
    }

    public void setStyle(int style) {
        if(getView()==null)return;
        TypedArray ta = getActivity().obtainStyledAttributes(style, R.styleable.SlyceMessagingTheme);
        try {
            this.customSettings.backgroudColor = ta.getColor(R.styleable.SlyceMessagingTheme_backgroundColor, Color.GRAY);
            getView().setBackgroundColor(this.customSettings.backgroudColor); // the background color
            this.customSettings.timestampColor = ta.getColor(R.styleable.SlyceMessagingTheme_timestampTextColor, Color.BLACK);
            this.customSettings.externalBubbleTextColor = ta.getColor(R.styleable.SlyceMessagingTheme_externalBubbleTextColor, Color.WHITE);
            this.customSettings.externalBubbleBackgroundColor = ta.getColor(R.styleable.SlyceMessagingTheme_externalBubbleBackground, Color.WHITE);
            this.customSettings.localBubbleBackgroundColor = ta.getColor(R.styleable.SlyceMessagingTheme_localBubbleBackground, Color.WHITE);
            this.customSettings.localBubbleTextColor = ta.getColor(R.styleable.SlyceMessagingTheme_localBubbleTextColor, Color.WHITE);
            this.customSettings.snackbarBackground = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarBackground, Color.WHITE);
            this.customSettings.snackbarButtonColor = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarButtonColor, Color.WHITE);
            this.customSettings.snackbarTitleColor = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarTitleColor, Color.WHITE);
        } finally {
            ta.recycle();
        }
    }

    public void addNewMessages(List<Message> messages) {
        mMessages.addAll(messages);
        new AddNewMessageTask(messages, mMessageItems, mRecyclerAdapter, mRecyclerView, getActivity().getApplicationContext(), customSettings).execute();
    }

    public void addNewMessage(Message message) {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        addNewMessages(messages);
    }

    public void setOnSendMessageListener(UserSendsMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        getTaskDetails(rootView, taskId);
        setUpBroadcastReceiver();
        this.customSettings = new CustomSettings();
        getBaseActivity().setToolbarTitle(getArguments().getString(CommonKeys.USER_NAME), this);
        // Setup views
        mEntryField = rootView.findViewById(R.id.slyce_messaging_edit_text_entry_field);
        ImageView mSendButton = rootView.findViewById(R.id.slyce_messaging_image_view_send);
        ImageView mSnapButton = rootView.findViewById(R.id.slyce_messaging_image_view_snap);
        mRecyclerView = rootView.findViewById(R.id.slyce_messaging_recycler_view);

        // Add interfaces
        mSendButton.setOnClickListener(this);
        mSnapButton.setOnClickListener(this);

        // Init variables for recycler view
        mMessages = new ArrayList<>();
        mMessageItems = new ArrayList<>();
        mRecyclerAdapter = new MessageRecyclerAdapter(mMessageItems, customSettings);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return !mRefresher.isRefreshing();
            }

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                    LogUtils.LOGE(TAG, e.getMessage(), e);
                }
            }
        };
        mLinearLayoutManager.setStackFromEnd(true);
        // Setup recycler view
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mRefresher.isRefreshing();
                    }
                }
        );
        startUpdateTimestampsThread();
        startHereWhenUpdate = 0;
        recentUpdatedTime = 0;
        mRefresher = new Refresher(false);
        setStyle(R.style.MessagesTheme);
        setMoreMessagesExist(true);
        loadMoreMessagesIfNecessary();
        startLoadMoreMessagesListener();
        setLoadMoreMessagesListener(new LoadMoreMessagesListener() {
            @Override
            public List<Message> loadMoreMessages() {
                return null;
            }
        });
        setUserClicksAvatarPictureListener(this);
    }

    private void startUpdateTimestampsThread() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (int i = startHereWhenUpdate; i < mMessages.size() && i < mMessageItems.size(); i++) {
                    try {
                        MessageItem messageItem = mMessageItems.get(i);
                        Message message = messageItem.getMessage();
                        if (DateUtils.dateNeedsUpdated(getActivity(), message.getDate(), messageItem.getDate())) {
                            messageItem.updateDate(getActivity(), message.getDate());
                            updateTimestampAtValue(i);
                        } else if (i == startHereWhenUpdate) {
                            i++;
                        }
                    } catch (RuntimeException exception) {

                    }
                }
            }
        }, 0, 62, TimeUnit.MINUTES);
    }

    private void startLoadMoreMessagesListener() {
        if (Build.VERSION.SDK_INT >= 23)
            mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    loadMoreMessagesIfNecessary();
                }
            });
        else
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    loadMoreMessagesIfNecessary();
                }
            });
    }

    private void loadMoreMessagesIfNecessary() {
        if (shouldReloadData()) {
            recentUpdatedTime = new Date().getTime();
            loadMoreMessages();
        }
    }

    private void loadMoreMessages() {
        new AsyncTask<Void, Void, Void>() {
            private boolean spinnerExists;
            private List<TextMessage> messages;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mRefresher.setIsRefreshing(true);
                spinnerExists = moreMessagesExist && mMessages.get(0) instanceof SpinnerMessage;
                if (spinnerExists) {
                    mMessages.remove(0);
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                MessageConversationResponse response = Api.SERVICE.getMessagesConversation(toUserId, taskId);
                messages = response.messages;//Api.SERVICE.getMessagesConversation()loadMoreMessagesListener.loadMoreMessages();
                moreMessagesExist = false;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                int upTo = messages.size();
                for (int i = messages.size() - 1; i >= 0; i--) {
                    Message message = messages.get(i);
                    message.setSource(message.fromUser == defaultUser.id ? LOCAL_USER : EXTERNAL_USER);
                    message.setUser(message.fromUser == defaultUser.id ? defaultUser : message.user);
                    mMessages.add(0, message);
                }
                if (spinnerExists && moreMessagesExist)
                    mMessages.add(0, new SpinnerMessage());
                mRefresher.setIsRefreshing(false);
                replaceMessages(mMessages, upTo);
            }
        }.execute();
    }

    public void replaceMessages(List<Message> messages) {
        replaceMessages(messages, -1);
    }

    private void replaceMessages(List<Message> messages, int upTo) {
        if (getActivity() != null) {
            new ReplaceMessagesTask(messages, mMessageItems, mRecyclerAdapter, mRecyclerView, getActivity().getApplicationContext(), mRefresher, upTo).execute();
        }
    }

    private boolean shouldReloadData() {
        int scrollOffset = mRecyclerView.computeVerticalScrollOffset();
        if (loadMoreMessagesListener == null || !moreMessagesExist) {
            return false;
        } else {
            return scrollOffset < START_RELOADING_DATA_AT_SCROLL_VALUE &&
                    recentUpdatedTime + 1000 < new Date().getTime();
        }
    }

    private void updateTimestampAtValue(final int i) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecyclerAdapter.notifyItemChanged(i);
                    mRecyclerView.smoothScrollToPosition(mRecyclerAdapter.getItemCount() - 1);
//
                }
            });
        }
    }

    private void smoothScrollToPosition(final int i) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.smoothScrollToPosition(mRecyclerAdapter.getItemCount() - 1);
//
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (!InternetUtils.isConnected(getBaseActivity())) {
            Application.toast(R.string.no_network_connection, Gravity.CENTER);
            return;
        }
        switch (v.getId()) {
            case R.id.toolbar_title:
                if (!getArguments().getBoolean(CommonKeys.ROLE, false))
                    startActivity(IntentBuilder.buildOtherUserProfileFragmentIntent(getBaseActivity(), toUserId));
                break;
            case R.id.slyce_messaging_image_view_send:
                sendUserTextMessage();
                break;
        }
    }

    private void sendUserTextMessage() {
        String text = StringUtils.getString(mEntryField);
        if (TextUtils.isEmpty(text))
            return;
        mEntryField.setText("");
        // Build messageData object
        TextMessage message = new TextMessage();
        message.setDate(getCurrDate(FORMAT_LONG_INPUT, TIME_ZONE_Default));
        message.setSource(LOCAL_USER);
        message.setText(text);
        message.setUser(defaultUser);
        message.taskId = taskId;
        message.toUser = toUserId;
        message.receiver = toUserId;
        MessageJob job = new MessageJob(message);
        Application.get().getJobManager().addJobInBackground(job);

    }

    @Override
    public int toolbarHomeColor() {
        return 0;
    }

    @Override
    public String getToolBarTile() {
        return null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_slyce_messaging;
    }

    @Override
    public int toolbarColor() {
        return 0;
    }

    @Override
    public int getScreenName() {
        return 0;
    }

    private void setUpBroadcastReceiver() {
        mIntentFilter = new IntentFilter(ACTION_MESSAGE);
        mIntentFilter.setPriority(100);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Bundle bundle = intent.getBundleExtra(CommonKeys.BUNDLE_EXTRAS);
                    Message message = bundle.getParcelable(CommonKeys.MESSAGE);
                    if (message != null && message.fromUser == toUserId && message.taskId == taskId) {
                        message.setSource(MessageSource.EXTERNAL_USER);
                        addNewMessage(message);
                    }
                    bundle.putBoolean(CommonKeys.ABORT, true);
                    Bundle results = getResultExtras(true);
                    results.putBoolean(CommonKeys.ABORT, true);
                    setResultExtras(results);
                    abortBroadcast();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void getTaskDetails(final View view, long taskId) {

        CallbacksManager.CancelableCallback<TaskResponse> callback = callbacksManager.new CancelableCallback<TaskResponse>() {
            @Override
            protected void onFailure(RetrofitError error) {

            }

            @Override
            protected void onSuccess(TaskResponse taskResponse, Response response) {
                if (taskResponse.responseHeader.responseCode == 1) {
                    final Task mTask = taskResponse.task;
                    view.findViewById(R.id.rootLayout).setVisibility(View.VISIBLE);
                    TextView viewDetails;
                    TextView mTitle = view.findViewById(R.id.title);
                    viewDetails = view.findViewById(R.id.viewDetails);
                    TextView fee = view.findViewById(R.id.fee);
                    TextView deadLine = view.findViewById(R.id.deadline);
                    mTitle.setText(mTask.title);
                    viewDetails.setTag(mTask);
                    fee.setText(Application.string(R.string.fee_labl, mTask.achieverWillGet));
                    deadLine.setText(getDeadLineDate(mTask.endsAt));
                } else {
                    view.findViewById(R.id.rootLayout).setVisibility(View.GONE);
                }
            }
        };
        apiFor(callback).getTaskDetails(taskId, callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().post(new RefreshInboxEvent(null));
        BusProvider.getInstance().unregister(this);
        try {
            mRecyclerAdapter = null;
            mRecyclerView = null;
            getActivity().unregisterReceiver(mBroadcastReceiver);
            BusProvider.getInstance().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
