package com.tencent.qcloud.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.qcloud.presentation.viewfeatures.ChatView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面输入控件
 */
public class ChatInput extends RelativeLayout implements TextWatcher, View.OnClickListener {

    private static final String TAG = "ChatInput";

    private ImageButton btnAdd, btnSend, btnVoice, btnKeyboard, btnEmotion;
    private EditText editText;
    private boolean isSendVisible, isHoldVoiceBtn, isEmoticonReady;
    private InputMode inputMode = InputMode.NONE;
    private ChatView chatView;
    private LinearLayout morePanel, textPanel;
    private TextView voicePanel;
    private LinearLayout emoticonPanel;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private List<GridView> gridList = new ArrayList<>();
    View view;
    ViewPager viewPager;
    LinearLayout ll_dot;

    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.chat_input, this);
        initView();
    }

    private void initView() {
        textPanel = (LinearLayout) findViewById(R.id.text_panel);
        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnSend = (ImageButton) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnVoice = (ImageButton) findViewById(R.id.btn_voice);
        btnVoice.setOnClickListener(this);
        btnEmotion = (ImageButton) findViewById(R.id.btnEmoticon);
        btnEmotion.setOnClickListener(this);
        morePanel = (LinearLayout) findViewById(R.id.morePanel);
        LinearLayout BtnImage = (LinearLayout) findViewById(R.id.btn_photo);
        BtnImage.setOnClickListener(this);
        LinearLayout BtnPhoto = (LinearLayout) findViewById(R.id.btn_image);
        BtnPhoto.setOnClickListener(this);
        LinearLayout btnVideo = (LinearLayout) findViewById(R.id.btn_video);
        btnVideo.setOnClickListener(this);
        LinearLayout btnFile = (LinearLayout) findViewById(R.id.btn_file);
        btnFile.setOnClickListener(this);
        setSendBtn();
        btnKeyboard = (ImageButton) findViewById(R.id.btn_keyboard);
        btnKeyboard.setOnClickListener(this);
        voicePanel = (TextView) findViewById(R.id.voice_panel);
        voicePanel.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isHoldVoiceBtn = true;
                        updateVoiceView();
                        break;
                    case MotionEvent.ACTION_UP:
                        isHoldVoiceBtn = false;
                        updateVoiceView();
                        break;
                }
                return true;
            }
        });
        editText = (EditText) findViewById(R.id.input);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    updateView(InputMode.TEXT);
                }
            }
        });
        isSendVisible = editText.getText().length() != 0;
        emoticonPanel = (LinearLayout) findViewById(R.id.emoticonPanel);
        view = LayoutInflater.from(getContext()).inflate(R.layout.view_emoji, null);
        viewPager = (ViewPager) view.findViewById(R.id.pager_emoji);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
    }

    private void updateView(InputMode mode) {
        if (mode == inputMode) return;
        leavingCurrentState();
        switch (inputMode = mode) {
            case MORE:
                morePanel.setVisibility(VISIBLE);
                break;
            case TEXT:
                if (editText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
                break;
            case VOICE:
                voicePanel.setVisibility(VISIBLE);
                textPanel.setVisibility(GONE);
                btnVoice.setVisibility(GONE);
                btnKeyboard.setVisibility(VISIBLE);
                break;
            case EMOTICON:
                if (!isEmoticonReady) {
                    prepareEmoticon();
                }
                emoticonPanel.setVisibility(VISIBLE);
                break;
        }
    }

    private void leavingCurrentState() {
        switch (inputMode) {
            case TEXT:
                View view = ((Activity) getContext()).getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editText.clearFocus();
                break;
            case MORE:
                morePanel.setVisibility(GONE);
                break;
            case VOICE:
                voicePanel.setVisibility(GONE);
                textPanel.setVisibility(VISIBLE);
                btnVoice.setVisibility(VISIBLE);
                btnKeyboard.setVisibility(GONE);
                break;
            case EMOTICON:
                emoticonPanel.setVisibility(GONE);
        }
    }


    private void updateVoiceView() {
        if (isHoldVoiceBtn) {
            voicePanel.setText(getResources().getString(R.string.chat_release_send));
            voicePanel.setBackground(getResources().getDrawable(R.drawable.btn_voice_pressed));
            chatView.startSendVoice();
        } else {
            voicePanel.setText(getResources().getString(R.string.chat_press_talk));
            voicePanel.setBackground(getResources().getDrawable(R.drawable.btn_voice_normal));
            chatView.endSendVoice();
        }
    }


    /**
     * 关联聊天界面逻辑
     */
    public void setChatView(ChatView chatView) {
        this.chatView = chatView;
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isSendVisible = s != null && s.length() > 0;
        setSendBtn();
        if (isSendVisible) {
            chatView.sending();
        }
    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setSendBtn() {
        if (isSendVisible) {
            btnAdd.setVisibility(GONE);
            btnSend.setVisibility(VISIBLE);
        } else {
            btnAdd.setVisibility(VISIBLE);
            btnSend.setVisibility(GONE);
        }
    }

    private void prepareEmoticon() {
        if (emoticonPanel == null) return;
        gridList.clear();
        final int num = 84;
        for (int j = 0; j < num; ) {
            final List<ImageView> imageViews = new ArrayList<>();
            GridView gridView = new GridView(getContext());
            gridView.setNumColumns(7);
            for (int i = j; i < ((j + 28) < num ? (j + 28) : num); i++) {
                try {
                    AssetManager am = getContext().getAssets();
                    final int index = i;
                    InputStream is;
                    if ((index + 1) % 28 == 0 || index == (num - 1)) {
                        is = am.open(String.format("emoticon/%d.png", 83));
                    } else {
                        is = am.open(String.format("emoticon/%d.png", index));
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Matrix matrix = new Matrix();
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    matrix.postScale(1.5f, 1.5f);
                    final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            width, height, matrix, true);
                    ImageView image = new ImageView(getContext());
                    image.setImageBitmap(resizedBitmap);
                    image.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), 40), 1f));
                    image.setScaleType(ImageView.ScaleType.CENTER);
                    image.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((index + 1) % 28 == 0 || index == (num - 1)) {
                                int keyCode = KeyEvent.KEYCODE_DEL;
                                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                                editText.dispatchKeyEvent(keyEvent);
                            } else {
                                String content = String.valueOf(index);
                                SpannableString str = new SpannableString(String.valueOf(index));
                                ImageSpan span = new ImageSpan(getContext(), resizedBitmap, ImageSpan.ALIGN_BASELINE);
                                str.setSpan(span, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.append(str);
                            }
                        }
                    });
                    imageViews.add(image);
                    is.close();
                } catch (IOException e) {

                }
            }
            gridView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return imageViews.size();
                }

                @Override
                public Object getItem(int position) {
                    return imageViews.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ImageView imageView = imageViews.get(position);
                    return imageView;
                }
            });
            gridList.add(gridView);
            j = j + 28;
        }
        viewPager.setAdapter(new EmojiAdapter(gridList));
        gotoInitData(gridList);
        emoticonPanel.addView(view);
        isEmoticonReady = true;
//        for (int i = 0; i < 5; ++i){
//            LinearLayout linearLayout = new LinearLayout(getContext());
//            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
//            for (int j = 0;j < 7; ++j){
//
//                try{
//                    AssetManager am = getContext().getAssets();
//                    final int index = 7*i+j;
//                    InputStream is = am.open(String.format("emoticon/%d.gif", index));
//                    Bitmap bitmap = BitmapFactory.decodeStream(is);
//                    Matrix matrix = new Matrix();
//                    int width = bitmap.getWidth();
//                    int height = bitmap.getHeight();
//                    matrix.postScale(3.5f, 3.5f);
//                    final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//                            width, height, matrix, true);
//                    ImageView image = new ImageView(getContext());
//                    image.setImageBitmap(resizedBitmap);
//                    image.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
//                    linearLayout.addView(image);
//                    image.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            String content = String.valueOf(index);
//                            SpannableString str = new SpannableString(String.valueOf(index));
//                            ImageSpan span = new ImageSpan(getContext(), resizedBitmap, ImageSpan.ALIGN_BASELINE);
//                            str.setSpan(span, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            editText.append(str);
//                        }
//                    });
//                    is.close();
//                }catch (IOException e){
//
//                }
//
//            }
//            emoticonPanel.addView(linearLayout);
//        }
//        isEmoticonReady = true;
    }

    /**
     * 初始表情布局下底部圆点
     *
     * @param list
     */
    private void gotoInitData(List<GridView> list) {
        ll_dot.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            if (i == 0) {
                imageView.setImageResource(R.drawable.shape_dot_select);

            } else {
                imageView.setImageResource(R.drawable.shape_dot_nomal);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(getContext(), 8),
                    dip2px(getContext(), 8));
            layoutParams.setMargins(20, 0, 0, 0);
            ll_dot.addView(imageView, layoutParams);
        }
        if (ll_dot.getChildCount() <= 1) {
            ll_dot.setVisibility(View.GONE);
        } else {
            ll_dot.setVisibility(VISIBLE);
        }
        viewPager.setOffscreenPageLimit(6);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ll_dot.getChildCount(); i++) {
                    if (i != position) {
                        ((ImageView) ll_dot.getChildAt(i)).setImageResource(R.drawable.shape_dot_nomal);
                    }
                }
                ((ImageView) ll_dot.getChildAt(position)).setImageResource(R.drawable.shape_dot_select);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 表情适配器
     */
    public class EmojiAdapter extends PagerAdapter {
        private List<GridView> list;

        public EmojiAdapter(List<GridView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((GridView) object);
        }

        @Override
        public GridView instantiateItem(ViewGroup container, int position) {
            GridView GridView = list.get(position);
            container.addView(GridView);
            return GridView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Activity activity = (Activity) getContext();
        int id = v.getId();
        if (id == R.id.btn_send) {
            chatView.sendText();
        }
        if (id == R.id.btn_add) {
            updateView(inputMode == InputMode.MORE ? InputMode.TEXT : InputMode.MORE);
        }
        if (id == R.id.btn_photo) {
            if (activity != null && requestCamera(activity)) {
                chatView.sendPhoto();
            }
        }
        if (id == R.id.btn_image) {
            if (activity != null && requestStorage(activity)) {
                chatView.sendImage();
            }
        }
        if (id == R.id.btn_voice) {
            if (activity != null && requestAudio(activity)) {
                updateView(InputMode.VOICE);
            }
        }
        if (id == R.id.btn_keyboard) {
            updateView(InputMode.TEXT);
        }
        if (id == R.id.btn_video) {
            if (getContext() instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) getContext();
                if (requestVideo(fragmentActivity)) {
                    VideoInputDialog.show(fragmentActivity.getSupportFragmentManager());
                }
            }
        }
        if (id == R.id.btnEmoticon) {
            updateView(inputMode == InputMode.EMOTICON ? InputMode.TEXT : InputMode.EMOTICON);
        }
        if (id == R.id.btn_file) {
            chatView.sendFile();
        }
    }


    /**
     * 获取输入框文字
     */
    public Editable getText() {
        return editText.getText();
    }

    /**
     * 设置输入框文字
     */
    public void setText(String text) {
        editText.setText(text);
    }


    /**
     * 设置输入模式
     */
    public void setInputMode(InputMode mode) {
        updateView(mode);
    }


    public enum InputMode {
        TEXT,
        VOICE,
        EMOTICON,
        MORE,
        VIDEO,
        NONE,
    }

    private boolean requestVideo(Activity activity) {
        if (afterM()) {
            final List<String> permissionsList = new ArrayList<>();
            if ((activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if (permissionsList.size() != 0) {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestCamera(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestAudio(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestStorage(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean afterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
