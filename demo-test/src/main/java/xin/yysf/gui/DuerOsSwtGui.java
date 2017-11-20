package xin.yysf.gui;

import com.baidu.duer.dcs.framework.message.DcsStreamRequestBody;
import com.baidu.duer.dcs.systeminterface.IAudioInput;
import com.baidu.duer.dcs.systeminterface.IWebView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import xin.yysf.duer.DuerSdkApp;

public class DuerOsSwtGui implements IWebView, IAudioInput.IAudioInputListener, SelectionListener {

    protected Shell shlDuerosgui;
    private Text text;
    private Browser browser;
    private Button stopBtn;
    private Button sentTxtBtn;
    private Button startBtn;




    private DuerSdkApp sdkApp;





    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            DuerOsSwtGui window = new DuerOsSwtGui();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shlDuerosgui.open();
        shlDuerosgui.layout();
        initDuerOs();
        while (!shlDuerosgui.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        sdkApp.shutdown();
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shlDuerosgui = new Shell();
        shlDuerosgui.setSize(726, 663);
        shlDuerosgui.setText("DuerOsGui");
        shlDuerosgui.setLayout(new FormLayout());

        startBtn = new Button(shlDuerosgui, SWT.NONE);

        FormData fd_startBtn = new FormData();
        fd_startBtn.top = new FormAttachment(0, 10);
        fd_startBtn.left = new FormAttachment(0, 10);
        startBtn.setLayoutData(fd_startBtn);
        startBtn.setText("\u5F55\u97F3");

        stopBtn = new Button(shlDuerosgui, SWT.NONE);

        FormData fd_stopBtn = new FormData();
        fd_stopBtn.top = new FormAttachment(startBtn, 0, SWT.TOP);
        fd_stopBtn.left = new FormAttachment(startBtn, 6);
        stopBtn.setLayoutData(fd_stopBtn);
        stopBtn.setText("\u505C\u6B62");
        stopBtn.setEnabled(false);
        browser = new Browser(shlDuerosgui, SWT.NONE);
        FormData fd_browser = new FormData();
        fd_browser.bottom = new FormAttachment(100, -10);
        fd_browser.left = new FormAttachment(0, 10);
        fd_browser.right = new FormAttachment(100, -10);
        browser.setLayoutData(fd_browser);

        text = new Text(shlDuerosgui, SWT.BORDER);
        FormData fd_text = new FormData();
        fd_text.bottom = new FormAttachment(browser, -6);
        fd_text.top = new FormAttachment(startBtn, 6);
        fd_text.left = new FormAttachment(0, 10);
        text.setLayoutData(fd_text);

        sentTxtBtn = new Button(shlDuerosgui, SWT.NONE);
        fd_text.right = new FormAttachment(sentTxtBtn, -6);
        fd_browser.top = new FormAttachment(sentTxtBtn, 4);
        FormData fd_sentTxtBtn = new FormData();
        fd_sentTxtBtn.top = new FormAttachment(0, 41);
        fd_sentTxtBtn.right = new FormAttachment(100, -10);
        sentTxtBtn.setLayoutData(fd_sentTxtBtn);
        sentTxtBtn.setText("\u53D1\u9001\u6587\u672C");


        startBtn.addSelectionListener(this);
        stopBtn.addSelectionListener(this);
        sentTxtBtn.addSelectionListener(this);
    }

    @Override
    public void onStartRecord(DcsStreamRequestBody dcsStreamRequestBody) {
        Display.getDefault().asyncExec(()->{
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            sentTxtBtn.setEnabled(false);
        });
    }

    @Override
    public void onStopRecord() {
        Display.getDefault().asyncExec(()->{
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            sentTxtBtn.setEnabled(true);
        });
    }

    @Override
    public void loadUrl(String url) {
        Display.getDefault().asyncExec(()->{
            browser.setUrl(url);
        });
    }

    @Override
    public void loadContent(String s) {
        Display.getDefault().asyncExec(()->{
            browser.setText(s);
        });
    }

    @Override
    public void linkClicked(String url) {

    }

    @Override
    public void addWebViewListener(IWebViewListener listener) {

    }

    @Override
    public void setText(String msg) {
        Display.getDefault().asyncExec(()->{
            text.setText(msg);
        });
    }

    @Override
    public void widgetSelected(SelectionEvent selectionEvent) {
        if(selectionEvent.getSource()==startBtn){
            sdkApp.factory.getVoiceInput().startRecord();
        }else if(selectionEvent.getSource()==stopBtn){
            sdkApp.factory.getVoiceInput().stopRecord();
        }else if(selectionEvent.getSource()==sentTxtBtn){
            MessageBox msg=new MessageBox(Display.getCurrent().getActiveShell());
            msg.setText("提示");
            msg.setMessage("未实现");
            msg.open();
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent selectionEvent) {

    }

    private void initDuerOs() {
        sdkApp=DuerSdkApp.createDuerOs();
        sdkApp.setWebView(this);
        sdkApp.factory.getVoiceInput().registerAudioInputListener(this);

        loadContent("<h1>HelloDuerOS</h1>");
        if(sdkApp.token==null){
            loadContent("Oauth2添加回调<b>http://127.0.0.1:8080/auth</b>然后访问<p>"+sdkApp.url+"</p>");
        }
    }
}
