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

public class dashboard_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static dashboard_module mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.dashboard_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (dashboard_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.dashboard_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.dashboard_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (dashboard_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (dashboard_module) Resume **");
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
		return dashboard_module.class;
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
            BA.LogInfo("** Activity (dashboard_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (dashboard_module) Pause event (activity is not paused). **");
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
            dashboard_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (dashboard_module) Resume **");
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
public static anywheresoftware.b4a.sql.SQL _connection = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor2 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor3 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor4 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor5 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor6 = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _logoutbitmap = null;
public static anywheresoftware.b4a.obejcts.TTS _tts1 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrollview1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_returns = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xdrawer _drawer = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview_menu = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _img_profile = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_name = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_position = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_dept = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_receiving = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_position_dash = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_dept_dash = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_name_dash = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _img_profile_dash = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_last_producttbl = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_last_warehouse = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_last_principal = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_last_expiration = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.login_module _login_module = null;
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
public ResumableSub_Activity_Create(wingan.app.dashboard_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.dashboard_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 73;BA.debugLine="logoutBitmap = LoadBitmap(File.DirAssets, \"logout";
parent._logoutbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logout.png");
 //BA.debugLineNum = 75;BA.debugLine="Drawer.Initialize(Me, \"Drawer\", Activity, 65%x)";
parent.mostCurrent._drawer._initialize /*String*/ (mostCurrent.activityBA,dashboard_module.getObject(),"Drawer",(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (65),mostCurrent.activityBA));
 //BA.debugLineNum = 76;BA.debugLine="Drawer.CenterPanel.LoadLayout(\"dashboard\")";
parent.mostCurrent._drawer._getcenterpanel /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().LoadLayout("dashboard",mostCurrent.activityBA);
 //BA.debugLineNum = 78;BA.debugLine="If File.Exists(File.DirRootExternal & \"/WING AN A";
if (true) break;

case 1:
//if
this.state = 4;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 79;BA.debugLine="File.Copy(File.DirAssets,\"tablet_db.db\", File.Di";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"tablet_db.db",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db");
 if (true) break;
;
 //BA.debugLineNum = 82;BA.debugLine="If connection.IsInitialized = False Then";

case 4:
//if
this.state = 7;
if (parent._connection.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 83;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 86;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 87;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"wms_logo.png")).getObject()));
 //BA.debugLineNum = 88;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 89;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 90;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"menu.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu.png").getObject()));
 //BA.debugLineNum = 91;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 92;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 93;BA.debugLine="Drawer.LeftPanel.LoadLayout(\"drawer\")";
parent.mostCurrent._drawer._getleftpanel /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().LoadLayout("drawer",mostCurrent.activityBA);
 //BA.debugLineNum = 95;BA.debugLine="LOAD_LISTVIEW_MENU";
_load_listview_menu();
 //BA.debugLineNum = 97;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 98;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 99;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 101;BA.debugLine="ScrollView1.Panel.LoadLayout(\"dashboard_scrollvie";
parent.mostCurrent._scrollview1.getPanel().LoadLayout("dashboard_scrollview",mostCurrent.activityBA);
 //BA.debugLineNum = 102;BA.debugLine="ScrollView1.Panel.Height = PANEL_RETURNS.Top + PA";
parent.mostCurrent._scrollview1.getPanel().setHeight((int) (parent.mostCurrent._panel_returns.getTop()+parent.mostCurrent._panel_returns.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 //BA.debugLineNum = 103;BA.debugLine="ScrollView1.Panel.Width = 120%x";
parent.mostCurrent._scrollview1.getPanel().setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (120),mostCurrent.activityBA));
 //BA.debugLineNum = 105;BA.debugLine="LOAD_USER_INFO";
_load_user_info();
 //BA.debugLineNum = 106;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 8;
;
 //BA.debugLineNum = 107;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 108;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 110;BA.debugLine="If FirstTime = True Then";
if (true) break;

case 8:
//if
this.state = 11;
if (_firsttime==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 10;
}if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 111;BA.debugLine="VERIFY_PRODUCTTBL";
_verify_producttbl();
 if (true) break;

case 11:
//C
this.state = -1;
;
 //BA.debugLineNum = 114;BA.debugLine="Log(LOGIN_MODULE.username)";
anywheresoftware.b4a.keywords.Common.LogImpl("71638444",parent.mostCurrent._login_module._username /*String*/ ,0);
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;
 //BA.debugLineNum = 128;BA.debugLine="Sub Activity_CreateMenu(Menu As ACMenu)";
 //BA.debugLineNum = 129;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("logout"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 130;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 131;BA.debugLine="UpdateIcon(\"logout\", logoutBitmap)";
_updateicon("logout",_logoutbitmap);
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 263;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 264;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK And Drawer.Lef";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK && mostCurrent._drawer._getleftopen /*boolean*/ ()) { 
 //BA.debugLineNum = 265;BA.debugLine="Drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 266;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 268;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 123;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 118;BA.debugLine="If TTS1.IsInitialized = False Then";
if (_tts1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 119;BA.debugLine="TTS1.Initialize(\"TTS1\")";
_tts1.Initialize(processBA,"TTS1");
 };
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.dashboard_module parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.dashboard_module parent;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item;
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
 //BA.debugLineNum = 295;BA.debugLine="If Item.Title = \"logout\" Then";
if (true) break;

case 1:
//if
this.state = 8;
if ((_item.getTitle()).equals("logout")) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 296;BA.debugLine="Msgbox2Async(\"Are you sure you want to log-out?\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to log-out?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 297;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 9;
return;
case 9:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 298;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 4:
//if
this.state = 7;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 299;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 300;BA.debugLine="StartActivity(LOGIN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._login_module.getObject()));
 //BA.debugLineNum = 301;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 302;BA.debugLine="connection.ExecNonQuery(\"UPDATE user_token_tabl";
parent._connection.ExecNonQuery("UPDATE user_token_table SET token = '0', last_logout = '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"'");
 if (true) break;

case 7:
//C
this.state = 8;
;
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 305;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static void  _actoolbarlight1_navigationitemclick() throws Exception{
ResumableSub_ACToolBarLight1_NavigationItemClick rsub = new ResumableSub_ACToolBarLight1_NavigationItemClick(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_NavigationItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_NavigationItemClick(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 272;BA.debugLine="Drawer.LeftOpen = Not(Drawer.LeftOpen)";
parent.mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.Not(parent.mostCurrent._drawer._getleftopen /*boolean*/ ()));
 //BA.debugLineNum = 273;BA.debugLine="LOG_PRINCIPAL";
_log_principal();
 //BA.debugLineNum = 274;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 275;BA.debugLine="LOG_PRODUCTTBL";
_log_producttbl();
 //BA.debugLineNum = 276;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 277;BA.debugLine="LOG_WAREHOUSE";
_log_warehouse();
 //BA.debugLineNum = 278;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 3;
return;
case 3:
//C
this.state = -1;
;
 //BA.debugLineNum = 279;BA.debugLine="LOG_EXPIRATION";
_log_expiration();
 //BA.debugLineNum = 280;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 258;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 259;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 260;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 261;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 262;BA.debugLine="End Sub";
return null;
}
public static String  _button_daily_inventory_click() throws Exception{
 //BA.debugLineNum = 216;BA.debugLine="Sub BUTTON_DAILY_INVENTORY_Click";
 //BA.debugLineNum = 217;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 218;BA.debugLine="StartActivity(DAILY_INVENTORY_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._daily_inventory_module.getObject()));
 //BA.debugLineNum = 219;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\")";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 220;BA.debugLine="DAILY_INVENTORY_MODULE.clear_trigger = 1";
mostCurrent._daily_inventory_module._clear_trigger /*String*/  = BA.NumberToString(1);
 //BA.debugLineNum = 221;BA.debugLine="End Sub";
return "";
}
public static String  _button_expiration_click() throws Exception{
 //BA.debugLineNum = 235;BA.debugLine="Sub BUTTON_EXPIRATION_Click";
 //BA.debugLineNum = 236;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 237;BA.debugLine="StartActivity(EXPIRATION_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._expiration_module.getObject()));
 //BA.debugLineNum = 238;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\")";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 239;BA.debugLine="End Sub";
return "";
}
public static String  _button_monthly_inventory_click() throws Exception{
 //BA.debugLineNum = 210;BA.debugLine="Sub BUTTON_MONTHLY_INVENTORY_Click";
 //BA.debugLineNum = 211;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 212;BA.debugLine="StartActivity(MONTHLY_INVENTORY_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._monthly_inventory_module.getObject()));
 //BA.debugLineNum = 213;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\")";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 214;BA.debugLine="End Sub";
return "";
}
public static String  _button_monthly_sound_click() throws Exception{
 //BA.debugLineNum = 598;BA.debugLine="Sub BUTTON_MONTHLY_SOUND_Click";
 //BA.debugLineNum = 599;BA.debugLine="TTS1.Speak(\"Monthly Inventory, Inputting the inve";
_tts1.Speak("Monthly Inventory, Inputting the inventory on-hand of actual stocks every monthly cutoff.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 600;BA.debugLine="End Sub";
return "";
}
public static String  _button_picklist_loading_click() throws Exception{
 //BA.debugLineNum = 229;BA.debugLine="Sub BUTTON_PICKLIST_LOADING_Click";
 //BA.debugLineNum = 230;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 231;BA.debugLine="StartActivity(LOADING_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._loading_module.getObject()));
 //BA.debugLineNum = 232;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\")";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 233;BA.debugLine="End Sub";
return "";
}
public static String  _button_picklist_preparing_click() throws Exception{
 //BA.debugLineNum = 223;BA.debugLine="Sub BUTTON_PICKLIST_PREPARING_Click";
 //BA.debugLineNum = 224;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 225;BA.debugLine="StartActivity(PREPARING_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._preparing_module.getObject()));
 //BA.debugLineNum = 226;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\")";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 227;BA.debugLine="End Sub";
return "";
}
public static String  _button_pricecheck_click() throws Exception{
 //BA.debugLineNum = 691;BA.debugLine="Sub BUTTON_PRICECHECK_Click";
 //BA.debugLineNum = 692;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 693;BA.debugLine="StartActivity(PRICE_CHECKING_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._price_checking_module.getObject()));
 //BA.debugLineNum = 694;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\")";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 695;BA.debugLine="End Sub";
return "";
}
public static String  _button_receiving_click() throws Exception{
 //BA.debugLineNum = 241;BA.debugLine="Sub BUTTON_RECEIVING_Click";
 //BA.debugLineNum = 242;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 243;BA.debugLine="StartActivity(RECEIVING_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._receiving_module.getObject()));
 //BA.debugLineNum = 244;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\")";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 245;BA.debugLine="End Sub";
return "";
}
public static String  _button_return_click() throws Exception{
 //BA.debugLineNum = 697;BA.debugLine="Sub BUTTON_RETURN_Click";
 //BA.debugLineNum = 698;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 699;BA.debugLine="StartActivity(RETURN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._return_module.getObject()));
 //BA.debugLineNum = 700;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\")";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 701;BA.debugLine="End Sub";
return "";
}
public static void  _button_update_expiration_click() throws Exception{
ResumableSub_BUTTON_UPDATE_EXPIRATION_Click rsub = new ResumableSub_BUTTON_UPDATE_EXPIRATION_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPDATE_EXPIRATION_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPDATE_EXPIRATION_Click(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
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
 //BA.debugLineNum = 683;BA.debugLine="Msgbox2Async(\"Are you sure to update your local e";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure to update your local expiration data?"),BA.ObjectToCharSequence("Sync Expiration Data"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 684;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 685;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 686;BA.debugLine="Drawer.LeftOpen = False";
parent.mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 687;BA.debugLine="DOWNLOAD_RECEIVING_EXPIRATION";
_download_receiving_expiration();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 689;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_update_principal_click() throws Exception{
ResumableSub_BUTTON_UPDATE_PRINCIPAL_Click rsub = new ResumableSub_BUTTON_UPDATE_PRINCIPAL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPDATE_PRINCIPAL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPDATE_PRINCIPAL_Click(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
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
 //BA.debugLineNum = 673;BA.debugLine="Msgbox2Async(\"Are you sure you want to update pri";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update principal data?"+anywheresoftware.b4a.keywords.Common.CRLF+"Time span: 5 seconds"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 674;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 675;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 676;BA.debugLine="Drawer.LeftOpen = False";
parent.mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 677;BA.debugLine="UPDATE_PRINCIPAL";
_update_principal();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 681;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_update_producttbl_click() throws Exception{
ResumableSub_BUTTON_UPDATE_PRODUCTTBL_Click rsub = new ResumableSub_BUTTON_UPDATE_PRODUCTTBL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPDATE_PRODUCTTBL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPDATE_PRODUCTTBL_Click(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
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
 //BA.debugLineNum = 652;BA.debugLine="Msgbox2Async(\"Are you sure you want to update pro";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update product data?"+anywheresoftware.b4a.keywords.Common.CRLF+"Time span: 1-2 minutes"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 653;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 654;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 655;BA.debugLine="Drawer.LeftOpen = False";
parent.mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 656;BA.debugLine="UPDATE_PRODUCTTBL";
_update_producttbl();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 661;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_update_warehouse_click() throws Exception{
ResumableSub_BUTTON_UPDATE_WAREHOUSE_Click rsub = new ResumableSub_BUTTON_UPDATE_WAREHOUSE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_UPDATE_WAREHOUSE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_UPDATE_WAREHOUSE_Click(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
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
 //BA.debugLineNum = 663;BA.debugLine="Msgbox2Async(\"Are you sure you want to update war";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update warehouse data?"+anywheresoftware.b4a.keywords.Common.CRLF+"Time span: 5 seconds"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 664;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 665;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 666;BA.debugLine="Drawer.LeftOpen = False";
parent.mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 667;BA.debugLine="UPDATE_WAREHOUSE";
_update_warehouse();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 671;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 337;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 338;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 339;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 340;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 341;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 342;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 343;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 332;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 333;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 334;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,dashboard_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 335;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 336;BA.debugLine="End Sub";
return null;
}
public static anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper  _createroundbitmap(anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _input,int _size) throws Exception{
int _l = 0;
anywheresoftware.b4a.objects.B4XCanvas _c = null;
anywheresoftware.b4a.objects.B4XViewWrapper _xview = null;
anywheresoftware.b4a.objects.B4XCanvas.B4XPath _path = null;
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _res = null;
 //BA.debugLineNum = 171;BA.debugLine="Sub CreateRoundBitmap (Input As B4XBitmap, Size As";
 //BA.debugLineNum = 172;BA.debugLine="If Input.Width <> Input.Height Then";
if (_input.getWidth()!=_input.getHeight()) { 
 //BA.debugLineNum = 174;BA.debugLine="Dim l As Int = Min(Input.Width, Input.Height)";
_l = (int) (anywheresoftware.b4a.keywords.Common.Min(_input.getWidth(),_input.getHeight()));
 //BA.debugLineNum = 175;BA.debugLine="Input = Input.Crop(Input.Width / 2 - l / 2, Inpu";
_input = _input.Crop((int) (_input.getWidth()/(double)2-_l/(double)2),(int) (_input.getHeight()/(double)2-_l/(double)2),_l,_l);
 };
 //BA.debugLineNum = 177;BA.debugLine="Dim c As B4XCanvas";
_c = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 178;BA.debugLine="Dim xview As B4XView = xui.CreatePanel(\"\")";
_xview = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xview = mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 179;BA.debugLine="xview.SetLayoutAnimated(0, 0, 0, Size, Size)";
_xview.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_size,_size);
 //BA.debugLineNum = 180;BA.debugLine="c.Initialize(xview)";
_c.Initialize(_xview);
 //BA.debugLineNum = 181;BA.debugLine="Dim path As B4XPath";
_path = new anywheresoftware.b4a.objects.B4XCanvas.B4XPath();
 //BA.debugLineNum = 182;BA.debugLine="path.InitializeOval(c.TargetRect)";
_path.InitializeOval(_c.getTargetRect());
 //BA.debugLineNum = 183;BA.debugLine="c.ClipPath(path)";
_c.ClipPath(_path);
 //BA.debugLineNum = 184;BA.debugLine="c.DrawBitmap(Input.Resize(Size, Size, False), c.T";
_c.DrawBitmap((android.graphics.Bitmap)(_input.Resize(_size,_size,anywheresoftware.b4a.keywords.Common.False).getObject()),_c.getTargetRect());
 //BA.debugLineNum = 185;BA.debugLine="c.RemoveClip";
_c.RemoveClip();
 //BA.debugLineNum = 187;BA.debugLine="c.Invalidate";
_c.Invalidate();
 //BA.debugLineNum = 188;BA.debugLine="Dim res As B4XBitmap = c.CreateBitmap";
_res = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_res = _c.CreateBitmap();
 //BA.debugLineNum = 189;BA.debugLine="c.Release";
_c.Release();
 //BA.debugLineNum = 190;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 191;BA.debugLine="End Sub";
return null;
}
public static void  _download_receiving_expiration() throws Exception{
ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION rsub = new ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION extends BA.ResumableSub {
public ResumableSub_DOWNLOAD_RECEIVING_EXPIRATION(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group17;
int index17;
int groupLen17;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 486;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 487;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 488;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_expi";
_cmd = _createcommand("select_expiration",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 489;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 490;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 16;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 15;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 491;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 492;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 4;
;
 //BA.debugLineNum = 493;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 494;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 495;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 496;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM product_exp";
parent._connection.ExecNonQuery("DELETE FROM product_expiration_ref_table");
 //BA.debugLineNum = 497;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 4;
;
 //BA.debugLineNum = 498;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 499;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 20;
return;
case 20:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 500;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("73342351",BA.NumberToString(2),0);
 //BA.debugLineNum = 501;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 13;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 12;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 502;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group17 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index17 = 0;
groupLen17 = group17.getSize();
this.state = 21;
if (true) break;

case 21:
//C
this.state = 10;
if (index17 < groupLen17) {
this.state = 9;
_row = (Object[])(group17.Get(index17));}
if (true) break;

case 22:
//C
this.state = 21;
index17++;
if (true) break;

case 9:
//C
this.state = 22;
 //BA.debugLineNum = 503;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO product_e";
parent._connection.ExecNonQuery("INSERT INTO product_expiration_ref_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("document_ref_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("quantity"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_expired"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("time_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("legend"))))])+"')");
 //BA.debugLineNum = 507;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 22;
;
 //BA.debugLineNum = 508;BA.debugLine="LABEL_MSGBOX2.Text = \"Downloading Expiration :";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Downloading Expiration : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])));
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 //BA.debugLineNum = 511;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 13;
;
 //BA.debugLineNum = 512;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM database_u";
parent._connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Expiration Table'");
 //BA.debugLineNum = 513;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 13;
;
 //BA.debugLineNum = 514;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO database_u";
parent._connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Expiration Table', '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', '"+parent.mostCurrent._login_module._username /*String*/ +"')");
 //BA.debugLineNum = 515;BA.debugLine="LABEL_MSGBOX2.Text = \"Expiration Downloaded Suc";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Expiration Downloaded Successfully"));
 //BA.debugLineNum = 516;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 26;
return;
case 26:
//C
this.state = 13;
;
 //BA.debugLineNum = 517;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 518;BA.debugLine="LOG_EXPIRATION";
_log_expiration();
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 520;BA.debugLine="ToastMessageShow(\"No expiration data for this P";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No expiration data for this Principal"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 521;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 27;
return;
case 27:
//C
this.state = 13;
;
 //BA.debugLineNum = 522;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 525;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 526;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 28;
return;
case 28:
//C
this.state = 16;
;
 //BA.debugLineNum = 527;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 528;BA.debugLine="Msgbox2Async(\"EXPIRATION TABLE IS NOT UPDATED.\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("EXPIRATION TABLE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 529;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 531;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 532;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _jr) throws Exception{
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static String  _get_login() throws Exception{
 //BA.debugLineNum = 535;BA.debugLine="Sub GET_LOGIN";
 //BA.debugLineNum = 536;BA.debugLine="If LOGIN_MODULE.phone_id.GetDeviceId = \"35294809";
if ((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("352948097840379")) { 
 //BA.debugLineNum = 537;BA.debugLine="LOGIN_MODULE.tab_id = \"DEBUGGER\"";
mostCurrent._login_module._tab_id /*String*/  = "DEBUGGER";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042456148")) { 
 //BA.debugLineNum = 539;BA.debugLine="LOGIN_MODULE.tab_id = \"A\"";
mostCurrent._login_module._tab_id /*String*/  = "A";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042438930")) { 
 //BA.debugLineNum = 541;BA.debugLine="LOGIN_MODULE.tab_id = \"B\"";
mostCurrent._login_module._tab_id /*String*/  = "B";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042452949")) { 
 //BA.debugLineNum = 543;BA.debugLine="LOGIN_MODULE.tab_id = \"C\"";
mostCurrent._login_module._tab_id /*String*/  = "C";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042466881")) { 
 //BA.debugLineNum = 545;BA.debugLine="LOGIN_MODULE.tab_id = \"D\"";
mostCurrent._login_module._tab_id /*String*/  = "D";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("359667094931063")) { 
 //BA.debugLineNum = 547;BA.debugLine="LOGIN_MODULE.tab_id = \"S-D\"";
mostCurrent._login_module._tab_id /*String*/  = "S-D";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042467103")) { 
 //BA.debugLineNum = 549;BA.debugLine="LOGIN_MODULE.tab_id = \"E\"";
mostCurrent._login_module._tab_id /*String*/  = "E";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("359667094930842")) { 
 //BA.debugLineNum = 551;BA.debugLine="LOGIN_MODULE.tab_id = \"S-E\"";
mostCurrent._login_module._tab_id /*String*/  = "S-E";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042658685")) { 
 //BA.debugLineNum = 553;BA.debugLine="LOGIN_MODULE.tab_id = \"F\"";
mostCurrent._login_module._tab_id /*String*/  = "F";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("359667094931014")) { 
 //BA.debugLineNum = 555;BA.debugLine="LOGIN_MODULE.tab_id = \"S-F\"";
mostCurrent._login_module._tab_id /*String*/  = "S-F";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042464068")) { 
 //BA.debugLineNum = 557;BA.debugLine="LOGIN_MODULE.tab_id = \"G\"";
mostCurrent._login_module._tab_id /*String*/  = "G";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042672041")) { 
 //BA.debugLineNum = 559;BA.debugLine="LOGIN_MODULE.tab_id = \"H\"";
mostCurrent._login_module._tab_id /*String*/  = "H";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("863129042450661")) { 
 //BA.debugLineNum = 561;BA.debugLine="LOGIN_MODULE.tab_id = \"I\"";
mostCurrent._login_module._tab_id /*String*/  = "I";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("356136101100354")) { 
 //BA.debugLineNum = 563;BA.debugLine="LOGIN_MODULE.tab_id = \"S-M\"";
mostCurrent._login_module._tab_id /*String*/  = "S-M";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("356136101101360")) { 
 //BA.debugLineNum = 565;BA.debugLine="LOGIN_MODULE.tab_id = \"S-N\"";
mostCurrent._login_module._tab_id /*String*/  = "S-N";
 }else if((mostCurrent._login_module._phone_id /*anywheresoftware.b4a.phone.Phone.PhoneId*/ .GetDeviceId()).equals("356136101096925")) { 
 //BA.debugLineNum = 567;BA.debugLine="LOGIN_MODULE.tab_id = \"S-O\"";
mostCurrent._login_module._tab_id /*String*/  = "S-O";
 }else {
 //BA.debugLineNum = 569;BA.debugLine="Msgbox2Async(\"Your device is not registered, Pl";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Your device is not registered, Please inform your IT Department"),BA.ObjectToCharSequence("Block"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 571;BA.debugLine="End Sub";
return "";
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 285;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 286;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 287;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 288;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 289;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 292;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 31;BA.debugLine="Private ScrollView1 As ScrollView";
mostCurrent._scrollview1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private PANEL_RETURNS As Panel";
mostCurrent._panel_returns = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 35;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 37;BA.debugLine="Private Drawer As B4XDrawer";
mostCurrent._drawer = new wingan.app.b4xdrawer();
 //BA.debugLineNum = 38;BA.debugLine="Private LISTVIEW_MENU As ListView";
mostCurrent._listview_menu = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private IMG_PROFILE As ImageView";
mostCurrent._img_profile = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private LABEL_LOAD_NAME As Label";
mostCurrent._label_load_name = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private LABEL_LOAD_POSITION As Label";
mostCurrent._label_load_position = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private LABEL_LOAD_DEPT As Label";
mostCurrent._label_load_dept = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private BUTTON_RECEIVING As Button";
mostCurrent._button_receiving = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private LABEL_LOAD_POSITION_DASH As Label";
mostCurrent._label_load_position_dash = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private LABEL_LOAD_DEPT_DASH As Label";
mostCurrent._label_load_dept_dash = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private LABEL_LOAD_NAME_DASH As Label";
mostCurrent._label_load_name_dash = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private IMG_PROFILE_DASH As ImageView";
mostCurrent._img_profile_dash = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private LABEL_LAST_PRODUCTTBL As Label";
mostCurrent._label_last_producttbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private LABEL_LAST_WAREHOUSE As Label";
mostCurrent._label_last_warehouse = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private LABEL_LAST_PRINCIPAL As Label";
mostCurrent._label_last_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private LABEL_LAST_EXPIRATION As Label";
mostCurrent._label_last_expiration = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static void  _listview_menu_itemclick(int _position,Object _value) throws Exception{
ResumableSub_LISTVIEW_MENU_ItemClick rsub = new ResumableSub_LISTVIEW_MENU_ItemClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LISTVIEW_MENU_ItemClick extends BA.ResumableSub {
public ResumableSub_LISTVIEW_MENU_ItemClick(wingan.app.dashboard_module parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.dashboard_module parent;
int _position;
Object _value;
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
 //BA.debugLineNum = 308;BA.debugLine="If Position = 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (_position==0) { 
this.state = 3;
}else if(_position==1) { 
this.state = 5;
}else if(_position==2) { 
this.state = 7;
}else if(_position==3) { 
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 14;
 if (true) break;

case 5:
//C
this.state = 14;
 //BA.debugLineNum = 311;BA.debugLine="Drawer.LeftOpen = False";
parent.mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 312;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 14;
;
 //BA.debugLineNum = 313;BA.debugLine="StartActivity(DATABASE_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._database_module.getObject()));
 //BA.debugLineNum = 314;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left\"";
_setanimation("right_to_center","center_to_left");
 if (true) break;

case 7:
//C
this.state = 14;
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 319;BA.debugLine="Msgbox2Async(\"Are you sure you want to log-out?\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to log-out?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 320;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 10;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 321;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 10:
//if
this.state = 13;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 322;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 323;BA.debugLine="StartActivity(LOGIN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._login_module.getObject()));
 //BA.debugLineNum = 324;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\"";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 325;BA.debugLine="connection.ExecNonQuery(\"UPDATE user_token_tabl";
parent._connection.ExecNonQuery("UPDATE user_token_table SET token = '0', last_logout = '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"'");
 if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 329;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_listview_menu() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 193;BA.debugLine="Sub LOAD_LISTVIEW_MENU";
 //BA.debugLineNum = 194;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 195;BA.debugLine="bg.Initialize2(Colors.White, 5, 0, Colors.Black)";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 196;BA.debugLine="LISTVIEW_MENU.Background = bg";
mostCurrent._listview_menu.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 197;BA.debugLine="LISTVIEW_MENU.Clear";
mostCurrent._listview_menu.Clear();
 //BA.debugLineNum = 198;BA.debugLine="LISTVIEW_MENU.SingleLineLayout.Label.Typeface = T";
mostCurrent._listview_menu.getSingleLineLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 199;BA.debugLine="LISTVIEW_MENU.SingleLineLayout.Label.TextSize = 1";
mostCurrent._listview_menu.getSingleLineLayout().Label.setTextSize((float) (16));
 //BA.debugLineNum = 200;BA.debugLine="LISTVIEW_MENU.SingleLineLayout.label.Height = 6%y";
mostCurrent._listview_menu.getSingleLineLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 201;BA.debugLine="LISTVIEW_MENU.SingleLineLayout.ItemHeight = 6%y";
mostCurrent._listview_menu.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 202;BA.debugLine="LISTVIEW_MENU.SingleLineLayout.Label.TextColor =";
mostCurrent._listview_menu.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 203;BA.debugLine="LISTVIEW_MENU.SingleLineLayout.Label.Gravity = Gr";
mostCurrent._listview_menu.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 204;BA.debugLine="LISTVIEW_MENU.AddSingleLine(\"     Users Settings";
mostCurrent._listview_menu.AddSingleLine(BA.ObjectToCharSequence("     Users Settings"));
 //BA.debugLineNum = 205;BA.debugLine="LISTVIEW_MENU.AddSingleLine(\"     Update Databas";
mostCurrent._listview_menu.AddSingleLine(BA.ObjectToCharSequence("     Update Database"));
 //BA.debugLineNum = 206;BA.debugLine="LISTVIEW_MENU.AddSingleLine(\"     About Us\")";
mostCurrent._listview_menu.AddSingleLine(BA.ObjectToCharSequence("     About Us"));
 //BA.debugLineNum = 207;BA.debugLine="LISTVIEW_MENU.AddSingleLine(\"     Log-out\")";
mostCurrent._listview_menu.AddSingleLine(BA.ObjectToCharSequence("     Log-out"));
 //BA.debugLineNum = 208;BA.debugLine="End Sub";
return "";
}
public static String  _load_user_info() throws Exception{
int _row = 0;
byte[] _buffer = null;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inputstream1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap1 = null;
anywheresoftware.b4a.objects.B4XViewWrapper _xiv = null;
 //BA.debugLineNum = 134;BA.debugLine="Sub LOAD_USER_INFO";
 //BA.debugLineNum = 135;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM use";
_cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM users_table where user = '"+mostCurrent._login_module._username /*String*/ +"'")));
 //BA.debugLineNum = 136;BA.debugLine="If cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 137;BA.debugLine="For row = 0 To cursor1.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (_cursor1.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit3 ;_row = _row + step3 ) {
 //BA.debugLineNum = 138;BA.debugLine="cursor1.Position = row";
_cursor1.setPosition(_row);
 //BA.debugLineNum = 139;BA.debugLine="Dim Buffer() As Byte 'declare an empty byte arr";
_buffer = new byte[(int) (0)];
;
 //BA.debugLineNum = 140;BA.debugLine="Buffer = cursor1.GetBlob(\"Picture\")";
_buffer = _cursor1.GetBlob("Picture");
 //BA.debugLineNum = 141;BA.debugLine="Dim InputStream1 As InputStream";
_inputstream1 = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 142;BA.debugLine="InputStream1.InitializeFromBytesArray(Buffer, 0";
_inputstream1.InitializeFromBytesArray(_buffer,(int) (0),_buffer.length);
 //BA.debugLineNum = 144;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 145;BA.debugLine="Bitmap1.Initialize2(InputStream1)";
_bitmap1.Initialize2((java.io.InputStream)(_inputstream1.getObject()));
 //BA.debugLineNum = 146;BA.debugLine="InputStream1.Close";
_inputstream1.Close();
 //BA.debugLineNum = 147;BA.debugLine="IMG_PROFILE.Bitmap = Bitmap1";
mostCurrent._img_profile.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 149;BA.debugLine="Dim xIV As B4XView = IMG_PROFILE";
_xiv = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xiv = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._img_profile.getObject()));
 //BA.debugLineNum = 150;BA.debugLine="xIV.SetBitmap(CreateRoundBitmap(Bitmap1, xIV .W";
_xiv.SetBitmap((android.graphics.Bitmap)(_createroundbitmap((anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper(), (android.graphics.Bitmap)(_bitmap1.getObject())),_xiv.getWidth()).getObject()));
 //BA.debugLineNum = 152;BA.debugLine="LABEL_LOAD_NAME.Text = cursor1.GetString(\"User\"";
mostCurrent._label_load_name.setText(BA.ObjectToCharSequence(_cursor1.GetString("User")));
 //BA.debugLineNum = 153;BA.debugLine="LABEL_LOAD_DEPT.Text = \"Deparment : \" & cursor1";
mostCurrent._label_load_dept.setText(BA.ObjectToCharSequence("Deparment : "+_cursor1.GetString("Department")));
 //BA.debugLineNum = 154;BA.debugLine="LABEL_LOAD_POSITION.Text = \"Position : \" & curs";
mostCurrent._label_load_position.setText(BA.ObjectToCharSequence("Position : "+_cursor1.GetString("Position")));
 //BA.debugLineNum = 156;BA.debugLine="Dim xIV As B4XView = IMG_PROFILE_DASH";
_xiv = new anywheresoftware.b4a.objects.B4XViewWrapper();
_xiv = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._img_profile_dash.getObject()));
 //BA.debugLineNum = 157;BA.debugLine="xIV.SetBitmap(CreateRoundBitmap(Bitmap1, xIV .W";
_xiv.SetBitmap((android.graphics.Bitmap)(_createroundbitmap((anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper(), (android.graphics.Bitmap)(_bitmap1.getObject())),_xiv.getWidth()).getObject()));
 //BA.debugLineNum = 159;BA.debugLine="LABEL_LOAD_NAME_DASH.Text = cursor1.GetString(\"";
mostCurrent._label_load_name_dash.setText(BA.ObjectToCharSequence(_cursor1.GetString("User")));
 //BA.debugLineNum = 160;BA.debugLine="LABEL_LOAD_DEPT_DASH.Text = cursor1.GetString(\"";
mostCurrent._label_load_dept_dash.setText(BA.ObjectToCharSequence(_cursor1.GetString("Department")));
 //BA.debugLineNum = 161;BA.debugLine="LABEL_LOAD_POSITION_DASH.Text = cursor1.GetStri";
mostCurrent._label_load_position_dash.setText(BA.ObjectToCharSequence(_cursor1.GetString("Position")));
 }
};
 //BA.debugLineNum = 166;BA.debugLine="GET_LOGIN";
_get_login();
 };
 //BA.debugLineNum = 168;BA.debugLine="End Sub";
return "";
}
public static void  _log_expiration() throws Exception{
ResumableSub_LOG_EXPIRATION rsub = new ResumableSub_LOG_EXPIRATION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_EXPIRATION extends BA.ResumableSub {
public ResumableSub_LOG_EXPIRATION(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
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
 //BA.debugLineNum = 639;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Expiration Table'")));
 //BA.debugLineNum = 640;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor5.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 641;BA.debugLine="For i = 0 To cursor5.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor5.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 642;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 643;BA.debugLine="cursor5.Position = i";
parent._cursor5.setPosition(_i);
 //BA.debugLineNum = 644;BA.debugLine="LABEL_LAST_EXPIRATION.Text = \"Last Update: \" &";
parent.mostCurrent._label_last_expiration.setText(BA.ObjectToCharSequence("Last Update: "+parent._cursor5.GetString("date_time_updated")+" - "+parent._cursor5.GetString("updated_by")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 if (true) break;

case 9:
//C
this.state = 10;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 649;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _log_principal() throws Exception{
ResumableSub_LOG_PRINCIPAL rsub = new ResumableSub_LOG_PRINCIPAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_PRINCIPAL extends BA.ResumableSub {
public ResumableSub_LOG_PRINCIPAL(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
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
 //BA.debugLineNum = 627;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Principal Table'")));
 //BA.debugLineNum = 628;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 629;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor3.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 630;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 631;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 632;BA.debugLine="LABEL_LAST_PRINCIPAL.Text = \"Last Update: \" & c";
parent.mostCurrent._label_last_principal.setText(BA.ObjectToCharSequence("Last Update: "+parent._cursor3.GetString("date_time_updated")+" - "+parent._cursor3.GetString("updated_by")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 if (true) break;

case 9:
//C
this.state = 10;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 637;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _log_producttbl() throws Exception{
ResumableSub_LOG_PRODUCTTBL rsub = new ResumableSub_LOG_PRODUCTTBL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_PRODUCTTBL extends BA.ResumableSub {
public ResumableSub_LOG_PRODUCTTBL(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
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
 //BA.debugLineNum = 603;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Product Table'")));
 //BA.debugLineNum = 604;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 605;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 606;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 607;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 608;BA.debugLine="LABEL_LAST_PRODUCTTBL.Text = \"Last Update: \" &";
parent.mostCurrent._label_last_producttbl.setText(BA.ObjectToCharSequence("Last Update: "+parent._cursor1.GetString("date_time_updated")+" - "+parent._cursor1.GetString("updated_by")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 if (true) break;

case 9:
//C
this.state = 10;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 613;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _log_warehouse() throws Exception{
ResumableSub_LOG_WAREHOUSE rsub = new ResumableSub_LOG_WAREHOUSE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOG_WAREHOUSE extends BA.ResumableSub {
public ResumableSub_LOG_WAREHOUSE(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
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
 //BA.debugLineNum = 615;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Warehouse Table'")));
 //BA.debugLineNum = 616;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor2.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 617;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 618;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 619;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 620;BA.debugLine="LABEL_LAST_WAREHOUSE.Text = \"Last Update: \" & c";
parent.mostCurrent._label_last_warehouse.setText(BA.ObjectToCharSequence("Last Update: "+parent._cursor2.GetString("date_time_updated")+" - "+parent._cursor2.GetString("updated_by")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 if (true) break;

case 9:
//C
this.state = 10;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 625;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim connection As SQL";
_connection = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 13;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim cursor2 As Cursor";
_cursor2 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim cursor3 As Cursor";
_cursor3 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim cursor4 As Cursor";
_cursor4 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim cursor5 As Cursor";
_cursor5 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim cursor6 As Cursor";
_cursor6 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private logoutBitmap As Bitmap";
_logoutbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim TTS1 As TTS";
_tts1 = new anywheresoftware.b4a.obejcts.TTS();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 247;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 248;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 249;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 250;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 251;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 252;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 253;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 254;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 255;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 256;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 257;BA.debugLine="End Sub";
return "";
}
public static String  _tts1_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 589;BA.debugLine="Sub TTS1_Ready (Success As Boolean)";
 //BA.debugLineNum = 590;BA.debugLine="If Success Then";
if (_success) { 
 }else {
 };
 //BA.debugLineNum = 596;BA.debugLine="End Sub";
return "";
}
public static void  _update_principal() throws Exception{
ResumableSub_UPDATE_PRINCIPAL rsub = new ResumableSub_UPDATE_PRINCIPAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_PRINCIPAL extends BA.ResumableSub {
public ResumableSub_UPDATE_PRINCIPAL(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group15;
int index15;
int groupLen15;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 447;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 448;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 449;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_prin";
_cmd = _createcommand("select_principal",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 450;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 11;
return;
case 11:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 451;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 452;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 453;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 454;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 455;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 456;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM principal_t";
parent._connection.ExecNonQuery("DELETE FROM principal_table");
 //BA.debugLineNum = 457;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 4;
;
 //BA.debugLineNum = 458;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 459;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 461;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("73276815",BA.NumberToString(2),0);
 //BA.debugLineNum = 462;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 4:
//for
this.state = 7;
group15 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index15 = 0;
groupLen15 = group15.getSize();
this.state = 14;
if (true) break;

case 14:
//C
this.state = 7;
if (index15 < groupLen15) {
this.state = 6;
_row = (Object[])(group15.Get(index15));}
if (true) break;

case 15:
//C
this.state = 14;
index15++;
if (true) break;

case 6:
//C
this.state = 15;
 //BA.debugLineNum = 463;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO principal_";
parent._connection.ExecNonQuery("INSERT INTO principal_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_acronym"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("mai_acronym"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_name"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_address"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("contact_person"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_tin"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_logo"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("trade_discount"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("office_phone_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("fax_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("mobile_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("active_flag"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_modified"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("modified_flag"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("edit_number"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("user_info"))))])+"')");
 //BA.debugLineNum = 469;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 15;
;
 //BA.debugLineNum = 470;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Principal : \" &";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Principal : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_name"))))])));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 473;BA.debugLine="LABEL_MSGBOX2.Text = \"Data Updated Successfully\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data Updated Successfully"));
 //BA.debugLineNum = 474;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 17;
return;
case 17:
//C
this.state = 10;
;
 //BA.debugLineNum = 475;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 477;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 478;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 18;
return;
case 18:
//C
this.state = 10;
;
 //BA.debugLineNum = 479;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 480;BA.debugLine="Msgbox2Async(\"PRINCIPAL DATABASE IS NOT UPDATED.";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("PRINCIPAL DATABASE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 481;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 483;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 484;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _update_producttbl() throws Exception{
ResumableSub_UPDATE_PRODUCTTBL rsub = new ResumableSub_UPDATE_PRODUCTTBL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_PRODUCTTBL extends BA.ResumableSub {
public ResumableSub_UPDATE_PRODUCTTBL(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group16;
int index16;
int groupLen16;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 345;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 346;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 347;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_prod";
_cmd = _createcommand("select_product_table",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 348;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 11;
return;
case 11:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 349;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 350;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 351;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 4;
;
 //BA.debugLineNum = 352;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 353;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 354;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 355;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM product_tab";
parent._connection.ExecNonQuery("DELETE FROM product_table");
 //BA.debugLineNum = 356;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 4;
;
 //BA.debugLineNum = 357;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 358;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 14;
return;
case 14:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 359;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("73145743",BA.NumberToString(2),0);
 //BA.debugLineNum = 360;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 4:
//for
this.state = 7;
group16 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index16 = 0;
groupLen16 = group16.getSize();
this.state = 15;
if (true) break;

case 15:
//C
this.state = 7;
if (index16 < groupLen16) {
this.state = 6;
_row = (Object[])(group16.Get(index16));}
if (true) break;

case 16:
//C
this.state = 15;
index16++;
if (true) break;

case 6:
//C
this.state = 16;
 //BA.debugLineNum = 361;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO product_ta";
parent._connection.ExecNonQuery("INSERT INTO product_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_category"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("brand_img"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_brand"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_desc_img"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_desc"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("expiration_date"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("case_bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("box_bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("bag_bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("pack_bar_code"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("weight"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit_weight"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("metric_tons"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("flag_deleted"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("prod_status"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("promo_product"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("life_span_year"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("life_span_month"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("default_expiration_date_reading"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("inner_piece"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("category_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("datetime_modified"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("total_audit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("length_volume"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("height_volume"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("width_volume"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("total_volume"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("effective_price_date"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_ACQUISITION"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_SRP"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_COMPANY"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_EXTRUCK"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_BOOKING"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_UNIT_PER_PCS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_CP_GS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_BAR"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_FOTS"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_PRINCE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_MARQUEE"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PACK_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("CASE_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("PCS_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("DOZ_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BOX_RCS_CHAIN"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("BAG_RCS_CHAIN"))))])+"')");
 //BA.debugLineNum = 389;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 17;
return;
case 17:
//C
this.state = 16;
;
 //BA.debugLineNum = 390;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Product : \" & ro";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Product : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_desc"))))])));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 392;BA.debugLine="LABEL_MSGBOX2.Text = \"Data Updated Successfully\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data Updated Successfully"));
 //BA.debugLineNum = 393;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 18;
return;
case 18:
//C
this.state = 10;
;
 //BA.debugLineNum = 394;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 395;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 10;
;
 //BA.debugLineNum = 396;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM database_up";
parent._connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Product Table'");
 //BA.debugLineNum = 397;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 10;
;
 //BA.debugLineNum = 398;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO database_up";
parent._connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Product Table', '"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"', '"+parent.mostCurrent._login_module._username /*String*/ +"')");
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 400;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 401;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 21;
return;
case 21:
//C
this.state = 10;
;
 //BA.debugLineNum = 402;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 403;BA.debugLine="Msgbox2Async(\"PRODUCT TABLE IS NOT UPDATED.\" & C";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("PRODUCT TABLE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 404;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 406;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 407;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _update_warehouse() throws Exception{
ResumableSub_UPDATE_WAREHOUSE rsub = new ResumableSub_UPDATE_WAREHOUSE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_WAREHOUSE extends BA.ResumableSub {
public ResumableSub_UPDATE_WAREHOUSE(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
int _gr = 0;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group16;
int index16;
int groupLen16;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 409;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 410;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 411;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_ware";
_cmd = _createcommand("select_warehouse",(Object[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 412;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 11;
return;
case 11:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 413;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 414;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 415;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 416;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 417;BA.debugLine="LABEL_MSGBOX2.Text = \"Reading data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Reading data..."));
 //BA.debugLineNum = 418;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM warehouse_t";
parent._connection.ExecNonQuery("DELETE FROM warehouse_table");
 //BA.debugLineNum = 419;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 4;
;
 //BA.debugLineNum = 420;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 421;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 13;
return;
case 13:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 423;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("73211279",BA.NumberToString(2),0);
 //BA.debugLineNum = 425;BA.debugLine="Dim gr As Int = 0";
_gr = (int) (0);
 //BA.debugLineNum = 426;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 4:
//for
this.state = 7;
group16 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index16 = 0;
groupLen16 = group16.getSize();
this.state = 14;
if (true) break;

case 14:
//C
this.state = 7;
if (index16 < groupLen16) {
this.state = 6;
_row = (Object[])(group16.Get(index16));}
if (true) break;

case 15:
//C
this.state = 14;
index16++;
if (true) break;

case 6:
//C
this.state = 15;
 //BA.debugLineNum = 427;BA.debugLine="gr = gr + 1";
_gr = (int) (_gr+1);
 //BA.debugLineNum = 428;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO warehouse_";
parent._connection.ExecNonQuery("INSERT INTO warehouse_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("warehouse_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("warehouse_name"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("assigned_modules"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_modified"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("modified_flag"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("edit_number"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("user_info"))))])+"')");
 //BA.debugLineNum = 431;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating Warehouse : \" &";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating Warehouse : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("warehouse_name"))))])));
 //BA.debugLineNum = 432;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 15;
;
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 434;BA.debugLine="LABEL_MSGBOX2.Text = \"Data Updated Successfully\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data Updated Successfully"));
 //BA.debugLineNum = 435;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 17;
return;
case 17:
//C
this.state = 10;
;
 //BA.debugLineNum = 436;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 438;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 439;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 18;
return;
case 18:
//C
this.state = 10;
;
 //BA.debugLineNum = 440;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 441;BA.debugLine="Msgbox2Async(\"WAREHOUSE DATABASE IS NOT UPDATED.";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("WAREHOUSE DATABASE IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 442;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 444;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 445;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 281;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 282;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 283;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 284;BA.debugLine="End Sub";
return "";
}
public static void  _verify_producttbl() throws Exception{
ResumableSub_VERIFY_PRODUCTTBL rsub = new ResumableSub_VERIFY_PRODUCTTBL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_VERIFY_PRODUCTTBL extends BA.ResumableSub {
public ResumableSub_VERIFY_PRODUCTTBL(wingan.app.dashboard_module parent) {
this.parent = parent;
}
wingan.app.dashboard_module parent;
int _i = 0;
int _result = 0;
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
 //BA.debugLineNum = 574;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM dat";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM database_update_table WHERE database = 'Product Table' AND date_time_updated LIKE '%"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"%'")));
 //BA.debugLineNum = 575;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor5.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 576;BA.debugLine="For i = 0 To cursor5.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step3 = 1;
limit3 = (int) (parent._cursor5.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 577;BA.debugLine="cursor5.Position = i";
parent._cursor5.setPosition(_i);
 //BA.debugLineNum = 578;BA.debugLine="Log(cursor5.GetString(\"date_time_updated\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("73473413",parent._cursor5.GetString("date_time_updated"),0);
 //BA.debugLineNum = 579;BA.debugLine="Log(DateTime.Date(DateTime.Now))";
anywheresoftware.b4a.keywords.Common.LogImpl("73473414",anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),0);
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 582;BA.debugLine="Log(DateTime.Date(DateTime.Now))";
anywheresoftware.b4a.keywords.Common.LogImpl("73473417",anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),0);
 //BA.debugLineNum = 583;BA.debugLine="Msgbox2Async(\"Hi, Good Day User! We would like t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Hi, Good Day User! We would like to inform you that we will having an update of product table daily for the system data to remove any possible conflict. Press OK to proceed."),BA.ObjectToCharSequence("Notice"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 584;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 10;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 585;BA.debugLine="UPDATE_PRODUCTTBL";
_update_producttbl();
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 587;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
	public boolean _onCreateOptionsMenu(android.view.Menu menu) {
		if (processBA.subExists("activity_createmenu")) {
			processBA.raiseEvent2(null, true, "activity_createmenu", false, new de.amberhome.objects.appcompat.ACMenuWrapper(menu));
			return true;
		}
		else
			return false;
	}
}
