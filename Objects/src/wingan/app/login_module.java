package wingan.app;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class login_module extends Activity implements B4AActivity{
	public static login_module mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.login_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (login_module).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.login_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.login_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (login_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (login_module) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return login_module.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (login_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (login_module) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            login_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (login_module) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor2 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor3 = null;
public static anywheresoftware.b4a.phone.Phone.PhoneId _phone_id = null;
public static anywheresoftware.b4a.sql.SQL _connection = null;
public static String _tab_id = "";
public static String _username = "";
public anywheresoftware.b4a.objects.IME _ctrl = null;
public carouselviewwrapper.carouselViewWrapper _cv1 = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bm = null;
public anywheresoftware.b4a.objects.collections.List _blist = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_pass = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinner_user = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_lastlog = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.dashboard_module _dashboard_module = null;
public wingan.app.return_module _return_module = null;
public wingan.app.cancelled_module _cancelled_module = null;
public wingan.app.sales_return_module _sales_return_module = null;
public wingan.app.preparing_module _preparing_module = null;
public wingan.app.loading_module _loading_module = null;
public wingan.app.daily_count _daily_count = null;
public wingan.app.inventory_table _inventory_table = null;
public wingan.app.montlhy_inventory2_module _montlhy_inventory2_module = null;
public wingan.app.price_checking_module _price_checking_module = null;
public wingan.app.expiration_module _expiration_module = null;
public wingan.app.daily_inventory_module _daily_inventory_module = null;
public wingan.app.daily_template _daily_template = null;
public wingan.app.database_module _database_module = null;
public wingan.app.monthly_inventory_module _monthly_inventory_module = null;
public wingan.app.monthlyinv_module _monthlyinv_module = null;
public wingan.app.number _number = null;
public wingan.app.receiving_module _receiving_module = null;
public wingan.app.receiving2_module _receiving2_module = null;
public wingan.app.scale_calc _scale_calc = null;
public wingan.app.starter _starter = null;
public wingan.app.httputils2service _httputils2service = null;
public wingan.app.b4xcollections _b4xcollections = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static void  _activity_create(boolean _firsttime) throws Exception{
ResumableSub_Activity_Create rsub = new ResumableSub_Activity_Create(null,_firsttime);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Create extends BA.ResumableSub {
public ResumableSub_Activity_Create(wingan.app.login_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.login_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 48;BA.debugLine="Activity.LoadLayout(\"login_design\")";
parent.mostCurrent._activity.LoadLayout("login_design",mostCurrent.activityBA);
 //BA.debugLineNum = 50;BA.debugLine="If connection.IsInitialized = False Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._connection.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 51;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 54;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 55;BA.debugLine="LOAD_USER";
_load_user();
 //BA.debugLineNum = 57;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 58;BA.debugLine="bg.Initialize(Colors.Transparent, 0)";
_bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Transparent,(int) (0));
 //BA.debugLineNum = 59;BA.debugLine="EDITTEXT_PASS.Background = bg";
parent.mostCurrent._edittext_pass.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 60;BA.debugLine="SPINNER_USER.Background = bg";
parent.mostCurrent._spinner_user.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 62;BA.debugLine="Carousel";
_carousel();
 //BA.debugLineNum = 64;BA.debugLine="EDITTEXT_PASS.Text = \"\"";
parent.mostCurrent._edittext_pass.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 66;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 67;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 225;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 226;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 227;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 78;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 79;BA.debugLine="SetAnimation(\"zoom_enter\", \"zoom_exit\")";
_setanimation("zoom_enter","zoom_exit");
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 71;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 72;BA.debugLine="EDITTEXT_PASS.Text = \"\"";
mostCurrent._edittext_pass.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 74;BA.debugLine="GET_LAST_LOG";
_get_last_log();
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static void  _button_login_click() throws Exception{
ResumableSub_BUTTON_LOGIN_Click rsub = new ResumableSub_BUTTON_LOGIN_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_LOGIN_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_LOGIN_Click(wingan.app.login_module parent) {
this.parent = parent;
}
wingan.app.login_module parent;
int _i = 0;
String _permission = "";
boolean _result = false;
int step6;
int limit6;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 134;BA.debugLine="If EDITTEXT_PASS.Text = \"\" Then";
if (true) break;

case 1:
//if
this.state = 64;
if ((parent.mostCurrent._edittext_pass.getText()).equals("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 64;
 //BA.debugLineNum = 135;BA.debugLine="ToastMessageShow(\"Password is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Password is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 137;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM us";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM users_table WHERE user = '"+parent.mostCurrent._spinner_user.getSelectedItem()+"' and pass = '"+parent.mostCurrent._edittext_pass.getText()+"'")));
 //BA.debugLineNum = 138;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 6:
//if
this.state = 63;
if (parent._cursor2.getRowCount()>0) { 
this.state = 8;
}else {
this.state = 58;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 139;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 9:
//for
this.state = 56;
step6 = 1;
limit6 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 65;
if (true) break;

case 65:
//C
this.state = 56;
if ((step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6)) this.state = 11;
if (true) break;

case 66:
//C
this.state = 65;
_i = ((int)(0 + _i + step6)) ;
if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 140;BA.debugLine="rp.CheckAndRequest(rp.PERMISSION_READ_PHONE_ST";
parent._rp.CheckAndRequest(processBA,parent._rp.PERMISSION_READ_PHONE_STATE);
 //BA.debugLineNum = 141;BA.debugLine="Wait For Activity_PermissionResult (Permission";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, this, null);
this.state = 67;
return;
case 67:
//C
this.state = 12;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 142;BA.debugLine="If Result Then";
if (true) break;

case 12:
//if
this.state = 55;
if (_result) { 
this.state = 14;
}else {
this.state = 54;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 143;BA.debugLine="ctrl.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 144;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 68;
return;
case 68:
//C
this.state = 15;
;
 //BA.debugLineNum = 145;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 146;BA.debugLine="If phone_id.GetDeviceId = \"352948097840379\" T";
if (true) break;

case 15:
//if
this.state = 52;
if ((parent._phone_id.GetDeviceId()).equals("352948097840379")) { 
this.state = 17;
}else if((parent._phone_id.GetDeviceId()).equals("863129042456148")) { 
this.state = 19;
}else if((parent._phone_id.GetDeviceId()).equals("863129042438930")) { 
this.state = 21;
}else if((parent._phone_id.GetDeviceId()).equals("863129042452949")) { 
this.state = 23;
}else if((parent._phone_id.GetDeviceId()).equals("863129042466881")) { 
this.state = 25;
}else if((parent._phone_id.GetDeviceId()).equals("359667094931063")) { 
this.state = 27;
}else if((parent._phone_id.GetDeviceId()).equals("863129042467103")) { 
this.state = 29;
}else if((parent._phone_id.GetDeviceId()).equals("359667094930842")) { 
this.state = 31;
}else if((parent._phone_id.GetDeviceId()).equals("863129042658685")) { 
this.state = 33;
}else if((parent._phone_id.GetDeviceId()).equals("359667094931014")) { 
this.state = 35;
}else if((parent._phone_id.GetDeviceId()).equals("863129042464068")) { 
this.state = 37;
}else if((parent._phone_id.GetDeviceId()).equals("863129042672041")) { 
this.state = 39;
}else if((parent._phone_id.GetDeviceId()).equals("863129042450661")) { 
this.state = 41;
}else if((parent._phone_id.GetDeviceId()).equals("356136101100354")) { 
this.state = 43;
}else if((parent._phone_id.GetDeviceId()).equals("356136101101360")) { 
this.state = 45;
}else if((parent._phone_id.GetDeviceId()).equals("356136101096925")) { 
this.state = 47;
}else if((parent._phone_id.GetDeviceId()).equals("863171044598378")) { 
this.state = 49;
}else {
this.state = 51;
}if (true) break;

case 17:
//C
this.state = 52;
 //BA.debugLineNum = 147;BA.debugLine="tab_id = \"DEBUGGER\"";
parent._tab_id = "DEBUGGER";
 //BA.debugLineNum = 148;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 19:
//C
this.state = 52;
 //BA.debugLineNum = 150;BA.debugLine="tab_id = \"A\"";
parent._tab_id = "A";
 //BA.debugLineNum = 151;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 21:
//C
this.state = 52;
 //BA.debugLineNum = 153;BA.debugLine="tab_id = \"B\"";
parent._tab_id = "B";
 //BA.debugLineNum = 154;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 23:
//C
this.state = 52;
 //BA.debugLineNum = 156;BA.debugLine="tab_id = \"C\"";
parent._tab_id = "C";
 //BA.debugLineNum = 157;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 25:
//C
this.state = 52;
 //BA.debugLineNum = 159;BA.debugLine="tab_id = \"D\"";
parent._tab_id = "D";
 //BA.debugLineNum = 160;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 27:
//C
this.state = 52;
 //BA.debugLineNum = 162;BA.debugLine="tab_id = \"S-D\"";
parent._tab_id = "S-D";
 //BA.debugLineNum = 163;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 29:
//C
this.state = 52;
 //BA.debugLineNum = 165;BA.debugLine="tab_id = \"E\"";
parent._tab_id = "E";
 //BA.debugLineNum = 166;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 31:
//C
this.state = 52;
 //BA.debugLineNum = 168;BA.debugLine="tab_id = \"S-E\"";
parent._tab_id = "S-E";
 //BA.debugLineNum = 169;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 33:
//C
this.state = 52;
 //BA.debugLineNum = 171;BA.debugLine="tab_id = \"F\"";
parent._tab_id = "F";
 //BA.debugLineNum = 172;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 35:
//C
this.state = 52;
 //BA.debugLineNum = 174;BA.debugLine="tab_id = \"S-F\"";
parent._tab_id = "S-F";
 //BA.debugLineNum = 175;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 37:
//C
this.state = 52;
 //BA.debugLineNum = 177;BA.debugLine="tab_id = \"G\"";
parent._tab_id = "G";
 //BA.debugLineNum = 178;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 39:
//C
this.state = 52;
 //BA.debugLineNum = 180;BA.debugLine="tab_id = \"H\"";
parent._tab_id = "H";
 //BA.debugLineNum = 181;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 41:
//C
this.state = 52;
 //BA.debugLineNum = 183;BA.debugLine="tab_id = \"I\"";
parent._tab_id = "I";
 //BA.debugLineNum = 184;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 43:
//C
this.state = 52;
 //BA.debugLineNum = 186;BA.debugLine="tab_id = \"S-M\"";
parent._tab_id = "S-M";
 //BA.debugLineNum = 187;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 45:
//C
this.state = 52;
 //BA.debugLineNum = 189;BA.debugLine="tab_id = \"S-N\"";
parent._tab_id = "S-N";
 //BA.debugLineNum = 190;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 47:
//C
this.state = 52;
 //BA.debugLineNum = 192;BA.debugLine="tab_id = \"S-O\"";
parent._tab_id = "S-O";
 //BA.debugLineNum = 193;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 49:
//C
this.state = 52;
 //BA.debugLineNum = 195;BA.debugLine="tab_id = \"VINCE\"";
parent._tab_id = "VINCE";
 //BA.debugLineNum = 196;BA.debugLine="LOGGER";
_logger();
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 198;BA.debugLine="Msgbox2Async(\"Your device is not registered,";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Your device is not registered, Please inform your IT Department"),BA.ObjectToCharSequence("Block"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 52:
//C
this.state = 55;
;
 if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 201;BA.debugLine="Msgbox2Async(\"Please accept phone permission";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please accept phone permission to proceed."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 55:
//C
this.state = 66;
;
 if (true) break;
if (true) break;

case 56:
//C
this.state = 63;
;
 //BA.debugLineNum = 204;BA.debugLine="username = SPINNER_USER.SelectedItem";
parent._username = parent.mostCurrent._spinner_user.getSelectedItem();
 if (true) break;

case 58:
//C
this.state = 59;
 //BA.debugLineNum = 206;BA.debugLine="If EDITTEXT_PASS.Text <> \"\" Then";
if (true) break;

case 59:
//if
this.state = 62;
if ((parent.mostCurrent._edittext_pass.getText()).equals("") == false) { 
this.state = 61;
}if (true) break;

case 61:
//C
this.state = 62;
 //BA.debugLineNum = 207;BA.debugLine="ToastMessageShow(\"Password doesn't match\", Fal";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Password doesn't match"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 208;BA.debugLine="EDITTEXT_PASS.Text = \"\"";
parent.mostCurrent._edittext_pass.setText(BA.ObjectToCharSequence(""));
 if (true) break;

case 62:
//C
this.state = 63;
;
 if (true) break;

case 63:
//C
this.state = 64;
;
 if (true) break;

case 64:
//C
this.state = -1;
;
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_permissionresult(String _permission,boolean _result) throws Exception{
}
public static void  _button_question_click() throws Exception{
ResumableSub_BUTTON_QUESTION_Click rsub = new ResumableSub_BUTTON_QUESTION_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_QUESTION_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_QUESTION_Click(wingan.app.login_module parent) {
this.parent = parent;
}
wingan.app.login_module parent;
int _result = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 214;BA.debugLine="Msgbox2Async(\"Are you sure you want to update use";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update user account?"+anywheresoftware.b4a.keywords.Common.CRLF+"Time span: 1-2 minutes"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 215;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 216;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 217;BA.debugLine="UPDATE_USER";
_update_user();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _carousel() throws Exception{
 //BA.debugLineNum = 82;BA.debugLine="Sub Carousel";
 //BA.debugLineNum = 84;BA.debugLine="blist.Initialize";
mostCurrent._blist.Initialize();
 //BA.debugLineNum = 86;BA.debugLine="bm.Initialize(File.DirAssets, \"image_1.jpg\")";
mostCurrent._bm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"image_1.jpg");
 //BA.debugLineNum = 87;BA.debugLine="blist.Add(bm)";
mostCurrent._blist.Add((Object)(mostCurrent._bm.getObject()));
 //BA.debugLineNum = 88;BA.debugLine="bm.Initialize(File.DirAssets, \"image_2.jpg\")";
mostCurrent._bm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"image_2.jpg");
 //BA.debugLineNum = 89;BA.debugLine="blist.Add(bm)";
mostCurrent._blist.Add((Object)(mostCurrent._bm.getObject()));
 //BA.debugLineNum = 97;BA.debugLine="cv1.ImageBitmaps = blist";
mostCurrent._cv1.setImageBitmaps((java.util.List)(mostCurrent._blist.getObject()));
 //BA.debugLineNum = 99;BA.debugLine="cv1.Radius = 5";
mostCurrent._cv1.setRadius((float) (5));
 //BA.debugLineNum = 100;BA.debugLine="cv1.StrokeColor = Colors.Gray";
mostCurrent._cv1.setStrokeColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 101;BA.debugLine="cv1.StrokeWidth = 3";
mostCurrent._cv1.setStrokeWidth((float) (3));
 //BA.debugLineNum = 102;BA.debugLine="cv1.FillColor = Colors.White";
mostCurrent._cv1.setFillColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 103;BA.debugLine="cv1.AutoPlay = True";
mostCurrent._cv1.setAutoPlay(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 104;BA.debugLine="cv1.CurrentItem = 0";
mostCurrent._cv1.setCurrentItem((int) (0));
 //BA.debugLineNum = 105;BA.debugLine="cv1.DisableAutoPlayOnUserInteraction = True";
mostCurrent._cv1.setDisableAutoPlayOnUserInteraction(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 106;BA.debugLine="cv1.Orientation = cv1.ORIENTATION_HORIZONTAL";
mostCurrent._cv1.setOrientation(mostCurrent._cv1.ORIENTATION_HORIZONTAL);
 //BA.debugLineNum = 107;BA.debugLine="cv1.PageCount = blist.Size";
mostCurrent._cv1.setPageCount(mostCurrent._blist.getSize());
 //BA.debugLineNum = 108;BA.debugLine="cv1.PageColor = Colors.Transparent";
mostCurrent._cv1.setPageColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 109;BA.debugLine="cv1.SlideInterval = 3000";
mostCurrent._cv1.setSlideInterval((int) (3000));
 //BA.debugLineNum = 111;BA.debugLine="cv1.playCarousel";
mostCurrent._cv1.playCarousel();
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return "";
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 237;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 238;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 239;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 240;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 241;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 242;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 232;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 233;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 234;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,login_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 235;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return null;
}
public static String  _get_last_log() throws Exception{
int _row = 0;
 //BA.debugLineNum = 315;BA.debugLine="Sub GET_LAST_LOG";
 //BA.debugLineNum = 316;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM use";
_cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM user_token_table")));
 //BA.debugLineNum = 317;BA.debugLine="If cursor3.RowCount > 0 Then";
if (_cursor3.getRowCount()>0) { 
 //BA.debugLineNum = 318;BA.debugLine="For row = 0 To cursor3.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor3.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit3 ;_row = _row + step3 ) {
 //BA.debugLineNum = 319;BA.debugLine="cursor3.Position = row";
_cursor3.setPosition(_row);
 //BA.debugLineNum = 320;BA.debugLine="LABEL_LOAD_LASTLOG.Text = \"Last User : \" & curs";
mostCurrent._label_load_lastlog.setText(BA.ObjectToCharSequence("Last User : "+_cursor3.GetString("user")+anywheresoftware.b4a.keywords.Common.CRLF+"Date Time Login : "+_cursor3.GetString("last_login")+anywheresoftware.b4a.keywords.Common.CRLF+"Date Time Logout : "+_cursor3.GetString("last_logout")));
 }
};
 };
 //BA.debugLineNum = 323;BA.debugLine="End Sub";
return "";
}
public static void  _get_token() throws Exception{
ResumableSub_GET_TOKEN rsub = new ResumableSub_GET_TOKEN(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_TOKEN extends BA.ResumableSub {
public ResumableSub_GET_TOKEN(wingan.app.login_module parent) {
this.parent = parent;
}
wingan.app.login_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 310;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM user_token_t";
parent._connection.ExecNonQuery("DELETE FROM user_token_table");
 //BA.debugLineNum = 311;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 312;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO user_token_t";
parent._connection.ExecNonQuery("INSERT INTO user_token_table VALUES ('"+parent.mostCurrent._spinner_user.getSelectedItem()+"','1','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','-')");
 //BA.debugLineNum = 313;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 34;BA.debugLine="Dim ctrl As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 37;BA.debugLine="Private cv1 As CarouselView";
mostCurrent._cv1 = new carouselviewwrapper.carouselViewWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim bm As Bitmap";
mostCurrent._bm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim blist As List";
mostCurrent._blist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 41;BA.debugLine="Private EDITTEXT_PASS As EditText";
mostCurrent._edittext_pass = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private SPINNER_USER As Spinner";
mostCurrent._spinner_user = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private LABEL_LOAD_LASTLOG As Label";
mostCurrent._label_load_lastlog = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static void  _load_user() throws Exception{
ResumableSub_LOAD_USER rsub = new ResumableSub_LOAD_USER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_USER extends BA.ResumableSub {
public ResumableSub_LOAD_USER(wingan.app.login_module parent) {
this.parent = parent;
}
wingan.app.login_module parent;
int _i = 0;
int step3;
int limit3;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 118;BA.debugLine="SPINNER_USER.Clear";
parent.mostCurrent._spinner_user.Clear();
 //BA.debugLineNum = 119;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT User FROM";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT User FROM users_table ORDER BY User ASC")));
 //BA.debugLineNum = 120;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step3 = 1;
limit3 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 121;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 122;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 123;BA.debugLine="SPINNER_USER.Add(cursor1.GetString(\"User\"))";
parent.mostCurrent._spinner_user.Add(parent._cursor1.GetString("User"));
 //BA.debugLineNum = 124;BA.debugLine="SPINNER_USER.DropdownBackgroundColor = Colors.Wh";
parent.mostCurrent._spinner_user.setDropdownBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _logger() throws Exception{
ResumableSub_LOGGER rsub = new ResumableSub_LOGGER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOGGER extends BA.ResumableSub {
public ResumableSub_LOGGER(wingan.app.login_module parent) {
this.parent = parent;
}
wingan.app.login_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 129;BA.debugLine="GET_TOKEN";
_get_token();
 //BA.debugLineNum = 130;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 131;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="Private rp As RuntimePermissions";
_rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 14;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim cursor2 As Cursor";
_cursor2 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim cursor3 As Cursor";
_cursor3 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim phone_id As PhoneId";
_phone_id = new anywheresoftware.b4a.phone.Phone.PhoneId();
 //BA.debugLineNum = 22;BA.debugLine="Dim connection As SQL";
_connection = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 25;BA.debugLine="Public tab_id As String";
_tab_id = "";
 //BA.debugLineNum = 26;BA.debugLine="Public username As String";
_username = "";
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 297;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 298;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 299;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 300;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 301;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 302;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 303;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 304;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 305;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 306;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 307;BA.debugLine="End Sub";
return "";
}
public static void  _update_sample() throws Exception{
ResumableSub_UPDATE_SAMPLE rsub = new ResumableSub_UPDATE_SAMPLE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_SAMPLE extends BA.ResumableSub {
public ResumableSub_UPDATE_SAMPLE(wingan.app.login_module parent) {
this.parent = parent;
}
wingan.app.login_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group8;
int index8;
int groupLen8;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 274;BA.debugLine="ProgressDialogShow2(\"Updating accounts...\", False";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Updating accounts..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 275;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 276;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_samp";
_cmd = _createcommand("select_sample",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 277;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 11;
return;
case 11:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 278;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 279;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 280;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 12;
return;
case 12:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 281;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 4:
//for
this.state = 7;
group8 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index8 = 0;
groupLen8 = group8.getSize();
this.state = 13;
if (true) break;

case 13:
//C
this.state = 7;
if (index8 < groupLen8) {
this.state = 6;
_row = (Object[])(group8.Get(index8));}
if (true) break;

case 14:
//C
this.state = 13;
index8++;
if (true) break;

case 6:
//C
this.state = 14;
 //BA.debugLineNum = 282;BA.debugLine="Msgbox(row(res.Columns.Get(\"principal_name\")),F";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_name"))))]),BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.False),mostCurrent.activityBA);
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 284;BA.debugLine="ProgressDialogShow2(\"Account succesfully updated";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Account succesfully updated."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 285;BA.debugLine="Sleep(2000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (2000));
this.state = 15;
return;
case 15:
//C
this.state = 10;
;
 //BA.debugLineNum = 286;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 287;BA.debugLine="LOAD_USER";
_load_user();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 289;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("71245200","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 290;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 291;BA.debugLine="Msgbox2Async(\"ACCOUNT DATA IS NOT UPDATED.\" & CR";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("ACCOUNT DATA IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 293;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 294;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _jr) throws Exception{
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static void  _update_user() throws Exception{
ResumableSub_UPDATE_USER rsub = new ResumableSub_UPDATE_USER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_USER extends BA.ResumableSub {
public ResumableSub_UPDATE_USER(wingan.app.login_module parent) {
this.parent = parent;
}
wingan.app.login_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
byte[] _buffer2 = null;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inputstream1 = null;
anywheresoftware.b4a.BA.IterableList group9;
int index9;
int groupLen9;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 245;BA.debugLine="ProgressDialogShow2(\"Updating accounts...\", False";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Updating accounts..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 246;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 247;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_user";
_cmd = _createcommand("select_user",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 248;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 15;
return;
case 15:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 249;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 14;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 250;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM users_table";
parent._connection.ExecNonQuery("DELETE FROM users_table");
 //BA.debugLineNum = 251;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 252;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 16;
return;
case 16:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 253;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 4:
//for
this.state = 11;
group9 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index9 = 0;
groupLen9 = group9.getSize();
this.state = 17;
if (true) break;

case 17:
//C
this.state = 11;
if (index9 < groupLen9) {
this.state = 6;
_row = (Object[])(group9.Get(index9));}
if (true) break;

case 18:
//C
this.state = 17;
index9++;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 254;BA.debugLine="Dim buffer2() As Byte";
_buffer2 = new byte[(int) (0)];
;
 //BA.debugLineNum = 255;BA.debugLine="buffer2 = row(res.Columns.Get(\"picture\"))";
_buffer2 = (byte[])(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("picture"))))]);
 //BA.debugLineNum = 256;BA.debugLine="Dim InputStream1 As InputStream";
_inputstream1 = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 257;BA.debugLine="InputStream1.InitializeFromBytesArray(buffer2,";
_inputstream1.InitializeFromBytesArray(_buffer2,(int) (0),_buffer2.length);
 //BA.debugLineNum = 258;BA.debugLine="If row(res.Columns.Get(\"Department\")) <> \"SALES";
if (true) break;

case 7:
//if
this.state = 10;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("Department"))))]).equals((Object)("SALES DEPARTMENT")) == false) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 259;BA.debugLine="connection.ExecNonQuery2(\"INSERT INTO users_ta";
parent._connection.ExecNonQuery2("INSERT INTO users_table VALUES (?,?,?,?,?)",anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("User"))))],_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("Pass"))))],_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("Department"))))],_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("Position"))))],(Object)(_buffer2)}));
 if (true) break;

case 10:
//C
this.state = 18;
;
 if (true) break;
if (true) break;

case 11:
//C
this.state = 14;
;
 //BA.debugLineNum = 262;BA.debugLine="ProgressDialogShow2(\"Account succesfully updated";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Account succesfully updated."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 263;BA.debugLine="Sleep(2000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (2000));
this.state = 19;
return;
case 19:
//C
this.state = 14;
;
 //BA.debugLineNum = 264;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 265;BA.debugLine="LOAD_USER";
_load_user();
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 267;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("71179671","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 268;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 269;BA.debugLine="Msgbox2Async(\"ACCOUNT DATA IS NOT UPDATED.\" & CR";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("ACCOUNT DATA IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 271;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 272;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
}
