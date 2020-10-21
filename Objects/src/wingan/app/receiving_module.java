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

public class receiving_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static receiving_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.receiving_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (receiving_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.receiving_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.receiving_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (receiving_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (receiving_module) Resume **");
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
		return receiving_module.class;
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
            BA.LogInfo("** Activity (receiving_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (receiving_module) Pause event (activity is not paused). **");
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
            receiving_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (receiving_module) Resume **");
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
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor7 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor8 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor9 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor10 = null;
public static String _purchase_order_no = "";
public static String _transaction_type = "";
public static String _principal_acronym = "";
public static String _principal_id = "";
public static String _principal_name = "";
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _plusbitmap = null;
public static int _re_po = 0;
public anywheresoftware.b4a.objects.IME _ctrl = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable _table_receiving = null;
public wingan.app.b4xcombobox _cmb_principal = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_po = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_po = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.login_module _login_module = null;
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
public ResumableSub_Activity_Create(wingan.app.receiving_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.receiving_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 72;BA.debugLine="Activity.LoadLayout(\"receiving\")";
parent.mostCurrent._activity.LoadLayout("receiving",mostCurrent.activityBA);
 //BA.debugLineNum = 74;BA.debugLine="plusBitmap = LoadBitmap(File.DirAssets, \"add.png\"";
parent._plusbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"add.png");
 //BA.debugLineNum = 76;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 77;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 78;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 79;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 80;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 81;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 82;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 84;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 85;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 86;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 88;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 89;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 92;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 93;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 94;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 96;BA.debugLine="LOAD_PURCHASE_HEADER";
_load_purchase_header();
 //BA.debugLineNum = 97;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 98;BA.debugLine="POPULATE_PRINCIPAL";
_populate_principal();
 //BA.debugLineNum = 99;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 100;BA.debugLine="GET_PRINCIPAL_ID";
_get_principal_id();
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;
 //BA.debugLineNum = 103;BA.debugLine="Sub Activity_CreateMenu(Menu As ACMenu)";
 //BA.debugLineNum = 104;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("create"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 105;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 106;BA.debugLine="UpdateIcon(\"create\", plusBitmap)";
_updateicon("create",_plusbitmap);
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 541;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 542;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 543;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 545;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 114;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 116;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(wingan.app.receiving_module parent) {
this.parent = parent;
}
wingan.app.receiving_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 110;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 111;BA.debugLine="LOAD_PURCHASE_ORDER";
_load_purchase_order();
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.receiving_module parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.receiving_module parent;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item;
int _result = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _row = 0;
int step21;
int limit21;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 164;BA.debugLine="If Item.Title = \"create\" Then";
if (true) break;

case 1:
//if
this.state = 26;
if ((_item.getTitle()).equals("create")) { 
this.state = 3;
}else {
this.state = 25;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 165;BA.debugLine="Msgbox2Async(\"Please choose a transaction type\",";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please choose a transaction type"),BA.ObjectToCharSequence("Entry"),"Purchase Order","","Auto Shipping",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 166;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 27;
return;
case 27:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 167;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 4:
//if
this.state = 23;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 6;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 23;
 //BA.debugLineNum = 168;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 169;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.RGB(2";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)));
 //BA.debugLineNum = 170;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 28;
return;
case 28:
//C
this.state = 23;
;
 //BA.debugLineNum = 171;BA.debugLine="EDITTEXT_PO.Background = bg";
parent.mostCurrent._edittext_po.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 172;BA.debugLine="PANEL_BG_PO.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_po.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 173;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 29;
return;
case 29:
//C
this.state = 23;
;
 //BA.debugLineNum = 174;BA.debugLine="EDITTEXT_PO.RequestFocus";
parent.mostCurrent._edittext_po.RequestFocus();
 //BA.debugLineNum = 175;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 30;
return;
case 30:
//C
this.state = 23;
;
 //BA.debugLineNum = 176;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_PO)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_po.getObject()));
 //BA.debugLineNum = 177;BA.debugLine="transaction_type = \"PURCHASE ORDER\"";
parent._transaction_type = "PURCHASE ORDER";
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 179;BA.debugLine="Msgbox2Async(\"Are you sure you want to make an";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to make an AUTO SHIP transaction for this principal?"),BA.ObjectToCharSequence("Auto Shipping"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 180;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 31;
return;
case 31:
//C
this.state = 9;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 181;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 9:
//if
this.state = 22;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 182;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM r";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_ref_table WHERE po_doc_no = '"+parent._principal_acronym+"-AUTO-"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()).toUpperCase()+"'")));
 //BA.debugLineNum = 183;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 12:
//if
this.state = 21;
if (parent._cursor1.getRowCount()>0) { 
this.state = 14;
}else {
this.state = 20;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 184;BA.debugLine="For row = 0 To cursor1.RowCount - 1";
if (true) break;

case 15:
//for
this.state = 18;
step21 = 1;
limit21 = (int) (parent._cursor1.getRowCount()-1);
_row = (int) (0) ;
this.state = 32;
if (true) break;

case 32:
//C
this.state = 18;
if ((step21 > 0 && _row <= limit21) || (step21 < 0 && _row >= limit21)) this.state = 17;
if (true) break;

case 33:
//C
this.state = 32;
_row = ((int)(0 + _row + step21)) ;
if (true) break;

case 17:
//C
this.state = 33;
 //BA.debugLineNum = 185;BA.debugLine="cursor1.Position = row";
parent._cursor1.setPosition(_row);
 if (true) break;
if (true) break;

case 18:
//C
this.state = 21;
;
 //BA.debugLineNum = 187;BA.debugLine="Msgbox2Async(\"This principal have existing AU";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This principal have existing AUTO SHIP for today, You cannot create more than one AUTO SHIP in a day, Please search it in the table"),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 189;BA.debugLine="transaction_type = \"AUTO SHIP\"";
parent._transaction_type = "AUTO SHIP";
 //BA.debugLineNum = 190;BA.debugLine="purchase_order_no = principal_acronym&\"-AUTO\"";
parent._purchase_order_no = parent._principal_acronym+"-AUTO"+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))+parent.mostCurrent._login_module._tab_id /*String*/ ;
 //BA.debugLineNum = 191;BA.debugLine="principal_name = CMB_PRINCIPAL.cmbBox.Selecte";
parent._principal_name = parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 192;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 34;
return;
case 34:
//C
this.state = 21;
;
 //BA.debugLineNum = 193;BA.debugLine="VALIDATE_PO_STATUS";
_validate_po_status();
 if (true) break;

case 21:
//C
this.state = 22;
;
 if (true) break;

case 22:
//C
this.state = 23;
;
 if (true) break;

case 23:
//C
this.state = 26;
;
 if (true) break;

case 25:
//C
this.state = 26;
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 200;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 145;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 146;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 147;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 148;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 140;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 141;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 142;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 143;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return null;
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 523;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 524;BA.debugLine="EDITTEXT_PO.Text = \"\"";
mostCurrent._edittext_po.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 525;BA.debugLine="PANEL_BG_PO.SetVisibleAnimated(300, False)";
mostCurrent._panel_bg_po.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 526;BA.debugLine="End Sub";
return "";
}
public static void  _button_load_click() throws Exception{
ResumableSub_BUTTON_LOAD_Click rsub = new ResumableSub_BUTTON_LOAD_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_LOAD_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_LOAD_Click(wingan.app.receiving_module parent) {
this.parent = parent;
}
wingan.app.receiving_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 528;BA.debugLine="purchase_order_no = EDITTEXT_PO.Text.ToUpperCase";
parent._purchase_order_no = parent.mostCurrent._edittext_po.getText().toUpperCase();
 //BA.debugLineNum = 529;BA.debugLine="principal_name = CMB_PRINCIPAL.cmbBox.SelectedIte";
parent._principal_name = parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 530;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 531;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 532;BA.debugLine="VALIDATE_PO_STATUS";
_validate_po_status();
 //BA.debugLineNum = 533;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_principal_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged rsub = new ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_PRINCIPAL_SelectedIndexChanged(wingan.app.receiving_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.receiving_module parent;
int _index;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 536;BA.debugLine="GET_PRINCIPAL_ID";
_get_principal_id();
 //BA.debugLineNum = 537;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 538;BA.debugLine="LOAD_PURCHASE_ORDER";
_load_purchase_order();
 //BA.debugLineNum = 539;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 365;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 366;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 367;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 368;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 369;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 370;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 371;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 360;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 361;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 362;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,receiving_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 363;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 364;BA.debugLine="End Sub";
return null;
}
public static void  _get_principal_id() throws Exception{
ResumableSub_GET_PRINCIPAL_ID rsub = new ResumableSub_GET_PRINCIPAL_ID(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_PRINCIPAL_ID extends BA.ResumableSub {
public ResumableSub_GET_PRINCIPAL_ID(wingan.app.receiving_module parent) {
this.parent = parent;
}
wingan.app.receiving_module parent;
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
 //BA.debugLineNum = 214;BA.debugLine="If CMB_PRINCIPAL.SelectedIndex <= -1 Then CMB_PRI";
if (true) break;

case 1:
//if
this.state = 6;
if (parent.mostCurrent._cmb_principal._getselectedindex /*int*/ ()<=-1) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
parent.mostCurrent._cmb_principal._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 215;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pri";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM principal_table WHERE principal_name = '"+parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"'")));
 //BA.debugLineNum = 216;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step3 = 1;
limit3 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 10;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 9;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 217;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 218;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 219;BA.debugLine="principal_id = cursor2.GetString(\"principal_id\")";
parent._principal_id = parent._cursor2.GetString("principal_id");
 //BA.debugLineNum = 220;BA.debugLine="principal_acronym = cursor2.GetString(\"principal";
parent._principal_acronym = parent._cursor2.GetString("principal_acronym");
 if (true) break;
if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 222;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 223;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 154;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 155;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 156;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 157;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 158;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 161;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 162;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 39;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 41;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 42;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 43;BA.debugLine="Private NameColumn(5) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (5)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 45;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 46;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 49;BA.debugLine="Private TABLE_RECEIVING As B4XTable";
mostCurrent._table_receiving = new wingan.app.b4xtable();
 //BA.debugLineNum = 50;BA.debugLine="Private CMB_PRINCIPAL As B4XComboBox";
mostCurrent._cmb_principal = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 51;BA.debugLine="Private PANEL_BG_PO As Panel";
mostCurrent._panel_bg_po = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private EDITTEXT_PO As EditText";
mostCurrent._edittext_po = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static void  _insert_purchase_order_ref() throws Exception{
ResumableSub_INSERT_PURCHASE_ORDER_REF rsub = new ResumableSub_INSERT_PURCHASE_ORDER_REF(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_PURCHASE_ORDER_REF extends BA.ResumableSub {
public ResumableSub_INSERT_PURCHASE_ORDER_REF(wingan.app.receiving_module parent) {
this.parent = parent;
}
wingan.app.receiving_module parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
String _query = "";
String _query1 = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 498;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_rece";
_cmd = _createcommand("insert_receiving_ref",(Object[])(new String[]{parent._purchase_order_no,parent._principal_id,parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,"-","-","RECEIVING",parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 500;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 501;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 502;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 503;BA.debugLine="Dim query As String = \"DELETE FROM receiving_ref";
_query = "DELETE FROM receiving_ref_table WHERE po_doc_no = ?";
 //BA.debugLineNum = 504;BA.debugLine="connection.ExecNonQuery2(query,Array As String(p";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._purchase_order_no}));
 //BA.debugLineNum = 505;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 506;BA.debugLine="Dim query1 As String = \"INSERT INTO receiving_re";
_query1 = "INSERT INTO receiving_ref_table VALUES (?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 507;BA.debugLine="connection.ExecNonQuery2(query1,Array As String(";
parent._connection.ExecNonQuery2(_query1,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._purchase_order_no,parent._principal_id,parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._username /*String*/ ,"-","-","RECEIVING",parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 509;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 6;
;
 //BA.debugLineNum = 510;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 511;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 10;
return;
case 10:
//C
this.state = 6;
;
 //BA.debugLineNum = 512;BA.debugLine="LOAD_PURCHASE_ORDER";
_load_purchase_order();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 514;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 515;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 516;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 517;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 11;
return;
case 11:
//C
this.state = 6;
;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 519;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 520;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _js) throws Exception{
}
public static String  _load_purchase_header() throws Exception{
 //BA.debugLineNum = 225;BA.debugLine="Sub LOAD_PURCHASE_HEADER";
 //BA.debugLineNum = 226;BA.debugLine="NameColumn(0)=TABLE_RECEIVING.AddColumn(\"Purchase";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_receiving._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Purchase Order",mostCurrent._table_receiving._column_type_text /*int*/ );
 //BA.debugLineNum = 227;BA.debugLine="NameColumn(1)=TABLE_RECEIVING.AddColumn(\"Receivin";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_receiving._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Receiving Date Time",mostCurrent._table_receiving._column_type_text /*int*/ );
 //BA.debugLineNum = 228;BA.debugLine="NameColumn(2)=TABLE_RECEIVING.AddColumn(\"Received";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_receiving._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Received Date Time",mostCurrent._table_receiving._column_type_text /*int*/ );
 //BA.debugLineNum = 229;BA.debugLine="NameColumn(3)=TABLE_RECEIVING.AddColumn(\"Status\",";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._table_receiving._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Status",mostCurrent._table_receiving._column_type_text /*int*/ );
 //BA.debugLineNum = 230;BA.debugLine="NameColumn(4)=TABLE_RECEIVING.AddColumn(\"Receivin";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._table_receiving._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Receiving Checker",mostCurrent._table_receiving._column_type_text /*int*/ );
 //BA.debugLineNum = 231;BA.debugLine="End Sub";
return "";
}
public static void  _load_purchase_order() throws Exception{
ResumableSub_LOAD_PURCHASE_ORDER rsub = new ResumableSub_LOAD_PURCHASE_ORDER(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_PURCHASE_ORDER extends BA.ResumableSub {
public ResumableSub_LOAD_PURCHASE_ORDER(wingan.app.receiving_module parent) {
this.parent = parent;
}
wingan.app.receiving_module parent;
anywheresoftware.b4a.objects.collections.List _data = null;
int _ia = 0;
Object[] _row = null;
int step8;
int limit8;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 233;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 1;
;
 //BA.debugLineNum = 234;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 235;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 1;
;
 //BA.debugLineNum = 236;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 237;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 238;BA.debugLine="cursor10 = connection.ExecQuery(\"SELECT * FROM re";
parent._cursor10 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM receiving_ref_table WHERE principal_id = '"+parent._principal_id+"'")));
 //BA.debugLineNum = 239;BA.debugLine="If cursor10.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor10.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 240;BA.debugLine="For ia = 0 To cursor10.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step8 = 1;
limit8 = (int) (parent._cursor10.getRowCount()-1);
_ia = (int) (0) ;
this.state = 17;
if (true) break;

case 17:
//C
this.state = 7;
if ((step8 > 0 && _ia <= limit8) || (step8 < 0 && _ia >= limit8)) this.state = 6;
if (true) break;

case 18:
//C
this.state = 17;
_ia = ((int)(0 + _ia + step8)) ;
if (true) break;

case 6:
//C
this.state = 18;
 //BA.debugLineNum = 241;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 19;
return;
case 19:
//C
this.state = 18;
;
 //BA.debugLineNum = 242;BA.debugLine="cursor10.Position = ia";
parent._cursor10.setPosition(_ia);
 //BA.debugLineNum = 243;BA.debugLine="Dim row(5) As Object";
_row = new Object[(int) (5)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 244;BA.debugLine="row(0) = cursor10.GetString(\"po_doc_no\")";
_row[(int) (0)] = (Object)(parent._cursor10.GetString("po_doc_no"));
 //BA.debugLineNum = 245;BA.debugLine="row(1) = cursor10.GetString(\"receiving_date\")";
_row[(int) (1)] = (Object)(parent._cursor10.GetString("receiving_date")+" "+parent._cursor10.GetString("receiving_time"));
 //BA.debugLineNum = 246;BA.debugLine="row(2) = cursor10.GetString(\"received_date\") &";
_row[(int) (2)] = (Object)(parent._cursor10.GetString("received_date")+" "+parent._cursor10.GetString("received_time"));
 //BA.debugLineNum = 247;BA.debugLine="row(3) = cursor10.GetString(\"po_status\")";
_row[(int) (3)] = (Object)(parent._cursor10.GetString("po_status"));
 //BA.debugLineNum = 248;BA.debugLine="row(4) = cursor10.GetString(\"receiving_by\")";
_row[(int) (4)] = (Object)(parent._cursor10.GetString("receiving_by"));
 //BA.debugLineNum = 249;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 251;BA.debugLine="TABLE_RECEIVING.RowHeight = 50dip";
parent.mostCurrent._table_receiving._rowheight /*int*/  = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));
 //BA.debugLineNum = 252;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 20;
return;
case 20:
//C
this.state = 10;
;
 //BA.debugLineNum = 253;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 255;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 256;BA.debugLine="ToastMessageShow(\"Data is empty\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Data is empty"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 258;BA.debugLine="TABLE_RECEIVING.SetData(Data)";
parent.mostCurrent._table_receiving._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 259;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 11:
//if
this.state = 14;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 13;
}if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 260;BA.debugLine="XSelections.Initialize(TABLE_RECEIVING)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_receiving);
 //BA.debugLineNum = 261;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 263;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = -1;
;
 //BA.debugLineNum = 264;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_purchase_order_no() throws Exception{
ResumableSub_LOAD_PURCHASE_ORDER_NO rsub = new ResumableSub_LOAD_PURCHASE_ORDER_NO(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_PURCHASE_ORDER_NO extends BA.ResumableSub {
public ResumableSub_LOAD_PURCHASE_ORDER_NO(wingan.app.receiving_module parent) {
this.parent = parent;
}
wingan.app.receiving_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
String _get1 = "";
String _prin_id = "";
int _count = 0;
Object[] _row = null;
int _i = 0;
anywheresoftware.b4a.BA.IterableList group14;
int index14;
int groupLen14;
int step39;
int limit39;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 434;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 435;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_po_n";
_cmd = _createcommand("select_po_no",(Object[])(new String[]{parent._purchase_order_no}));
 //BA.debugLineNum = 436;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 42;
return;
case 42:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 437;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 41;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 40;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 438;BA.debugLine="LABEL_MSGBOX2.Text = \"Getting Order...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Getting Order..."));
 //BA.debugLineNum = 439;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 43;
return;
case 43:
//C
this.state = 4;
;
 //BA.debugLineNum = 440;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 441;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 44;
return;
case 44:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 443;BA.debugLine="Log(2)";
anywheresoftware.b4a.keywords.Common.LogImpl("786835210",BA.NumberToString(2),0);
 //BA.debugLineNum = 444;BA.debugLine="Dim get1 As String = 0";
_get1 = BA.NumberToString(0);
 //BA.debugLineNum = 445;BA.debugLine="Dim prin_id As String";
_prin_id = "";
 //BA.debugLineNum = 446;BA.debugLine="Dim count As Int = 0";
_count = (int) (0);
 //BA.debugLineNum = 447;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 38;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 37;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 448;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 20;
group14 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index14 = 0;
groupLen14 = group14.getSize();
this.state = 45;
if (true) break;

case 45:
//C
this.state = 20;
if (index14 < groupLen14) {
this.state = 9;
_row = (Object[])(group14.Get(index14));}
if (true) break;

case 46:
//C
this.state = 45;
index14++;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 449;BA.debugLine="If principal_id = row(res.Columns.Get(\"princip";
if (true) break;

case 10:
//if
this.state = 19;
if ((parent._principal_id).equals(BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))]))) { 
this.state = 12;
}else {
this.state = 18;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 450;BA.debugLine="If count = 0 Then";
if (true) break;

case 13:
//if
this.state = 16;
if (_count==0) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 451;BA.debugLine="connection.ExecNonQuery(\"DELETE FROM purchas";
parent._connection.ExecNonQuery("DELETE FROM purchase_order_ref_table WHERE po_doc_no = '"+parent._purchase_order_no+"'");
 //BA.debugLineNum = 452;BA.debugLine="count = count + 1";
_count = (int) (_count+1);
 if (true) break;

case 16:
//C
this.state = 19;
;
 //BA.debugLineNum = 454;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO purchase";
parent._connection.ExecNonQuery("INSERT INTO purchase_order_ref_table VALUES ('"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("po_doc_no"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("quantity"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("unit_price"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("po_amount"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("acqui_amount"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("total_pieces"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_registered"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("date_time_modified"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("modified_flag"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("edit_number"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("user_info"))))])+"')");
 //BA.debugLineNum = 458;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 47;
return;
case 47:
//C
this.state = 19;
;
 //BA.debugLineNum = 459;BA.debugLine="LABEL_MSGBOX2.Text = \"Getting purchase order";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Getting purchase order : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])));
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 461;BA.debugLine="get1 = 1";
_get1 = BA.NumberToString(1);
 //BA.debugLineNum = 462;BA.debugLine="prin_id = row(res.Columns.Get(\"principal_id\")";
_prin_id = BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))]);
 if (true) break;

case 19:
//C
this.state = 46;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 465;BA.debugLine="If get1 = 0 Then";

case 20:
//if
this.state = 35;
if ((_get1).equals(BA.NumberToString(0))) { 
this.state = 22;
}else {
this.state = 30;
}if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 466;BA.debugLine="If re_po = 1 Then";
if (true) break;

case 23:
//if
this.state = 28;
if (parent._re_po==1) { 
this.state = 25;
}else {
this.state = 27;
}if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 467;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 48;
return;
case 48:
//C
this.state = 28;
;
 //BA.debugLineNum = 468;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 469;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 49;
return;
case 49:
//C
this.state = 28;
;
 //BA.debugLineNum = 470;BA.debugLine="LOAD_PURCHASE_ORDER";
_load_purchase_order();
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 472;BA.debugLine="INSERT_PURCHASE_ORDER_REF";
_insert_purchase_order_ref();
 if (true) break;

case 28:
//C
this.state = 35;
;
 if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 475;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT princip";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id = '"+_prin_id+"'")));
 //BA.debugLineNum = 476;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
if (true) break;

case 31:
//for
this.state = 34;
step39 = 1;
limit39 = (int) (parent._cursor3.getRowCount()-1);
_i = (int) (0) ;
this.state = 50;
if (true) break;

case 50:
//C
this.state = 34;
if ((step39 > 0 && _i <= limit39) || (step39 < 0 && _i >= limit39)) this.state = 33;
if (true) break;

case 51:
//C
this.state = 50;
_i = ((int)(0 + _i + step39)) ;
if (true) break;

case 33:
//C
this.state = 51;
 //BA.debugLineNum = 477;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 52;
return;
case 52:
//C
this.state = 51;
;
 //BA.debugLineNum = 478;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 479;BA.debugLine="Msgbox2Async(\"This purchase order is belongs";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This purchase order is belongs to principal : "+parent._cursor3.GetString("principal_name")),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 480;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;
if (true) break;

case 34:
//C
this.state = 35;
;
 if (true) break;

case 35:
//C
this.state = 38;
;
 if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 484;BA.debugLine="Msgbox2Async(\"The purchase order you input is N";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The purchase order you input is NOT EXISTING IN THE SYSTEM"),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 486;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 38:
//C
this.state = 41;
;
 if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 489;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 490;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 53;
return;
case 53:
//C
this.state = 41;
;
 //BA.debugLineNum = 491;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 492;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 493;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 41:
//C
this.state = -1;
;
 //BA.debugLineNum = 495;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 496;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static String  _openlabel(anywheresoftware.b4a.objects.LabelWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 123;BA.debugLine="Sub OpenLabel(se As Label)";
 //BA.debugLineNum = 124;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 125;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 126;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 118;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 119;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 120;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 121;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 122;BA.debugLine="End Sub";
return "";
}
public static void  _populate_principal() throws Exception{
ResumableSub_POPULATE_PRINCIPAL rsub = new ResumableSub_POPULATE_PRINCIPAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_POPULATE_PRINCIPAL extends BA.ResumableSub {
public ResumableSub_POPULATE_PRINCIPAL(wingan.app.receiving_module parent) {
this.parent = parent;
}
wingan.app.receiving_module parent;
int _i = 0;
int step5;
int limit5;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 203;BA.debugLine="CMB_PRINCIPAL.cmbBox.Clear";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 204;BA.debugLine="CMB_PRINCIPAL.cmbBox.DropdownTextColor = Colors.B";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 205;BA.debugLine="CMB_PRINCIPAL.cmbBox.TextColor = Colors.White";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 206;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT principal_";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table ORDER BY principal_name ASC")));
 //BA.debugLineNum = 207;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step5 = 1;
limit5 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_i = ((int)(0 + _i + step5)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 208;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 209;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 210;BA.debugLine="CMB_PRINCIPAL.cmbBox.Add(cursor1.GetString(\"prin";
parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor1.GetString("principal_name"));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 212;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 19;BA.debugLine="Dim cursor7 As Cursor";
_cursor7 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim cursor8 As Cursor";
_cursor8 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim cursor9 As Cursor";
_cursor9 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim cursor10 As Cursor";
_cursor10 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Public purchase_order_no As String";
_purchase_order_no = "";
 //BA.debugLineNum = 25;BA.debugLine="Public transaction_type As String";
_transaction_type = "";
 //BA.debugLineNum = 26;BA.debugLine="Dim principal_acronym As String";
_principal_acronym = "";
 //BA.debugLineNum = 27;BA.debugLine="Dim principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 28;BA.debugLine="Dim principal_name As String";
_principal_name = "";
 //BA.debugLineNum = 30;BA.debugLine="Private plusBitmap As Bitmap";
_plusbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim re_po As Int = 0";
_re_po = (int) (0);
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 129;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 130;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 131;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 132;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 133;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 134;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 135;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 136;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 137;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 138;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static void  _table_receiving_cellclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_RECEIVING_CellClicked rsub = new ResumableSub_TABLE_RECEIVING_CellClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_RECEIVING_CellClicked extends BA.ResumableSub {
public ResumableSub_TABLE_RECEIVING_CellClicked(wingan.app.receiving_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.receiving_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
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
 //BA.debugLineNum = 341;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 342;BA.debugLine="Dim RowData As Map = TABLE_RECEIVING.GetRow(RowId";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_receiving._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 344;BA.debugLine="Msgbox2Async(\"Press OK to proceed to this purchas";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Press OK to proceed to this purchase order."),BA.ObjectToCharSequence("Purchase Order"),"OK","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 345;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 346;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 347;BA.debugLine="StartActivity(RECEIVING2_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._receiving2_module.getObject()));
 //BA.debugLineNum = 348;BA.debugLine="purchase_order_no = RowData.Get(\"Purchase Order\"";
parent._purchase_order_no = BA.ObjectToString(_rowdata.Get((Object)("Purchase Order")));
 //BA.debugLineNum = 349;BA.debugLine="principal_name = CMB_PRINCIPAL.cmbBox.SelectedIt";
parent._principal_name = parent.mostCurrent._cmb_principal._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 351;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_receiving_celllongclicked(String _columnid,long _rowid) throws Exception{
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
 //BA.debugLineNum = 352;BA.debugLine="Sub TABLE_RECEIVING_CellLongClicked (ColumnId As S";
 //BA.debugLineNum = 353;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 355;BA.debugLine="Dim RowData As Map = TABLE_RECEIVING.GetRow(RowId";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = mostCurrent._table_receiving._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 356;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 357;BA.debugLine="End Sub";
return "";
}
public static String  _table_receiving_dataupdated() throws Exception{
boolean _shouldrefresh = false;
wingan.app.b4xtable._b4xtablecolumn _column = null;
int _maxwidth = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
long _rowid = 0L;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl1 = null;
anywheresoftware.b4a.objects.collections.Map _row = null;
int _clr = 0;
String _othercolumnvalue = "";
 //BA.debugLineNum = 265;BA.debugLine="Sub TABLE_RECEIVING_DataUpdated";
 //BA.debugLineNum = 266;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 267;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 269;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 271;BA.debugLine="For i = 0 To TABLE_RECEIVING.VisibleRowIds.Size";
{
final int step4 = 1;
final int limit4 = mostCurrent._table_receiving._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 272;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 273;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 274;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 279;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 280;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 281;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 286;BA.debugLine="TABLE_RECEIVING.NumberOfFrozenColumns = 1";
mostCurrent._table_receiving._numberoffrozencolumns /*int*/  = (int) (1);
 //BA.debugLineNum = 316;BA.debugLine="For i = 0 To TABLE_RECEIVING.VisibleRowIds.Size -";
{
final int step15 = 1;
final int limit15 = (int) (mostCurrent._table_receiving._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit15 ;_i = _i + step15 ) {
 //BA.debugLineNum = 317;BA.debugLine="Dim RowId As Long = TABLE_RECEIVING.VisibleRowId";
_rowid = BA.ObjectToLongNumber(mostCurrent._table_receiving._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 318;BA.debugLine="If RowId > 0 Then";
if (_rowid>0) { 
 //BA.debugLineNum = 319;BA.debugLine="Dim pnl1 As B4XView = NameColumn(3).CellsLayout";
_pnl1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._namecolumn[(int) (3)].CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 320;BA.debugLine="Dim row As Map = TABLE_RECEIVING.GetRow(RowId)";
_row = new anywheresoftware.b4a.objects.collections.Map();
_row = mostCurrent._table_receiving._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 321;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 322;BA.debugLine="Dim OtherColumnValue As String = row.Get(NameCo";
_othercolumnvalue = BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (3)].Id /*String*/ )));
 //BA.debugLineNum = 323;BA.debugLine="If OtherColumnValue = \"RECEIVING\" Then";
if ((_othercolumnvalue).equals("RECEIVING")) { 
 //BA.debugLineNum = 324;BA.debugLine="clr = xui.Color_Red";
_clr = mostCurrent._xui.Color_Red;
 }else if((_othercolumnvalue).equals("RECEIVED")) { 
 //BA.debugLineNum = 326;BA.debugLine="clr = xui.Color_ARGB(255,255,157,0)";
_clr = mostCurrent._xui.Color_ARGB((int) (255),(int) (255),(int) (157),(int) (0));
 };
 //BA.debugLineNum = 328;BA.debugLine="pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Co";
_pnl1.GetView((int) (0)).SetColorAndBorder(_clr,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (215),(int) (215),(int) (215)),(int) (0));
 };
 }
};
 //BA.debugLineNum = 335;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 336;BA.debugLine="TABLE_RECEIVING.Refresh";
mostCurrent._table_receiving._refresh /*String*/ ();
 //BA.debugLineNum = 337;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 339;BA.debugLine="End Sub";
return "";
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 150;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 151;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 152;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static void  _validate_po_status() throws Exception{
ResumableSub_VALIDATE_PO_STATUS rsub = new ResumableSub_VALIDATE_PO_STATUS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_VALIDATE_PO_STATUS extends BA.ResumableSub {
public ResumableSub_VALIDATE_PO_STATUS(wingan.app.receiving_module parent) {
this.parent = parent;
}
wingan.app.receiving_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group12;
int index12;
int groupLen12;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 373;BA.debugLine="PANEL_BG_PO.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_po.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 374;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 375;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Downloading Purchase Or";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Downloading Purchase Order"));
 //BA.debugLineNum = 376;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching data..."));
 //BA.debugLineNum = 377;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 378;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_rece";
_cmd = _createcommand("select_receiving_ref",new Object[]{(Object)(parent.mostCurrent._edittext_po.getText())});
 //BA.debugLineNum = 379;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 37;
return;
case 37:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 380;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 36;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 35;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 381;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 382;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 38;
return;
case 38:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 383;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 33;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 26;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 384;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 24;
group12 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index12 = 0;
groupLen12 = group12.getSize();
this.state = 39;
if (true) break;

case 39:
//C
this.state = 24;
if (index12 < groupLen12) {
this.state = 9;
_row = (Object[])(group12.Get(index12));}
if (true) break;

case 40:
//C
this.state = 39;
index12++;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 385;BA.debugLine="If row(res.Columns.Get(\"po_status\")) = \"PROCES";
if (true) break;

case 10:
//if
this.state = 23;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("po_status"))))]).equals((Object)("PROCESSED"))) { 
this.state = 12;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("po_status"))))]).equals((Object)("RECEIVING"))) { 
this.state = 14;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("po_status"))))]).equals((Object)("RECEIVED"))) { 
this.state = 22;
}if (true) break;

case 12:
//C
this.state = 23;
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 388;BA.debugLine="If row(res.Columns.Get(\"tab_id\")) = LOGIN_MOD";
if (true) break;

case 15:
//if
this.state = 20;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("tab_id"))))]).equals((Object)(parent.mostCurrent._login_module._tab_id /*String*/ )) && (_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("receiving_by"))))]).equals((Object)(parent.mostCurrent._login_module._username /*String*/ ))) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 20;
 //BA.debugLineNum = 389;BA.debugLine="re_po = 1";
parent._re_po = (int) (1);
 //BA.debugLineNum = 390;BA.debugLine="LOAD_PURCHASE_ORDER_NO";
_load_purchase_order_no();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 392;BA.debugLine="Msgbox2Async(\"The purchase order you inputed";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The purchase order you inputed :"+anywheresoftware.b4a.keywords.Common.CRLF+" Purchase Order No : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("po_doc_no"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Principal : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_name"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" is NOW RECEIVING by :"+anywheresoftware.b4a.keywords.Common.CRLF+" Receiving Checker : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("receiving_by"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Receiving Date : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("receiving_date"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Receiving Time : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("receiving_time"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Tablet : TABLET "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("tab_id"))))])),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 401;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 20:
//C
this.state = 23;
;
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 404;BA.debugLine="Msgbox2Async(\"The purchase order you inputed";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The purchase order you inputed :"+anywheresoftware.b4a.keywords.Common.CRLF+" Purchase Order No : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("po_doc_no"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Principal : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_name"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" is ALREADY RECEIVED by :"+anywheresoftware.b4a.keywords.Common.CRLF+" Receiving Checker : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("receiving_by"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Receiving Date : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("received_date"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Receiving Time : "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("received_time"))))])+anywheresoftware.b4a.keywords.Common.CRLF+" Tablet : TABLET "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("tab_id"))))])),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 413;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 23:
//C
this.state = 40;
;
 if (true) break;
if (true) break;

case 24:
//C
this.state = 33;
;
 if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 418;BA.debugLine="re_po = 0";
parent._re_po = (int) (0);
 //BA.debugLineNum = 419;BA.debugLine="If transaction_type = \"PURCHASE ORDER\" Then";
if (true) break;

case 27:
//if
this.state = 32;
if ((parent._transaction_type).equals("PURCHASE ORDER")) { 
this.state = 29;
}else if((parent._transaction_type).equals("AUTO SHIP")) { 
this.state = 31;
}if (true) break;

case 29:
//C
this.state = 32;
 //BA.debugLineNum = 420;BA.debugLine="LOAD_PURCHASE_ORDER_NO";
_load_purchase_order_no();
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 422;BA.debugLine="INSERT_PURCHASE_ORDER_REF";
_insert_purchase_order_ref();
 if (true) break;

case 32:
//C
this.state = 33;
;
 if (true) break;

case 33:
//C
this.state = 36;
;
 if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 426;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 427;BA.debugLine="Log(\"ERROR: \" & jr.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("786769719","ERROR: "+_jr._errormessage /*String*/ ,0);
 //BA.debugLineNum = 428;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 429;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 36:
//C
this.state = -1;
;
 //BA.debugLineNum = 431;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 432;BA.debugLine="End Sub";
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
