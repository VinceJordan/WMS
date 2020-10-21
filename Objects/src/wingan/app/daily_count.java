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

public class daily_count extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static daily_count mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.daily_count");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (daily_count).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.daily_count");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.daily_count", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (daily_count) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (daily_count) Resume **");
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
		return daily_count.class;
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
            BA.LogInfo("** Activity (daily_count) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (daily_count) Pause event (activity is not paused). **");
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
            daily_count mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (daily_count) Resume **");
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
public static anywheresoftware.b4a.phone.Phone _phone = null;
public static String _inventory_date = "";
public static String _status = "";
public static String _date = "";
public static String _time = "";
public static String _progpath = "";
public static double _scaleauto = 0;
public static String[] _texts = null;
public static String _languageid = "";
public static String _sval = "";
public static double _val = 0;
public static String _op0 = "";
public static double _result1 = 0;
public static String _txt = "";
public static int _new1 = 0;
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public static anywheresoftware.b4a.objects.Timer _ts = null;
public static String _principal_id = "";
public static String _product_id = "";
public static String _reason = "";
public static String _input_type = "";
public static String _caseper = "";
public static String _pcsper = "";
public static String _dozper = "";
public static String _boxper = "";
public static String _bagper = "";
public static String _packper = "";
public static String _total_pieces = "";
public static String _scan_code = "";
public static String _status_trigger = "";
public static String _error_trigger = "";
public static String _item_number = "";
public static String _page = "";
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _controlbitmap = null;
public anywheresoftware.b4a.objects.IME _ctrl = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn0 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn7 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn8 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn9 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnback = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnclr = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnexit = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaperroll = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scvpaperroll = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlkeyboard = null;
public anywheresoftware.b4a.objects.StringUtils _stu = null;
public static String _scannermacaddress = "";
public static boolean _scanneronceconnected = false;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xsearchtemplate _searchtemplate = null;
public wingan.app.b4xsearchtemplate _searchtemplate2 = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_status = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_date = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_inv_date = null;
public wingan.app.b4xtable _table_daily_inventory = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_principal = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_description = null;
public wingan.app.b4xcombobox _cmb_reason = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_quantity = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_input = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvl_list = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_actual = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_add = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_deduct = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_save = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_cancel = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_sysdate = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_upldate = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_bluetooth_status = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_page = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_next = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_prev = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_header_text = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_answer = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_calcu = null;
public b4a.example.dateutils _dateutils = null;
public wingan.app.main _main = null;
public wingan.app.login_module _login_module = null;
public wingan.app.dashboard_module _dashboard_module = null;
public wingan.app.return_module _return_module = null;
public wingan.app.cancelled_module _cancelled_module = null;
public wingan.app.sales_return_module _sales_return_module = null;
public wingan.app.preparing_module _preparing_module = null;
public wingan.app.loading_module _loading_module = null;
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
public ResumableSub_Activity_Create(wingan.app.daily_count parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.daily_count parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 145;BA.debugLine="Activity.LoadLayout(\"daily_count\")";
parent.mostCurrent._activity.LoadLayout("daily_count",mostCurrent.activityBA);
 //BA.debugLineNum = 147;BA.debugLine="controlBitmap = LoadBitmap(File.DirAssets, \"check";
parent._controlbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"check.png");
 //BA.debugLineNum = 148;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 150;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 151;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 152;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 153;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 154;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 155;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 156;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 158;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 159;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 160;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 162;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 163;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 166;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 167;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 168;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 171;BA.debugLine="serial1.Initialize(\"Serial\")";
parent._serial1.Initialize("Serial");
 //BA.debugLineNum = 172;BA.debugLine="Ts.Initialize(\"Timer\", 2000)";
parent._ts.Initialize(processBA,"Timer",(long) (2000));
 //BA.debugLineNum = 174;BA.debugLine="phone.SetScreenOrientation(0)";
parent._phone.SetScreenOrientation(processBA,(int) (0));
 //BA.debugLineNum = 176;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 177;BA.debugLine="Dialog.Initialize(Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 178;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 179;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 180;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 181;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 182;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 183;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 184;BA.debugLine="Dialog.ButtonsTextColor = Colors.Black";
parent.mostCurrent._dialog._buttonstextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 185;BA.debugLine="Dialog.PutAtTop = True";
parent.mostCurrent._dialog._putattop /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 187;BA.debugLine="SearchTemplate.Initialize";
parent.mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 188;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 189;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 190;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 191;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
parent.mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 192;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
parent.mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 195;BA.debugLine="SearchTemplate2.Initialize";
parent.mostCurrent._searchtemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 196;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextBackgr";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 197;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextColor";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 198;BA.debugLine="SearchTemplate2.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 199;BA.debugLine="SearchTemplate2.ItemHightlightColor = Colors.Whit";
parent.mostCurrent._searchtemplate2._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 200;BA.debugLine="SearchTemplate2.TextHighlightColor = Colors.RGB(8";
parent.mostCurrent._searchtemplate2._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 202;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 203;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 204;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 205;BA.debugLine="LOAD_TABLE_HEADER";
_load_table_header();
 //BA.debugLineNum = 207;BA.debugLine="GET_DAILY";
_get_daily();
 //BA.debugLineNum = 209;BA.debugLine="page = 1";
parent._page = BA.NumberToString(1);
 //BA.debugLineNum = 211;BA.debugLine="LABEL_PAGE.Text = \"PAGE : \" & page";
parent.mostCurrent._label_page.setText(BA.ObjectToCharSequence("PAGE : "+parent._page));
 //BA.debugLineNum = 213;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 214;BA.debugLine="Dim Ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 215;BA.debugLine="Ref.Target = EDITTEXT_QUANTITY ' The text field b";
_ref.Target = (Object)(parent.mostCurrent._edittext_quantity.getObject());
 //BA.debugLineNum = 216;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 218;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
ResumableSub_Activity_CreateMenu rsub = new ResumableSub_Activity_CreateMenu(null,_menu);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_CreateMenu extends BA.ResumableSub {
public ResumableSub_Activity_CreateMenu(wingan.app.daily_count parent,de.amberhome.objects.appcompat.ACMenuWrapper _menu) {
this.parent = parent;
this._menu = _menu;
}
wingan.app.daily_count parent;
de.amberhome.objects.appcompat.ACMenuWrapper _menu;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item2 = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 220;BA.debugLine="Log(\"start\")";
anywheresoftware.b4a.keywords.Common.LogImpl("757081857","start",0);
 //BA.debugLineNum = 221;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 222;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 223;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 224;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 225;BA.debugLine="Dim item2 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item2 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item2 = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (0),BA.ObjectToCharSequence("check"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 226;BA.debugLine="item2.ShowAsAction = item2.SHOW_AS_ACTION_ALWAYS";
_item2.setShowAsAction(_item2.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 227;BA.debugLine="UpdateIcon(\"check\", controlBitmap)";
_updateicon("check",parent._controlbitmap);
 //BA.debugLineNum = 228;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 229;BA.debugLine="GET_STATUS";
_get_status();
 //BA.debugLineNum = 230;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 1903;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 1904;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 1905;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 1907;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 239;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 240;BA.debugLine="Log (\"Activity paused. Disconnecting...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("757212929","Activity paused. Disconnecting...",0);
 //BA.debugLineNum = 241;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 242;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 243;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 232;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 233;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("757147393","Resuming...",0);
 //BA.debugLineNum = 234;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 235;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 236;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 238;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.daily_count parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.daily_count parent;
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
 //BA.debugLineNum = 282;BA.debugLine="If Item.Title = \"cart\" Then";
if (true) break;

case 1:
//if
this.state = 26;
if ((_item.getTitle()).equals("cart")) { 
this.state = 3;
}else if((_item.getTitle()).equals("check")) { 
this.state = 11;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 283;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("757606146","Resuming...",0);
 //BA.debugLineNum = 284;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Connecting...\"";
parent.mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Connecting..."));
 //BA.debugLineNum = 285;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Blue";
parent.mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 286;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 287;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 288;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 289;BA.debugLine="If ScannerOnceConnected=True Then";
if (true) break;

case 4:
//if
this.state = 9;
if (parent._scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 290;BA.debugLine="Ts.Enabled=True";
parent._ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 291;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Connected\"";
parent.mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Connected"));
 //BA.debugLineNum = 292;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Green";
parent.mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 293;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 294;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 296;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Disconnected\"";
parent.mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Disconnected"));
 //BA.debugLineNum = 297;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Red";
parent.mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 298;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 299;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 if (true) break;

case 9:
//C
this.state = 26;
;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 302;BA.debugLine="If status_trigger = 0 Then";
if (true) break;

case 12:
//if
this.state = 25;
if ((parent._status_trigger).equals(BA.NumberToString(0))) { 
this.state = 14;
}else {
this.state = 20;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 303;BA.debugLine="Msgbox2Async(\"Are you sure you want to finalize";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to finalize this actual counting?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 304;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 27;
return;
case 27:
//C
this.state = 15;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 305;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 15:
//if
this.state = 18;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 17;
}if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 306;BA.debugLine="UPDATE_PRODUCTOFFTAKE";
_update_productofftake();
 if (true) break;

case 18:
//C
this.state = 25;
;
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 309;BA.debugLine="Msgbox2Async(\"Are you sure you want to upload t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to upload this inventory date? Once you upload, you cannot configure this data."),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 310;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 28;
return;
case 28:
//C
this.state = 21;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 311;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 21:
//if
this.state = 24;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 23;
}if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 312;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = 26;
;
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 316;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 262;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 263;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 264;BA.debugLine="StartActivity(DAILY_INVENTORY_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._daily_inventory_module.getObject()));
 //BA.debugLineNum = 266;BA.debugLine="DAILY_INVENTORY_MODULE.clear_trigger = 0";
mostCurrent._daily_inventory_module._clear_trigger /*String*/  = BA.NumberToString(0);
 //BA.debugLineNum = 267;BA.debugLine="End Sub";
return "";
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 819;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 820;BA.debugLine="Log(\"Connection broken...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("758327041","Connection broken...",0);
 //BA.debugLineNum = 821;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 822;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 823;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Disconnected\"";
mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Disconnected"));
 //BA.debugLineNum = 824;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Red";
mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 825;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 826;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 827;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 828;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 829;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Connected\"";
mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Connected"));
 //BA.debugLineNum = 830;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Green";
mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 831;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 832;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 834;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 };
 //BA.debugLineNum = 836;BA.debugLine="End Sub";
return "";
}
public static void  _astream_newdata(byte[] _buffer) throws Exception{
ResumableSub_AStream_NewData rsub = new ResumableSub_AStream_NewData(null,_buffer);
rsub.resume(processBA, null);
}
public static class ResumableSub_AStream_NewData extends BA.ResumableSub {
public ResumableSub_AStream_NewData(wingan.app.daily_count parent,byte[] _buffer) {
this.parent = parent;
this._buffer = _buffer;
}
wingan.app.daily_count parent;
byte[] _buffer;
int _trigger = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _row = 0;
int _result = 0;
int _qrow = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int step7;
int limit7;
int step21;
int limit21;
int step35;
int limit35;
int step46;
int limit46;
int step79;
int limit79;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 686;BA.debugLine="Log(\"Received: \" & BytesToString(Buffer, 0, Buffe";
anywheresoftware.b4a.keywords.Common.LogImpl("758261505","Received: "+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"),0);
 //BA.debugLineNum = 689;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 690;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 691;BA.debugLine="If cursor2.RowCount >= 2 Then";
if (true) break;

case 1:
//if
this.state = 22;
if (parent._cursor2.getRowCount()>=2) { 
this.state = 3;
}else if(parent._cursor2.getRowCount()==1) { 
this.state = 15;
}else {
this.state = 21;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 692;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 693;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 694;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step7 = 1;
limit7 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 76;
if (true) break;

case 76:
//C
this.state = 7;
if ((step7 > 0 && _row <= limit7) || (step7 < 0 && _row >= limit7)) this.state = 6;
if (true) break;

case 77:
//C
this.state = 76;
_row = ((int)(0 + _row + step7)) ;
if (true) break;

case 6:
//C
this.state = 77;
 //BA.debugLineNum = 695;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 696;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 697;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 699;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) '";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 700;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 78;
return;
case 78:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 701;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 8:
//if
this.state = 13;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 10;
}else {
this.state = 12;
}if (true) break;

case 10:
//C
this.state = 13;
 //BA.debugLineNum = 702;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 703;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 705;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 13:
//C
this.state = 22;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 710;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 16:
//for
this.state = 19;
step21 = 1;
limit21 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 79;
if (true) break;

case 79:
//C
this.state = 19;
if ((step21 > 0 && _row <= limit21) || (step21 < 0 && _row >= limit21)) this.state = 18;
if (true) break;

case 80:
//C
this.state = 79;
_row = ((int)(0 + _row + step21)) ;
if (true) break;

case 18:
//C
this.state = 80;
 //BA.debugLineNum = 711;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 712;BA.debugLine="Log(1)";
anywheresoftware.b4a.keywords.Common.LogImpl("758261531",BA.NumberToString(1),0);
 //BA.debugLineNum = 713;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor2.GetString";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 714;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;
if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 717;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 718;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not REG";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 719;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 22:
//C
this.state = 23;
;
 //BA.debugLineNum = 722;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 81;
return;
case 81:
//C
this.state = 23;
;
 //BA.debugLineNum = 723;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE product_description ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 724;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 23:
//if
this.state = 32;
if (parent._cursor4.getRowCount()>0) { 
this.state = 25;
}else {
this.state = 31;
}if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 725;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 26:
//for
this.state = 29;
step35 = 1;
limit35 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 82;
if (true) break;

case 82:
//C
this.state = 29;
if ((step35 > 0 && _row <= limit35) || (step35 < 0 && _row >= limit35)) this.state = 28;
if (true) break;

case 83:
//C
this.state = 82;
_row = ((int)(0 + _row + step35)) ;
if (true) break;

case 28:
//C
this.state = 83;
 //BA.debugLineNum = 726;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 if (true) break;
if (true) break;

case 29:
//C
this.state = 32;
;
 if (true) break;

case 31:
//C
this.state = 32;
 //BA.debugLineNum = 729;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 730;BA.debugLine="Msgbox2Async(\"The product you scanned :\"& CRLF &";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._label_load_description.getText()+" "+anywheresoftware.b4a.keywords.Common.CRLF+"is not added to this template."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 731;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 32:
//C
this.state = 33;
;
 //BA.debugLineNum = 733;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 84;
return;
case 84:
//C
this.state = 33;
;
 //BA.debugLineNum = 734;BA.debugLine="If trigger = 0 Then";
if (true) break;

case 33:
//if
this.state = 75;
if (_trigger==0) { 
this.state = 35;
}if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 735;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 736;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 36:
//for
this.state = 58;
step46 = 1;
limit46 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 85;
if (true) break;

case 85:
//C
this.state = 58;
if ((step46 > 0 && _qrow <= limit46) || (step46 < 0 && _qrow >= limit46)) this.state = 38;
if (true) break;

case 86:
//C
this.state = 85;
_qrow = ((int)(0 + _qrow + step46)) ;
if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 737;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 738;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"pr";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 739;BA.debugLine="principal_id = cursor3.GetString(\"principal_id\"";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 740;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 742;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 743;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0 T";
if (true) break;

case 39:
//if
this.state = 42;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 41;
}if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 744;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 746;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 Th";

case 42:
//if
this.state = 45;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 44;
}if (true) break;

case 44:
//C
this.state = 45;
 //BA.debugLineNum = 747;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 749;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 Th";

case 45:
//if
this.state = 48;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 47;
}if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 750;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 752;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 Th";

case 48:
//if
this.state = 51;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 50;
}if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 753;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 755;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 Th";

case 51:
//if
this.state = 54;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 53;
}if (true) break;

case 53:
//C
this.state = 54;
 //BA.debugLineNum = 756;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 758;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0 T";

case 54:
//if
this.state = 57;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 56;
}if (true) break;

case 56:
//C
this.state = 57;
 //BA.debugLineNum = 759;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 57:
//C
this.state = 86;
;
 //BA.debugLineNum = 762;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS\"";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 763;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 764;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 765;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 766;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 767;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS\"";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 if (true) break;
if (true) break;

case 58:
//C
this.state = 59;
;
 //BA.debugLineNum = 770;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principal";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 771;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 59:
//if
this.state = 66;
if (parent._cursor6.getRowCount()>0) { 
this.state = 61;
}if (true) break;

case 61:
//C
this.state = 62;
 //BA.debugLineNum = 772;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 62:
//for
this.state = 65;
step79 = 1;
limit79 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 87;
if (true) break;

case 87:
//C
this.state = 65;
if ((step79 > 0 && _row <= limit79) || (step79 < 0 && _row >= limit79)) this.state = 64;
if (true) break;

case 88:
//C
this.state = 87;
_row = ((int)(0 + _row + step79)) ;
if (true) break;

case 64:
//C
this.state = 88;
 //BA.debugLineNum = 773;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 774;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring(";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 65:
//C
this.state = 66;
;
 if (true) break;

case 66:
//C
this.state = 67;
;
 //BA.debugLineNum = 777;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 89;
return;
case 89:
//C
this.state = 67;
;
 //BA.debugLineNum = 778;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 779;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Color";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 780;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 781;BA.debugLine="reason = \"N/A\"";
parent._reason = "N/A";
 //BA.debugLineNum = 782;BA.debugLine="input_type = \"BARCODE\"";
parent._input_type = "BARCODE";
 //BA.debugLineNum = 783;BA.debugLine="scan_code = BytesToString(Buffer, 0, Buffer.Len";
parent._scan_code = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 //BA.debugLineNum = 784;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 785;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 90;
return;
case 90:
//C
this.state = 67;
;
 //BA.debugLineNum = 786;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"SAVED\" Then";
if (true) break;

case 67:
//if
this.state = 74;
if ((parent.mostCurrent._label_load_status.getText()).equals("SAVED")) { 
this.state = 69;
}else if((parent.mostCurrent._label_load_status.getText()).equals("FINAL")) { 
this.state = 71;
}else {
this.state = 73;
}if (true) break;

case 69:
//C
this.state = 74;
 //BA.debugLineNum = 787;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 788;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 91;
return;
case 91:
//C
this.state = 74;
;
 //BA.debugLineNum = 789;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 790;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 92;
return;
case 92:
//C
this.state = 74;
;
 //BA.debugLineNum = 791;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 792;BA.debugLine="BUTTON_SAVE.Visible = True";
parent.mostCurrent._button_save.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 793;BA.debugLine="CMB_REASON.cmbBox.Enabled = True";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 794;BA.debugLine="CMB_UNIT.cmbBox.Enabled = True";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 795;BA.debugLine="EDITTEXT_QUANTITY.Enabled = True";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 796;BA.debugLine="CMB_UNIT.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (0));
 //BA.debugLineNum = 797;BA.debugLine="CMB_REASON.SelectedIndex = 0";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (0));
 if (true) break;

case 71:
//C
this.state = 74;
 //BA.debugLineNum = 799;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 800;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 801;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 93;
return;
case 93:
//C
this.state = 74;
;
 //BA.debugLineNum = 802;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 803;BA.debugLine="BUTTON_SAVE.Visible = True";
parent.mostCurrent._button_save.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 804;BA.debugLine="CMB_REASON.cmbBox.Enabled = True";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 805;BA.debugLine="CMB_UNIT.cmbBox.Enabled = True";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 806;BA.debugLine="EDITTEXT_QUANTITY.Enabled = True";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 807;BA.debugLine="CMB_UNIT.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (0));
 if (true) break;

case 73:
//C
this.state = 74;
 //BA.debugLineNum = 809;BA.debugLine="BUTTON_SAVE.Visible = False";
parent.mostCurrent._button_save.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 810;BA.debugLine="CMB_REASON.cmbBox.Enabled = False";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 811;BA.debugLine="CMB_UNIT.cmbBox.Enabled = False";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 812;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 74:
//C
this.state = 75;
;
 //BA.debugLineNum = 814;BA.debugLine="LOAD_LIST";
_load_list();
 //BA.debugLineNum = 815;BA.debugLine="CLEAR";
_clear();
 if (true) break;

case 75:
//C
this.state = -1;
;
 //BA.debugLineNum = 818;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _inputlist_result(int _result) throws Exception{
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 837;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 838;BA.debugLine="Log(\"Connection terminated...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("758392577","Connection terminated...",0);
 //BA.debugLineNum = 839;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 840;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 257;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 258;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 259;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 260;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return null;
}
public static String  _btnback_click() throws Exception{
 //BA.debugLineNum = 1831;BA.debugLine="Sub btnBack_Click";
 //BA.debugLineNum = 1832;BA.debugLine="If sVal.Length > 0 Then";
if (_sval.length()>0) { 
 //BA.debugLineNum = 1833;BA.debugLine="Txt = sVal.SubString2(0, sVal.Length - 1)";
_txt = _sval.substring((int) (0),(int) (_sval.length()-1));
 //BA.debugLineNum = 1834;BA.debugLine="sVal = sVal.SubString2(0, sVal.Length - 1)";
_sval = _sval.substring((int) (0),(int) (_sval.length()-1));
 //BA.debugLineNum = 1835;BA.debugLine="UpdateTape";
_updatetape();
 };
 //BA.debugLineNum = 1837;BA.debugLine="End Sub";
return "";
}
public static String  _btncharsize_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 1851;BA.debugLine="Sub btnCharSize_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 1852;BA.debugLine="If Checked = False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1853;BA.debugLine="lblPaperRoll.TextSize = 16 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (16*_scaleauto));
 }else {
 //BA.debugLineNum = 1855;BA.debugLine="lblPaperRoll.TextSize = 22 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (22*_scaleauto));
 };
 //BA.debugLineNum = 1857;BA.debugLine="End Sub";
return "";
}
public static String  _btnclr_click() throws Exception{
 //BA.debugLineNum = 1812;BA.debugLine="Sub btnClr_Click";
 //BA.debugLineNum = 1819;BA.debugLine="Val = 0";
_val = 0;
 //BA.debugLineNum = 1820;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1821;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1822;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1823;BA.debugLine="Txt = \"\"";
_txt = "";
 //BA.debugLineNum = 1824;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1825;BA.debugLine="lblPaperRoll.Text = \"\"";
mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1826;BA.debugLine="lblPaperRoll.Height = scvPaperRoll.Height";
mostCurrent._lblpaperroll.setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1827;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1828;BA.debugLine="LABEL_LOAD_ANSWER.Text = \"0\"";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1830;BA.debugLine="End Sub";
return "";
}
public static String  _btndigit_click() throws Exception{
String _bs = "";
anywheresoftware.b4a.objects.ConcreteViewWrapper _send = null;
 //BA.debugLineNum = 1619;BA.debugLine="Sub btnDigit_Click";
 //BA.debugLineNum = 1620;BA.debugLine="Dim bs As String, Send As View";
_bs = "";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 1622;BA.debugLine="If New1 = 0 Then";
if (_new1==0) { 
 //BA.debugLineNum = 1623;BA.debugLine="New1 = 1";
_new1 = (int) (1);
 };
 //BA.debugLineNum = 1626;BA.debugLine="Send = Sender";
_send = (anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 1627;BA.debugLine="bs = Send.Tag";
_bs = BA.ObjectToString(_send.getTag());
 //BA.debugLineNum = 1629;BA.debugLine="Select bs";
switch (BA.switchObjectToInt(_bs,"0","1","2","3","4","5","6","7","8","9","3.1415926535897932",".","a","b","c","d","e","y","g","s","x","f")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: 
case 6: 
case 7: 
case 8: 
case 9: 
case 10: 
case 11: {
 //BA.debugLineNum = 1631;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"g","s","m","x")) {
case 0: 
case 1: 
case 2: 
case 3: {
 //BA.debugLineNum = 1633;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1636;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1637;BA.debugLine="Txt = Txt & CRLF & CRLF";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 1638;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1639;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1640;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1641;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 };
 //BA.debugLineNum = 1644;BA.debugLine="If bs = \"3.1415926535897932\" Then";
if ((_bs).equals("3.1415926535897932")) { 
 //BA.debugLineNum = 1645;BA.debugLine="If sVal <> \"\" Then";
if ((_sval).equals("") == false) { 
 //BA.debugLineNum = 1646;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1648;BA.debugLine="Txt = Txt & cPI";
_txt = _txt+BA.NumberToString(anywheresoftware.b4a.keywords.Common.cPI);
 //BA.debugLineNum = 1649;BA.debugLine="sVal = cPI";
_sval = BA.NumberToString(anywheresoftware.b4a.keywords.Common.cPI);
 }else if((_bs).equals(".")) { 
 //BA.debugLineNum = 1651;BA.debugLine="If sVal.IndexOf(\".\") < 0 Then";
if (_sval.indexOf(".")<0) { 
 //BA.debugLineNum = 1652;BA.debugLine="Txt = Txt & bs";
_txt = _txt+_bs;
 //BA.debugLineNum = 1653;BA.debugLine="sVal = sVal & bs";
_sval = _sval+_bs;
 };
 }else {
 //BA.debugLineNum = 1656;BA.debugLine="Txt = Txt & bs";
_txt = _txt+_bs;
 //BA.debugLineNum = 1657;BA.debugLine="sVal = sVal & bs";
_sval = _sval+_bs;
 };
 break; }
case 12: 
case 13: 
case 14: 
case 15: 
case 16: 
case 17: {
 //BA.debugLineNum = 1660;BA.debugLine="If sVal =\"\" Then";
if ((_sval).equals("")) { 
 //BA.debugLineNum = 1661;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"a","b","c","d","y","")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: {
 //BA.debugLineNum = 1663;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1665;BA.debugLine="sVal = Result1";
_sval = BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1667;BA.debugLine="GetValue(bs)";
_getvalue(_bs);
 break; }
case 18: 
case 19: 
case 20: {
 //BA.debugLineNum = 1669;BA.debugLine="If sVal = \"\" Then";
if ((_sval).equals("")) { 
 //BA.debugLineNum = 1670;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"a","b","c","d","y","")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: {
 //BA.debugLineNum = 1672;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1674;BA.debugLine="sVal = Result1";
_sval = BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1676;BA.debugLine="If Op0 = \"\" Then";
if ((_op0).equals("")) { 
 //BA.debugLineNum = 1677;BA.debugLine="Result1 = sVal";
_result1 = (double)(Double.parseDouble(_sval));
 };
 //BA.debugLineNum = 1679;BA.debugLine="GetValue(bs)";
_getvalue(_bs);
 break; }
case 21: {
 //BA.debugLineNum = 1681;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1682;BA.debugLine="Txt = Txt & CRLF & CRLF";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 1683;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1684;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1685;BA.debugLine="Op0 = \"\"";
_op0 = "";
 };
 break; }
}
;
 //BA.debugLineNum = 1689;BA.debugLine="UpdateTape";
_updatetape();
 //BA.debugLineNum = 1690;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_click() throws Exception{
 //BA.debugLineNum = 1838;BA.debugLine="Sub btnExit_Click";
 //BA.debugLineNum = 1839;BA.debugLine="Val = 0";
_val = 0;
 //BA.debugLineNum = 1840;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1841;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1842;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1843;BA.debugLine="Txt = \"\"";
_txt = "";
 //BA.debugLineNum = 1844;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1845;BA.debugLine="lblPaperRoll.Text = \"\"";
mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1846;BA.debugLine="lblPaperRoll.Height = scvPaperRoll.Height";
mostCurrent._lblpaperroll.setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1847;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1848;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_LOAD_ANSWER.Text";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(mostCurrent._label_load_answer.getText()));
 //BA.debugLineNum = 1849;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1850;BA.debugLine="End Sub";
return "";
}
public static String  _button_calcu_click() throws Exception{
 //BA.debugLineNum = 1893;BA.debugLine="Sub BUTTON_CALCU_Click";
 //BA.debugLineNum = 1894;BA.debugLine="oncal";
_oncal();
 //BA.debugLineNum = 1895;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,True)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1896;BA.debugLine="PANEL_BG_CALCU.BringToFront";
mostCurrent._panel_bg_calcu.BringToFront();
 //BA.debugLineNum = 1897;BA.debugLine="End Sub";
return "";
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 1129;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 1130;BA.debugLine="CLEAR";
_clear();
 //BA.debugLineNum = 1131;BA.debugLine="End Sub";
return "";
}
public static String  _button_exit_calcu_click() throws Exception{
 //BA.debugLineNum = 1889;BA.debugLine="Sub BUTTON_EXIT_CALCU_Click";
 //BA.debugLineNum = 1890;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1891;BA.debugLine="End Sub";
return "";
}
public static String  _button_exit_click() throws Exception{
 //BA.debugLineNum = 853;BA.debugLine="Sub BUTTON_EXIT_Click";
 //BA.debugLineNum = 854;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 855;BA.debugLine="End Sub";
return "";
}
public static void  _button_next_click() throws Exception{
ResumableSub_BUTTON_NEXT_Click rsub = new ResumableSub_BUTTON_NEXT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_NEXT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_NEXT_Click(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1568;BA.debugLine="TABLE_DAILY_INVENTORY.CurrentPage = TABLE_DAILY_I";
parent.mostCurrent._table_daily_inventory._setcurrentpage /*int*/ ((int) (parent.mostCurrent._table_daily_inventory._getcurrentpage /*int*/ ()+1));
 //BA.debugLineNum = 1569;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1570;BA.debugLine="page = page + 1";
parent._page = BA.NumberToString((double)(Double.parseDouble(parent._page))+1);
 //BA.debugLineNum = 1571;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 1572;BA.debugLine="LABEL_PAGE.Text = \"PAGE : \" & page";
parent.mostCurrent._label_page.setText(BA.ObjectToCharSequence("PAGE : "+parent._page));
 //BA.debugLineNum = 1573;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_prev_click() throws Exception{
ResumableSub_BUTTON_PREV_Click rsub = new ResumableSub_BUTTON_PREV_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_PREV_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_PREV_Click(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 1561;BA.debugLine="TABLE_DAILY_INVENTORY.CurrentPage = TABLE_DAILY_I";
parent.mostCurrent._table_daily_inventory._setcurrentpage /*int*/ ((int) (parent.mostCurrent._table_daily_inventory._getcurrentpage /*int*/ ()-1));
 //BA.debugLineNum = 1562;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 1563;BA.debugLine="page = page - 1";
parent._page = BA.NumberToString((double)(Double.parseDouble(parent._page))-1);
 //BA.debugLineNum = 1564;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 1565;BA.debugLine="LABEL_PAGE.Text = \"PAGE : \" & page";
parent.mostCurrent._label_page.setText(BA.ObjectToCharSequence("PAGE : "+parent._page));
 //BA.debugLineNum = 1566;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_save_click() throws Exception{
ResumableSub_BUTTON_SAVE_Click rsub = new ResumableSub_BUTTON_SAVE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SAVE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SAVE_Click(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
String _system_count = "";
String _transaction_number = "";
String _date1 = "";
String _time1 = "";
int _i = 0;
String _query = "";
String _unit = "";
int _result = 0;
String _insert_query = "";
int step36;
int limit36;
int step54;
int limit54;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 887;BA.debugLine="If LOGIN_MODULE.username <> \"\" Or LOGIN_MODULE.ta";
if (true) break;

case 1:
//if
this.state = 96;
if ((parent.mostCurrent._login_module._username /*String*/ ).equals("") == false || (parent.mostCurrent._login_module._tab_id /*String*/ ).equals("") == false) { 
this.state = 3;
}else {
this.state = 95;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 888;BA.debugLine="Dim system_count As String";
_system_count = "";
 //BA.debugLineNum = 889;BA.debugLine="Dim transaction_number As String";
_transaction_number = "";
 //BA.debugLineNum = 890;BA.debugLine="Dim date1 As String";
_date1 = "";
 //BA.debugLineNum = 891;BA.debugLine="Dim time1 As String";
_time1 = "";
 //BA.debugLineNum = 892;BA.debugLine="If EDITTEXT_QUANTITY.Text = \"\" Or EDITTEXT_QUANTI";
if (true) break;

case 4:
//if
this.state = 93;
if ((parent.mostCurrent._edittext_quantity.getText()).equals("") || (double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))<=0) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 93;
 //BA.debugLineNum = 893;BA.debugLine="Msgbox2Async(\"You cannot input a zero value quan";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You cannot input a zero value quantity."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 894;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 895;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 97;
return;
case 97:
//C
this.state = 93;
;
 //BA.debugLineNum = 896;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 897;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 98;
return;
case 98:
//C
this.state = 93;
;
 //BA.debugLineNum = 898;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 900;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = -1 Then";
if (true) break;

case 9:
//if
this.state = 12;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 901;BA.debugLine="CMB_UNIT.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;
;
 //BA.debugLineNum = 904;BA.debugLine="If CMB_REASON.cmbBox.SelectedIndex = -1 Then";

case 12:
//if
this.state = 15;
if (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 905;BA.debugLine="CMB_REASON.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;
;
 //BA.debugLineNum = 908;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbB";

case 15:
//if
this.state = 28;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 17;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 19;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 21;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 23;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 25;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 27;
}if (true) break;

case 17:
//C
this.state = 28;
 //BA.debugLineNum = 909;BA.debugLine="total_pieces = caseper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 19:
//C
this.state = 28;
 //BA.debugLineNum = 911;BA.debugLine="total_pieces = pcsper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 21:
//C
this.state = 28;
 //BA.debugLineNum = 913;BA.debugLine="total_pieces = dozper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 23:
//C
this.state = 28;
 //BA.debugLineNum = 915;BA.debugLine="total_pieces = boxper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 25:
//C
this.state = 28;
 //BA.debugLineNum = 917;BA.debugLine="total_pieces = bagper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 919;BA.debugLine="total_pieces = packper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;
;
 //BA.debugLineNum = 921;BA.debugLine="If BUTTON_SAVE.Text = \" SAVE\" Then";

case 28:
//if
this.state = 92;
if ((parent.mostCurrent._button_save.getText()).equals(" SAVE")) { 
this.state = 30;
}else {
this.state = 79;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 922;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT MAX(CAST";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT MAX(CAST(item_number as INT)) as item_number from daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'")));
 //BA.debugLineNum = 923;BA.debugLine="If cursor7.RowCount > 0 Then";
if (true) break;

case 31:
//if
this.state = 49;
if (parent._cursor7.getRowCount()>0) { 
this.state = 33;
}if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 924;BA.debugLine="For i = 0 To cursor7.RowCount - 1";
if (true) break;

case 34:
//for
this.state = 43;
step36 = 1;
limit36 = (int) (parent._cursor7.getRowCount()-1);
_i = (int) (0) ;
this.state = 99;
if (true) break;

case 99:
//C
this.state = 43;
if ((step36 > 0 && _i <= limit36) || (step36 < 0 && _i >= limit36)) this.state = 36;
if (true) break;

case 100:
//C
this.state = 99;
_i = ((int)(0 + _i + step36)) ;
if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 925;BA.debugLine="cursor7.Position = i";
parent._cursor7.setPosition(_i);
 //BA.debugLineNum = 926;BA.debugLine="If cursor7.GetString(\"item_number\") = Null Or";
if (true) break;

case 37:
//if
this.state = 42;
if (parent._cursor7.GetString("item_number")== null || (parent._cursor7.GetString("item_number")).equals("")) { 
this.state = 39;
}else {
this.state = 41;
}if (true) break;

case 39:
//C
this.state = 42;
 //BA.debugLineNum = 927;BA.debugLine="transaction_number = 1";
_transaction_number = BA.NumberToString(1);
 if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 929;BA.debugLine="transaction_number = cursor7.GetString(\"item_";
_transaction_number = BA.NumberToString((double)(Double.parseDouble(parent._cursor7.GetString("item_number")))+1);
 if (true) break;

case 42:
//C
this.state = 100;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 932;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"SAVED\" Then";

case 43:
//if
this.state = 48;
if ((parent.mostCurrent._label_load_status.getText()).equals("SAVED")) { 
this.state = 45;
}else {
this.state = 47;
}if (true) break;

case 45:
//C
this.state = 48;
 //BA.debugLineNum = 933;BA.debugLine="date1 = \"-\"";
_date1 = "-";
 //BA.debugLineNum = 934;BA.debugLine="time1 = \"-\"";
_time1 = "-";
 if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 936;BA.debugLine="date1 = LABEL_LOAD_DATE.Text.SubString2(0,LAB";
_date1 = parent.mostCurrent._label_load_date.getText().substring((int) (0),parent.mostCurrent._label_load_date.getText().indexOf(" "));
 //BA.debugLineNum = 937;BA.debugLine="time1 = LABEL_LOAD_DATE.Text.SubString2(LABEL";
_time1 = parent.mostCurrent._label_load_date.getText().substring((int) (parent.mostCurrent._label_load_date.getText().indexOf(" ")+1),parent.mostCurrent._label_load_date.getText().length());
 if (true) break;

case 48:
//C
this.state = 49;
;
 if (true) break;

case 49:
//C
this.state = 50;
;
 //BA.debugLineNum = 940;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT system_c";
parent._cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT system_count from daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"' and product_id = '"+parent._product_id+"' GROUP BY product_id")));
 //BA.debugLineNum = 941;BA.debugLine="If cursor8.RowCount > 0 Then";
if (true) break;

case 50:
//if
this.state = 65;
if (parent._cursor8.getRowCount()>0) { 
this.state = 52;
}else {
this.state = 64;
}if (true) break;

case 52:
//C
this.state = 53;
 //BA.debugLineNum = 942;BA.debugLine="For i = 0 To cursor8.RowCount - 1";
if (true) break;

case 53:
//for
this.state = 62;
step54 = 1;
limit54 = (int) (parent._cursor8.getRowCount()-1);
_i = (int) (0) ;
this.state = 101;
if (true) break;

case 101:
//C
this.state = 62;
if ((step54 > 0 && _i <= limit54) || (step54 < 0 && _i >= limit54)) this.state = 55;
if (true) break;

case 102:
//C
this.state = 101;
_i = ((int)(0 + _i + step54)) ;
if (true) break;

case 55:
//C
this.state = 56;
 //BA.debugLineNum = 943;BA.debugLine="cursor8.Position = i";
parent._cursor8.setPosition(_i);
 //BA.debugLineNum = 944;BA.debugLine="If cursor8.GetString(\"system_count\") = Null O";
if (true) break;

case 56:
//if
this.state = 61;
if (parent._cursor8.GetString("system_count")== null || (parent._cursor8.GetString("system_count")).equals("") || (parent._cursor8.GetString("system_count")).equals("0")) { 
this.state = 58;
}else {
this.state = 60;
}if (true) break;

case 58:
//C
this.state = 61;
 //BA.debugLineNum = 945;BA.debugLine="system_count = \"0\"";
_system_count = "0";
 if (true) break;

case 60:
//C
this.state = 61;
 //BA.debugLineNum = 947;BA.debugLine="system_count = cursor8.GetString(\"system_cou";
_system_count = parent._cursor8.GetString("system_count");
 if (true) break;

case 61:
//C
this.state = 102;
;
 if (true) break;
if (true) break;

case 62:
//C
this.state = 65;
;
 if (true) break;

case 64:
//C
this.state = 65;
 //BA.debugLineNum = 951;BA.debugLine="system_count = \"0\"";
_system_count = "0";
 if (true) break;

case 65:
//C
this.state = 66;
;
 //BA.debugLineNum = 954;BA.debugLine="Dim query As String = \"INSERT INTO daily_invent";
_query = "INSERT INTO daily_inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 955;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent.mostCurrent._label_load_inv_date.getText(),_transaction_number,parent._principal_id,parent.mostCurrent._label_load_principal.getText(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,_system_count,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_status.getText(),_date1,_time1,parent._scan_code,parent._reason,parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),BA.NumberToString(0),"-","-",parent.mostCurrent._login_module._tab_id /*String*/ ,parent.mostCurrent._login_module._username /*String*/ }));
 //BA.debugLineNum = 959;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 103;
return;
case 103:
//C
this.state = 66;
;
 //BA.debugLineNum = 960;BA.debugLine="Dim unit As String = CMB_UNIT.cmbBox.SelectedIt";
_unit = parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 961;BA.debugLine="If unit = \"PCS\" Then";
if (true) break;

case 66:
//if
this.state = 77;
if ((_unit).equals("PCS")) { 
this.state = 68;
}else if((_unit).equals("DOZ")) { 
this.state = 76;
}if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 962;BA.debugLine="If EDITTEXT_QUANTITY.Text > 1 Then";
if (true) break;

case 69:
//if
this.state = 74;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))>1) { 
this.state = 71;
}else {
this.state = 73;
}if (true) break;

case 71:
//C
this.state = 74;
 //BA.debugLineNum = 963;BA.debugLine="unit = \"PIECES\"";
_unit = "PIECES";
 if (true) break;

case 73:
//C
this.state = 74;
 //BA.debugLineNum = 965;BA.debugLine="unit = \"PIECE\"";
_unit = "PIECE";
 if (true) break;

case 74:
//C
this.state = 77;
;
 if (true) break;

case 76:
//C
this.state = 77;
 //BA.debugLineNum = 968;BA.debugLine="unit = \"DOZEN\"";
_unit = "DOZEN";
 if (true) break;

case 77:
//C
this.state = 92;
;
 //BA.debugLineNum = 970;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 104;
return;
case 104:
//C
this.state = 92;
;
 //BA.debugLineNum = 972;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 105;
return;
case 105:
//C
this.state = 92;
;
 //BA.debugLineNum = 973;BA.debugLine="ToastMessageShow(\"Product Added\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Added"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 79:
//C
this.state = 80;
 //BA.debugLineNum = 975;BA.debugLine="Msgbox2Async(\"Are you sure you want to update t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update this item?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 976;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 106;
return;
case 106:
//C
this.state = 80;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 977;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 80:
//if
this.state = 91;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 82;
}if (true) break;

case 82:
//C
this.state = 83;
 //BA.debugLineNum = 978;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 83:
//if
this.state = 90;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 85;
}if (true) break;

case 85:
//C
this.state = 86;
 //BA.debugLineNum = 979;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"FINAL\" Then";
if (true) break;

case 86:
//if
this.state = 89;
if ((parent.mostCurrent._label_load_status.getText()).equals("FINAL")) { 
this.state = 88;
}if (true) break;

case 88:
//C
this.state = 89;
 //BA.debugLineNum = 980;BA.debugLine="Dim insert_query As String = \"INSERT INTO da";
_insert_query = "INSERT INTO daily_inventory_disc_table_trail SELECT *,'EDITED' as 'edit_type',? as 'edit_by',? as 'date',? as 'time' from daily_inventory_disc_table WHERE group_id = ? AND inventory_date = ? AND item_number = ?";
 //BA.debugLineNum = 981;BA.debugLine="connection.ExecNonQuery2(insert_query, Array";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent.mostCurrent._label_load_inv_date.getText(),parent._item_number}));
 if (true) break;

case 89:
//C
this.state = 90;
;
 //BA.debugLineNum = 983;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 107;
return;
case 107:
//C
this.state = 90;
;
 //BA.debugLineNum = 984;BA.debugLine="Dim query As String = \"UPDATE daily_inventory";
_query = "UPDATE daily_inventory_disc_table SET unit = ?, quantity = ?, total_pieces = ? , edit_count = edit_count + 1, input_reason = ? , user_info = ? WHERE group_id = ? AND inventory_date = ? AND item_number = ?";
 //BA.debugLineNum = 985;BA.debugLine="connection.ExecNonQuery2(query,Array As Strin";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent.mostCurrent._label_load_inv_date.getText(),parent._item_number}));
 //BA.debugLineNum = 986;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 108;
return;
case 108:
//C
this.state = 90;
;
 //BA.debugLineNum = 987;BA.debugLine="ToastMessageShow(\"Transaction Updated\", False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Updated"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 988;BA.debugLine="LOAD_REASON";
_load_reason();
 if (true) break;

case 90:
//C
this.state = 91;
;
 if (true) break;

case 91:
//C
this.state = 92;
;
 if (true) break;

case 92:
//C
this.state = 93;
;
 //BA.debugLineNum = 992;BA.debugLine="GET_DAILY";
_get_daily();
 //BA.debugLineNum = 993;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 109;
return;
case 109:
//C
this.state = 93;
;
 //BA.debugLineNum = 994;BA.debugLine="LOAD_LIST";
_load_list();
 //BA.debugLineNum = 995;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 110;
return;
case 110:
//C
this.state = 93;
;
 //BA.debugLineNum = 996;BA.debugLine="CLEAR";
_clear();
 if (true) break;

case 93:
//C
this.state = 96;
;
 if (true) break;

case 95:
//C
this.state = 96;
 //BA.debugLineNum = 999;BA.debugLine="Msgbox2Async(\"TABLET ID AND USERNAME CANNOT READ";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 96:
//C
this.state = -1;
;
 //BA.debugLineNum = 1001;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _calcresult1(String _op) throws Exception{
String _res = "";
 //BA.debugLineNum = 1775;BA.debugLine="Sub CalcResult1(Op As String)";
 //BA.debugLineNum = 1776;BA.debugLine="Select Op";
switch (BA.switchObjectToInt(_op,"a","b","c","d","g","s","x","y")) {
case 0: {
 //BA.debugLineNum = 1778;BA.debugLine="Result1 = Result1 + Val";
_result1 = _result1+_val;
 break; }
case 1: {
 //BA.debugLineNum = 1780;BA.debugLine="Result1 = Result1 - Val";
_result1 = _result1-_val;
 break; }
case 2: {
 //BA.debugLineNum = 1782;BA.debugLine="Result1 = Result1 * Val";
_result1 = _result1*_val;
 break; }
case 3: {
 //BA.debugLineNum = 1784;BA.debugLine="Result1 = Result1 / Val";
_result1 = _result1/(double)_val;
 break; }
case 4: {
 //BA.debugLineNum = 1786;BA.debugLine="Result1 = Result1 * Result1";
_result1 = _result1*_result1;
 break; }
case 5: {
 //BA.debugLineNum = 1788;BA.debugLine="Result1 = Sqrt(Result1)";
_result1 = anywheresoftware.b4a.keywords.Common.Sqrt(_result1);
 break; }
case 6: {
 //BA.debugLineNum = 1790;BA.debugLine="If Result1 <> 0 Then";
if (_result1!=0) { 
 //BA.debugLineNum = 1791;BA.debugLine="Result1 = 1 / Result1";
_result1 = 1/(double)_result1;
 };
 break; }
case 7: {
 //BA.debugLineNum = 1794;BA.debugLine="Result1 = Result1 * Val / 100";
_result1 = _result1*_val/(double)100;
 break; }
}
;
 //BA.debugLineNum = 1796;BA.debugLine="Dim res As String = Result1";
_res = BA.NumberToString(_result1);
 //BA.debugLineNum = 1797;BA.debugLine="LABEL_LOAD_ANSWER.Text = res";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence(_res));
 //BA.debugLineNum = 1798;BA.debugLine="End Sub";
return "";
}
public static String  _clear() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 1132;BA.debugLine="Sub CLEAR";
 //BA.debugLineNum = 1133;BA.debugLine="item_number = 0";
_item_number = BA.NumberToString(0);
 //BA.debugLineNum = 1134;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1135;BA.debugLine="bg.Initialize2(Colors.RGB(0,255,70), 5, 0, Colors";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (255),(int) (70)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1136;BA.debugLine="BUTTON_SAVE.Background = bg";
mostCurrent._button_save.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1137;BA.debugLine="BUTTON_SAVE.Text = \" SAVE\"";
mostCurrent._button_save.setText(BA.ObjectToCharSequence(" SAVE"));
 //BA.debugLineNum = 1138;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1139;BA.debugLine="BUTTON_CANCEL.Visible = False";
mostCurrent._button_cancel.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1140;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_reason_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_REASON_SelectedIndexChanged rsub = new ResumableSub_CMB_REASON_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_REASON_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_REASON_SelectedIndexChanged(wingan.app.daily_count parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.daily_count parent;
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
 //BA.debugLineNum = 876;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 877;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 878;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 879;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_unit_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_UNIT_SelectedIndexChanged rsub = new ResumableSub_CMB_UNIT_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_UNIT_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_UNIT_SelectedIndexChanged(wingan.app.daily_count parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.daily_count parent;
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
 //BA.debugLineNum = 881;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 882;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 883;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 884;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 885;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 1149;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 1150;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 1151;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 1152;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 1153;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 1154;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 1155;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 1144;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 1145;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 1146;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,daily_count.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 1147;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 1148;BA.debugLine="End Sub";
return null;
}
public static void  _delete_daily_disc() throws Exception{
ResumableSub_DELETE_DAILY_DISC rsub = new ResumableSub_DELETE_DAILY_DISC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_DAILY_DISC extends BA.ResumableSub {
public ResumableSub_DELETE_DAILY_DISC(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
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
 //BA.debugLineNum = 1283;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 1284;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1285;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 1286;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Uploading Data\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Uploading Data"));
 //BA.debugLineNum = 1287;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching Data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching Data..."));
 //BA.debugLineNum = 1288;BA.debugLine="LABEL_MSGBOX1.Text = \"Loading, Please wait...\"";
parent.mostCurrent._label_msgbox1.setText(BA.ObjectToCharSequence("Loading, Please wait..."));
 //BA.debugLineNum = 1289;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_dail";
_cmd = _createcommand("delete_daily_disc",new Object[]{(Object)(parent.mostCurrent._daily_inventory_module._group_id /*String*/ ),(Object)(parent.mostCurrent._label_load_inv_date.getText())});
 //BA.debugLineNum = 1290;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1291;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1292;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 1293;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1294;BA.debugLine="INSERT_DAILY_DISC";
_insert_daily_disc();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1296;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1297;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1298;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1299;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1300;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1301;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1302;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1303;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1304;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 11;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 1305;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1307;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1308;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1311;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1312;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _js) throws Exception{
}
public static void  _delete_daily_disc_trail() throws Exception{
ResumableSub_DELETE_DAILY_DISC_TRAIL rsub = new ResumableSub_DELETE_DAILY_DISC_TRAIL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_DAILY_DISC_TRAIL extends BA.ResumableSub {
public ResumableSub_DELETE_DAILY_DISC_TRAIL(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
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
 //BA.debugLineNum = 1358;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_dail";
_cmd = _createcommand("delete_daily_disc_trail",new Object[]{(Object)(parent.mostCurrent._daily_inventory_module._group_id /*String*/ ),(Object)(parent.mostCurrent._label_load_inv_date.getText())});
 //BA.debugLineNum = 1359;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1360;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1361;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 1362;BA.debugLine="INSERT_DAILY_DISC_TRAIL";
_insert_daily_disc_trail();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1364;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1365;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1366;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1367;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1368;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1369;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 14;
return;
case 14:
//C
this.state = 6;
;
 //BA.debugLineNum = 1370;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1371;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 15;
return;
case 15:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1372;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 11;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 1373;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1375;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1376;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1379;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1380;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_daily_ref() throws Exception{
ResumableSub_DELETE_DAILY_REF rsub = new ResumableSub_DELETE_DAILY_REF(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_DAILY_REF extends BA.ResumableSub {
public ResumableSub_DELETE_DAILY_REF(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
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
 //BA.debugLineNum = 1424;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_dail";
_cmd = _createcommand("delete_daily_ref",new Object[]{(Object)(parent.mostCurrent._daily_inventory_module._group_id /*String*/ )});
 //BA.debugLineNum = 1425;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1426;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1427;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 1428;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1429;BA.debugLine="INSERT_DAILY_REF";
_insert_daily_ref();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1431;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1432;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1433;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1434;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1435;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1436;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1437;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1438;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1439;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 11;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 1440;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1442;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1443;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1446;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1447;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_daily_template() throws Exception{
ResumableSub_DELETE_DAILY_TEMPLATE rsub = new ResumableSub_DELETE_DAILY_TEMPLATE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DELETE_DAILY_TEMPLATE extends BA.ResumableSub {
public ResumableSub_DELETE_DAILY_TEMPLATE(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
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
 //BA.debugLineNum = 1489;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_dail";
_cmd = _createcommand("delete_daily_template",new Object[]{(Object)(parent.mostCurrent._daily_inventory_module._group_id /*String*/ )});
 //BA.debugLineNum = 1490;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1491;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1492;BA.debugLine="If js.Success Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 1493;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 14;
return;
case 14:
//C
this.state = 12;
;
 //BA.debugLineNum = 1494;BA.debugLine="INSERT_DAILY_TEMPLATE";
_insert_daily_template();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1496;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1497;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1498;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1499;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1500;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1501;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 1502;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1503;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1504;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 11;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 1505;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 1507;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1508;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 1511;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1512;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_daily() throws Exception{
ResumableSub_GET_DAILY rsub = new ResumableSub_GET_DAILY(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_DAILY extends BA.ResumableSub {
public ResumableSub_GET_DAILY(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
anywheresoftware.b4a.objects.collections.List _data = null;
int _i = 0;
String _actual_count = "";
String _add_count = "";
String _deduct_count = "";
String _system_count = "";
int _ia = 0;
Object[] _row = null;
int step6;
int limit6;
int step14;
int limit14;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 334;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 47;
return;
case 47:
//C
this.state = 1;
;
 //BA.debugLineNum = 335;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 336;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 337;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' ORDER by product_variant ASC")));
 //BA.debugLineNum = 338;BA.debugLine="If cursor7.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 42;
if (parent._cursor7.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 339;BA.debugLine="For i = 0 To cursor7.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 41;
step6 = 1;
limit6 = (int) (parent._cursor7.getRowCount()-1);
_i = (int) (0) ;
this.state = 48;
if (true) break;

case 48:
//C
this.state = 41;
if ((step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6)) this.state = 6;
if (true) break;

case 49:
//C
this.state = 48;
_i = ((int)(0 + _i + step6)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 340;BA.debugLine="cursor7.Position = i";
parent._cursor7.setPosition(_i);
 //BA.debugLineNum = 341;BA.debugLine="cursor8 = connection.ExecQuery(\"SELECT (Select";
parent._cursor8 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT (Select sum(total_pieces) FROM daily_inventory_disc_table WHERE (input_reason = 'Actual Count' or input_reason = 'Wrong Count') AND group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent._inventory_date+"' and product_id = '"+parent._cursor7.GetString("product_id")+"') as 'actual_count',"+"(Select sum(total_pieces) FROM daily_inventory_disc_table WHERE input_reason LIKE '%Add' AND group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent._inventory_date+"' and product_id = '"+parent._cursor7.GetString("product_id")+"') as 'add_count',"+"(Select sum(total_pieces) FROM daily_inventory_disc_table WHERE input_reason LIKE '%Deduct' AND group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent._inventory_date+"' and product_id = '"+parent._cursor7.GetString("product_id")+"') as 'deduct_count',"+"system_count FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent._inventory_date+"' and product_id = '"+parent._cursor7.GetString("product_id")+"' GROUP BY product_id")));
 //BA.debugLineNum = 345;BA.debugLine="If cursor8.RowCount > 0 Then";
if (true) break;

case 7:
//if
this.state = 40;
if (parent._cursor8.getRowCount()>0) { 
this.state = 9;
}else {
this.state = 39;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 346;BA.debugLine="Dim actual_count As String";
_actual_count = "";
 //BA.debugLineNum = 347;BA.debugLine="Dim add_count As String";
_add_count = "";
 //BA.debugLineNum = 348;BA.debugLine="Dim deduct_count As String";
_deduct_count = "";
 //BA.debugLineNum = 349;BA.debugLine="Dim system_count As String";
_system_count = "";
 //BA.debugLineNum = 350;BA.debugLine="For ia = 0 To cursor8.RowCount - 1";
if (true) break;

case 10:
//for
this.state = 37;
step14 = 1;
limit14 = (int) (parent._cursor8.getRowCount()-1);
_ia = (int) (0) ;
this.state = 50;
if (true) break;

case 50:
//C
this.state = 37;
if ((step14 > 0 && _ia <= limit14) || (step14 < 0 && _ia >= limit14)) this.state = 12;
if (true) break;

case 51:
//C
this.state = 50;
_ia = ((int)(0 + _ia + step14)) ;
if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 351;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 52;
return;
case 52:
//C
this.state = 13;
;
 //BA.debugLineNum = 352;BA.debugLine="cursor8.Position = ia";
parent._cursor8.setPosition(_ia);
 //BA.debugLineNum = 353;BA.debugLine="If cursor8.GetString(\"actual_count\") = Null O";
if (true) break;

case 13:
//if
this.state = 18;
if (parent._cursor8.GetString("actual_count")== null || (parent._cursor8.GetString("actual_count")).equals("")) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 18;
 //BA.debugLineNum = 354;BA.debugLine="actual_count = \"0\"";
_actual_count = "0";
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 356;BA.debugLine="actual_count = cursor8.GetString(\"actual_cou";
_actual_count = parent._cursor8.GetString("actual_count");
 if (true) break;

case 18:
//C
this.state = 19;
;
 //BA.debugLineNum = 358;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 53;
return;
case 53:
//C
this.state = 19;
;
 //BA.debugLineNum = 359;BA.debugLine="If cursor8.GetString(\"add_count\") = Null Or c";
if (true) break;

case 19:
//if
this.state = 24;
if (parent._cursor8.GetString("add_count")== null || (parent._cursor8.GetString("add_count")).equals("")) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 360;BA.debugLine="add_count = \"0\"";
_add_count = "0";
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 362;BA.debugLine="add_count = cursor8.GetString(\"add_count\")";
_add_count = parent._cursor8.GetString("add_count");
 if (true) break;

case 24:
//C
this.state = 25;
;
 //BA.debugLineNum = 364;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 54;
return;
case 54:
//C
this.state = 25;
;
 //BA.debugLineNum = 365;BA.debugLine="If cursor8.GetString(\"deduct_count\") = Null O";
if (true) break;

case 25:
//if
this.state = 30;
if (parent._cursor8.GetString("deduct_count")== null || (parent._cursor8.GetString("deduct_count")).equals("")) { 
this.state = 27;
}else {
this.state = 29;
}if (true) break;

case 27:
//C
this.state = 30;
 //BA.debugLineNum = 366;BA.debugLine="deduct_count = \"0\"";
_deduct_count = "0";
 if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 368;BA.debugLine="deduct_count = cursor8.GetString(\"deduct_cou";
_deduct_count = parent._cursor8.GetString("deduct_count");
 if (true) break;

case 30:
//C
this.state = 31;
;
 //BA.debugLineNum = 370;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 55;
return;
case 55:
//C
this.state = 31;
;
 //BA.debugLineNum = 371;BA.debugLine="If cursor8.GetString(\"system_count\") = Null O";
if (true) break;

case 31:
//if
this.state = 36;
if (parent._cursor8.GetString("system_count")== null || (parent._cursor8.GetString("system_count")).equals("")) { 
this.state = 33;
}else {
this.state = 35;
}if (true) break;

case 33:
//C
this.state = 36;
 //BA.debugLineNum = 372;BA.debugLine="system_count = \"0\"";
_system_count = "0";
 if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 374;BA.debugLine="system_count = cursor8.GetString(\"system_cou";
_system_count = parent._cursor8.GetString("system_count");
 if (true) break;

case 36:
//C
this.state = 51;
;
 if (true) break;
if (true) break;

case 37:
//C
this.state = 40;
;
 //BA.debugLineNum = 377;BA.debugLine="Dim row(7) As Object";
_row = new Object[(int) (7)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 378;BA.debugLine="row(0) = cursor7.GetString(\"product_variant\")";
_row[(int) (0)] = (Object)(parent._cursor7.GetString("product_variant"));
 //BA.debugLineNum = 379;BA.debugLine="row(1) = cursor7.GetString(\"product_descriptio";
_row[(int) (1)] = (Object)(parent._cursor7.GetString("product_description"));
 //BA.debugLineNum = 380;BA.debugLine="row(2) = Number.Format3((system_count),0,0,0,\"";
_row[(int) (2)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_system_count))),(int) (0),(int) (0),(int) (0),".",",","",(int) (0),(int) (15)));
 //BA.debugLineNum = 381;BA.debugLine="row(3) = Number.Format3((add_count),0,0,0,\".\",";
_row[(int) (3)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_add_count))),(int) (0),(int) (0),(int) (0),".",",","",(int) (0),(int) (15)));
 //BA.debugLineNum = 382;BA.debugLine="row(4) = Number.Format3((deduct_count),0,0,0,\"";
_row[(int) (4)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_deduct_count))),(int) (0),(int) (0),(int) (0),".",",","",(int) (0),(int) (15)));
 //BA.debugLineNum = 383;BA.debugLine="row(5) = Number.Format3((actual_count),0,0,0,\"";
_row[(int) (5)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_actual_count))),(int) (0),(int) (0),(int) (0),".",",","",(int) (0),(int) (15)));
 //BA.debugLineNum = 384;BA.debugLine="row(6) = Number.Format3((actual_count - ((syst";
_row[(int) (6)] = (Object)(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_actual_count))-(((double)(Double.parseDouble(_system_count))+(double)(Double.parseDouble(_add_count)))-(double)(Double.parseDouble(_deduct_count)))),(int) (0),(int) (0),(int) (0),".",",","",(int) (0),(int) (15)));
 //BA.debugLineNum = 385;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 387;BA.debugLine="Dim row(7) As Object";
_row = new Object[(int) (7)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 388;BA.debugLine="row(0) = cursor7.GetString(\"product_variant\")";
_row[(int) (0)] = (Object)(parent._cursor7.GetString("product_variant"));
 //BA.debugLineNum = 389;BA.debugLine="row(1) = cursor7.GetString(\"product_descriptio";
_row[(int) (1)] = (Object)(parent._cursor7.GetString("product_description"));
 //BA.debugLineNum = 390;BA.debugLine="row(2) = \"0\"";
_row[(int) (2)] = (Object)("0");
 //BA.debugLineNum = 391;BA.debugLine="row(3) = \"0\"";
_row[(int) (3)] = (Object)("0");
 //BA.debugLineNum = 392;BA.debugLine="row(4) = \"0\"";
_row[(int) (4)] = (Object)("0");
 //BA.debugLineNum = 393;BA.debugLine="row(5) = \"0\"";
_row[(int) (5)] = (Object)("0");
 //BA.debugLineNum = 394;BA.debugLine="row(6) = \"0\"";
_row[(int) (6)] = (Object)("0");
 //BA.debugLineNum = 395;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 40:
//C
this.state = 49;
;
 if (true) break;
if (true) break;

case 41:
//C
this.state = 42;
;
 if (true) break;

case 42:
//C
this.state = 43;
;
 //BA.debugLineNum = 399;BA.debugLine="TABLE_DAILY_INVENTORY.SetData(Data)";
parent.mostCurrent._table_daily_inventory._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 400;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 43:
//if
this.state = 46;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 45;
}if (true) break;

case 45:
//C
this.state = 46;
 //BA.debugLineNum = 401;BA.debugLine="XSelections.Initialize(TABLE_DAILY_INVENTORY)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_daily_inventory);
 //BA.debugLineNum = 402;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 46:
//C
this.state = -1;
;
 //BA.debugLineNum = 404;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 56;
return;
case 56:
//C
this.state = -1;
;
 //BA.debugLineNum = 405;BA.debugLine="TABLE_DAILY_INVENTORY.pnlHeader.Visible = False";
parent.mostCurrent._table_daily_inventory._pnlheader /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 406;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_status() throws Exception{
ResumableSub_GET_STATUS rsub = new ResumableSub_GET_STATUS(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_STATUS extends BA.ResumableSub {
public ResumableSub_GET_STATUS(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
int _i = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg2 = null;
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
 //BA.debugLineNum = 565;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent._inventory_date+"' GROUP BY group_id ORDER BY date_registered, time_registered ASC LIMIT 1")));
 //BA.debugLineNum = 566;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 18;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 17;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 567;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 15;
step3 = 1;
limit3 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 19;
if (true) break;

case 19:
//C
this.state = 15;
if ((step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3)) this.state = 6;
if (true) break;

case 20:
//C
this.state = 19;
_i = ((int)(0 + _i + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 568;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 7;
;
 //BA.debugLineNum = 569;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 570;BA.debugLine="LABEL_LOAD_INV_DATE.Text = cursor1.GetString(\"i";
parent.mostCurrent._label_load_inv_date.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("inventory_date")));
 //BA.debugLineNum = 571;BA.debugLine="LABEL_LOAD_STATUS.Text = cursor1.GetString(\"sta";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("status")));
 //BA.debugLineNum = 572;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 22;
return;
case 22:
//C
this.state = 7;
;
 //BA.debugLineNum = 573;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"FINAL\" Then";
if (true) break;

case 7:
//if
this.state = 14;
if ((parent.mostCurrent._label_load_status.getText()).equals("FINAL")) { 
this.state = 9;
}else if((parent.mostCurrent._label_load_status.getText()).equals("UPLOADED")) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 9:
//C
this.state = 14;
 //BA.debugLineNum = 574;BA.debugLine="LABEL_LOAD_DATE.Text = cursor1.GetString(\"date";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("date_registered")+" "+parent._cursor1.GetString("time_registered")));
 //BA.debugLineNum = 575;BA.debugLine="LABEL_LOAD_SYSDATE.Text = cursor1.GetString(\"d";
parent.mostCurrent._label_load_sysdate.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("date_final")+" "+parent._cursor1.GetString("time_final")));
 //BA.debugLineNum = 576;BA.debugLine="LABEL_LOAD_UPLDATE.Text = \"----\"";
parent.mostCurrent._label_load_upldate.setText(BA.ObjectToCharSequence("----"));
 //BA.debugLineNum = 577;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 578;BA.debugLine="bg.Initialize2(Colors.Green,5,0,Colors.Black)";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.Green,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 579;BA.debugLine="LABEL_LOAD_STATUS.Background = bg";
parent.mostCurrent._label_load_status.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 580;BA.debugLine="Dim bg2 As ColorDrawable";
_bg2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 581;BA.debugLine="bg2.Initialize2(Colors.ARGB(123,82,169,255),36";
_bg2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (123),(int) (82),(int) (169),(int) (255)),(int) (360),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 582;BA.debugLine="status_trigger = 1";
parent._status_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 583;BA.debugLine="controlBitmap = LoadBitmap(File.DirAssets, \"up";
parent._controlbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"upload.png");
 //BA.debugLineNum = 584;BA.debugLine="UpdateIcon(\"check\", controlBitmap)";
_updateicon("check",parent._controlbitmap);
 if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 586;BA.debugLine="LABEL_LOAD_DATE.Text = cursor1.GetString(\"date";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("date_registered")+" "+parent._cursor1.GetString("time_registered")));
 //BA.debugLineNum = 587;BA.debugLine="LABEL_LOAD_SYSDATE.Text = cursor1.GetString(\"d";
parent.mostCurrent._label_load_sysdate.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("date_final")+" "+parent._cursor1.GetString("time_final")));
 //BA.debugLineNum = 588;BA.debugLine="LABEL_LOAD_UPLDATE.Text = cursor1.GetString(\"d";
parent.mostCurrent._label_load_upldate.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("date_upload")+" "+parent._cursor1.GetString("time_upload")));
 //BA.debugLineNum = 589;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 590;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colo";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 591;BA.debugLine="LABEL_LOAD_STATUS.Background = bg";
parent.mostCurrent._label_load_status.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 592;BA.debugLine="controlBitmap = LoadBitmap(File.DirAssets, \"up";
parent._controlbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"upload.png");
 //BA.debugLineNum = 593;BA.debugLine="UpdateIcon(\"check\", controlBitmap)";
_updateicon("check",parent._controlbitmap);
 //BA.debugLineNum = 594;BA.debugLine="status_trigger = 2";
parent._status_trigger = BA.NumberToString(2);
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 596;BA.debugLine="status_trigger = 0";
parent._status_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 597;BA.debugLine="LABEL_LOAD_STATUS.Text = \"SAVED\"";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence("SAVED"));
 //BA.debugLineNum = 598;BA.debugLine="LABEL_LOAD_DATE.Text = cursor1.GetString(\"date";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("date_registered")+" "+parent._cursor1.GetString("time_registered")));
 //BA.debugLineNum = 599;BA.debugLine="LABEL_LOAD_SYSDATE.Text = \"----\"";
parent.mostCurrent._label_load_sysdate.setText(BA.ObjectToCharSequence("----"));
 //BA.debugLineNum = 600;BA.debugLine="LABEL_LOAD_UPLDATE.Text = \"----\"";
parent.mostCurrent._label_load_upldate.setText(BA.ObjectToCharSequence("----"));
 //BA.debugLineNum = 601;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 602;BA.debugLine="bg.Initialize2(Colors.Red,5,0,Colors.Black)";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.Red,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 603;BA.debugLine="LABEL_LOAD_STATUS.Background = bg";
parent.mostCurrent._label_load_status.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 604;BA.debugLine="Dim bg2 As ColorDrawable";
_bg2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 605;BA.debugLine="bg2.Initialize2(Colors.ARGB(123,0,255,0),360,0";
_bg2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (123),(int) (0),(int) (255),(int) (0)),(int) (360),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 606;BA.debugLine="controlBitmap = LoadBitmap(File.DirAssets, \"ch";
parent._controlbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"check.png");
 //BA.debugLineNum = 607;BA.debugLine="UpdateIcon(\"check\", controlBitmap)";
_updateicon("check",parent._controlbitmap);
 if (true) break;

case 14:
//C
this.state = 20;
;
 if (true) break;
if (true) break;

case 15:
//C
this.state = 18;
;
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 611;BA.debugLine="status_trigger = 0";
parent._status_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 612;BA.debugLine="LABEL_LOAD_INV_DATE.Text = inventory_date";
parent.mostCurrent._label_load_inv_date.setText(BA.ObjectToCharSequence(parent._inventory_date));
 //BA.debugLineNum = 613;BA.debugLine="LABEL_LOAD_STATUS.Text = \"SAVED\"";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence("SAVED"));
 //BA.debugLineNum = 614;BA.debugLine="LABEL_LOAD_DATE.Text = \"----\"";
parent.mostCurrent._label_load_date.setText(BA.ObjectToCharSequence("----"));
 //BA.debugLineNum = 615;BA.debugLine="LABEL_LOAD_SYSDATE.Text = \"----\"";
parent.mostCurrent._label_load_sysdate.setText(BA.ObjectToCharSequence("----"));
 //BA.debugLineNum = 616;BA.debugLine="LABEL_LOAD_UPLDATE.Text = \"----\"";
parent.mostCurrent._label_load_upldate.setText(BA.ObjectToCharSequence("----"));
 //BA.debugLineNum = 617;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 618;BA.debugLine="bg.Initialize2(Colors.Red,5,0,Colors.Black)";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.Red,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 619;BA.debugLine="LABEL_LOAD_STATUS.Background = bg";
parent.mostCurrent._label_load_status.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 620;BA.debugLine="Dim bg2 As ColorDrawable";
_bg2 = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 621;BA.debugLine="bg2.Initialize2(Colors.ARGB(123,0,255,0),360,0,C";
_bg2.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (123),(int) (0),(int) (255),(int) (0)),(int) (360),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 622;BA.debugLine="controlBitmap = LoadBitmap(File.DirAssets, \"chec";
parent._controlbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"check.png");
 //BA.debugLineNum = 623;BA.debugLine="UpdateIcon(\"check\", controlBitmap)";
_updateicon("check",parent._controlbitmap);
 if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 625;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 272;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 273;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 274;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 275;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 276;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 279;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 280;BA.debugLine="End Sub";
return null;
}
public static String  _getvalue(String _op) throws Exception{
 //BA.debugLineNum = 1691;BA.debugLine="Sub GetValue(Op As String)";
 //BA.debugLineNum = 1692;BA.debugLine="If Op0 = \"e\" And (Op = \"s\" Or Op = \"g\" Or Op = \"x";
if ((_op0).equals("e") && ((_op).equals("s") || (_op).equals("g") || (_op).equals("x"))) { 
 //BA.debugLineNum = 1693;BA.debugLine="Val = Result1";
_val = _result1;
 }else {
 //BA.debugLineNum = 1695;BA.debugLine="Val = sVal";
_val = (double)(Double.parseDouble(_sval));
 };
 //BA.debugLineNum = 1698;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1700;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1701;BA.debugLine="If Op0 = \"a\" Or Op0 = \"b\" Or Op0 = \"c\" Or Op0 =";
if ((_op0).equals("a") || (_op0).equals("b") || (_op0).equals("c") || (_op0).equals("d") || (_op0).equals("y")) { 
 //BA.debugLineNum = 1702;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1703;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1705;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1706;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1707;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1708;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1709;BA.debugLine="Op0 = \"e\"";
_op0 = "e";
 //BA.debugLineNum = 1710;BA.debugLine="Op = \"e\"";
_op = "e";
 //BA.debugLineNum = 1711;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1714;BA.debugLine="If New1 = 1 Then";
if (_new1==1) { 
 //BA.debugLineNum = 1715;BA.debugLine="Result1 = Val";
_result1 = _val;
 //BA.debugLineNum = 1716;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1717;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1718;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1719;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1720;BA.debugLine="Op = \"e\"";
_op = "e";
 };
 //BA.debugLineNum = 1722;BA.debugLine="UpdateTape";
_updatetape();
 //BA.debugLineNum = 1723;BA.debugLine="New1 = 2";
_new1 = (int) (2);
 //BA.debugLineNum = 1724;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1725;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1728;BA.debugLine="If Op = \"e\" Then";
if ((_op).equals("e")) { 
 //BA.debugLineNum = 1729;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1730;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1732;BA.debugLine="Txt = Txt & CRLF & \" =  \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+" =  ";
 //BA.debugLineNum = 1733;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1734;BA.debugLine="Txt = Txt & Result1";
_txt = _txt+BA.NumberToString(_result1);
 }else {
 //BA.debugLineNum = 1736;BA.debugLine="If Op0 = \"g\" Or Op0 = \"s\" Or Op0 = \"x\" Then";
if ((_op0).equals("g") || (_op0).equals("s") || (_op0).equals("x")) { 
 //BA.debugLineNum = 1737;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1738;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1739;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1742;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1743;BA.debugLine="If Op0<>\"e\" Then";
if ((_op0).equals("e") == false) { 
 //BA.debugLineNum = 1744;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1746;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1747;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1748;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1749;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1750;BA.debugLine="Op = \"e\"";
_op = "e";
 };
 };
 //BA.debugLineNum = 1753;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1754;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 73;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 75;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 76;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Dim btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn";
mostCurrent._btn0 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn1 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn2 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn3 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn4 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn5 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn6 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn7 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn8 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn9 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Dim btnBack, btnClr, btnExit As Button";
mostCurrent._btnback = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnclr = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnexit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Dim lblPaperRoll As Label";
mostCurrent._lblpaperroll = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Dim scvPaperRoll As ScrollView";
mostCurrent._scvpaperroll = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Dim pnlKeyboard As Panel";
mostCurrent._pnlkeyboard = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Dim stu As StringUtils";
mostCurrent._stu = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 87;BA.debugLine="Dim ScannerMacAddress As String";
mostCurrent._scannermacaddress = "";
 //BA.debugLineNum = 88;BA.debugLine="Dim ScannerOnceConnected As Boolean";
_scanneronceconnected = false;
 //BA.debugLineNum = 90;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 91;BA.debugLine="Private NameColumn(7) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (7)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 93;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 94;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 96;BA.debugLine="Private SearchTemplate2 As B4XSearchTemplate";
mostCurrent._searchtemplate2 = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 98;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 99;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 101;BA.debugLine="Private LABEL_LOAD_STATUS As Label";
mostCurrent._label_load_status = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Private LABEL_LOAD_DATE As Label";
mostCurrent._label_load_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 103;BA.debugLine="Private LABEL_LOAD_INV_DATE As Label";
mostCurrent._label_load_inv_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 104;BA.debugLine="Private TABLE_DAILY_INVENTORY As B4XTable";
mostCurrent._table_daily_inventory = new wingan.app.b4xtable();
 //BA.debugLineNum = 105;BA.debugLine="Private LABEL_LOAD_PRINCIPAL As Label";
mostCurrent._label_load_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 106;BA.debugLine="Private LABEL_LOAD_VARIANT As Label";
mostCurrent._label_load_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Private LABEL_LOAD_DESCRIPTION As Label";
mostCurrent._label_load_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 108;BA.debugLine="Private CMB_REASON As B4XComboBox";
mostCurrent._cmb_reason = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 109;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 110;BA.debugLine="Private EDITTEXT_QUANTITY As EditText";
mostCurrent._edittext_quantity = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 111;BA.debugLine="Private PANEL_BG_INPUT As Panel";
mostCurrent._panel_bg_input = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 112;BA.debugLine="Private LVL_LIST As ListView";
mostCurrent._lvl_list = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 113;BA.debugLine="Private LABEL_LOAD_ACTUAL As Label";
mostCurrent._label_load_actual = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 114;BA.debugLine="Private LABEL_LOAD_ADD As Label";
mostCurrent._label_load_add = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 115;BA.debugLine="Private LABEL_LOAD_DEDUCT As Label";
mostCurrent._label_load_deduct = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 116;BA.debugLine="Private BUTTON_SAVE As Button";
mostCurrent._button_save = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 117;BA.debugLine="Private BUTTON_CANCEL As Button";
mostCurrent._button_cancel = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 118;BA.debugLine="Private LABEL_LOAD_SYSDATE As Label";
mostCurrent._label_load_sysdate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 119;BA.debugLine="Private LABEL_LOAD_UPLDATE As Label";
mostCurrent._label_load_upldate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 120;BA.debugLine="Private LABEL_BLUETOOTH_STATUS As Label";
mostCurrent._label_bluetooth_status = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 121;BA.debugLine="Private LABEL_PAGE As Label";
mostCurrent._label_page = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Private BUTTON_NEXT As Button";
mostCurrent._button_next = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private BUTTON_PREV As Button";
mostCurrent._button_prev = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 124;BA.debugLine="Private LABEL_HEADER_TEXT As Label";
mostCurrent._label_header_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 125;BA.debugLine="Private LABEL_MSGBOX1 As Label";
mostCurrent._label_msgbox1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 126;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 127;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 128;BA.debugLine="Private LABEL_LOAD_ANSWER As Label";
mostCurrent._label_load_answer = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Private PANEL_BG_CALCU As Panel";
mostCurrent._panel_bg_calcu = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static void  _insert_daily_disc() throws Exception{
ResumableSub_INSERT_DAILY_DISC rsub = new ResumableSub_INSERT_DAILY_DISC(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_DAILY_DISC extends BA.ResumableSub {
public ResumableSub_INSERT_DAILY_DISC(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
int _i = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;
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
 //BA.debugLineNum = 1314;BA.debugLine="Dim date As String = DateTime.Date(DateTime.Now)";
parent._date = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1315;BA.debugLine="Dim time As String = DateTime.Time(DateTime.Now)";
parent._time = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1316;BA.debugLine="cursor7 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor7 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'")));
 //BA.debugLineNum = 1317;BA.debugLine="If cursor7.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor7.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1318;BA.debugLine="For i = 0 To cursor7.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step5 = 1;
limit5 = (int) (parent._cursor7.getRowCount()-1);
_i = (int) (0) ;
this.state = 27;
if (true) break;

case 27:
//C
this.state = 13;
if ((step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5)) this.state = 6;
if (true) break;

case 28:
//C
this.state = 27;
_i = ((int)(0 + _i + step5)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1319;BA.debugLine="cursor7.Position = i";
parent._cursor7.setPosition(_i);
 //BA.debugLineNum = 1320;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 29;
return;
case 29:
//C
this.state = 7;
;
 //BA.debugLineNum = 1321;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_da";
_cmd = _createcommand("insert_daily_disc",(Object[])(new String[]{parent._cursor7.GetString("group_id"),parent._cursor7.GetString("inventory_date"),parent._cursor7.GetString("item_number"),parent._cursor7.GetString("principal_id"),parent._cursor7.GetString("principal_name"),parent._cursor7.GetString("product_id"),parent._cursor7.GetString("product_variant"),parent._cursor7.GetString("product_description"),parent._cursor7.GetString("unit"),parent._cursor7.GetString("quantity"),parent._cursor7.GetString("total_pieces"),parent._cursor7.GetString("system_count"),parent._cursor7.GetString("date_registered"),parent._cursor7.GetString("time_registered"),parent._cursor7.GetString("status"),parent._cursor7.GetString("date_final"),parent._cursor7.GetString("time_final"),parent._cursor7.GetString("scan_code"),parent._cursor7.GetString("reason"),parent._cursor7.GetString("input_reason"),parent._cursor7.GetString("edit_count"),parent._cursor7.GetString("date_upload"),parent._cursor7.GetString("time_upload"),parent._cursor7.GetString("tab_id"),parent._cursor7.GetString("user_info")}));
 //BA.debugLineNum = 1326;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1327;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading : \" & cursor7.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading : "+parent._cursor7.GetString("product_description")));
 //BA.debugLineNum = 1328;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1329;BA.debugLine="If js.Success Then";
if (true) break;

case 7:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1332;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1333;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1334;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1335;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("759441174","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1336;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO S";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1337;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1338;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 31;
return;
case 31:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 28;
;
 //BA.debugLineNum = 1340;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 1343;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 1344;BA.debugLine="If error_trigger = 0 Then";
if (true) break;

case 15:
//if
this.state = 26;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 26;
 //BA.debugLineNum = 1345;BA.debugLine="DELETE_DAILY_DISC_TRAIL";
_delete_daily_disc_trail();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1347;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1348;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1349;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 20:
//if
this.state = 25;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 22;
}else {
this.state = 24;
}if (true) break;

case 22:
//C
this.state = 25;
 //BA.debugLineNum = 1350;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1352;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1353;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 25:
//C
this.state = 26;
;
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 1356;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_daily_disc_trail() throws Exception{
ResumableSub_INSERT_DAILY_DISC_TRAIL rsub = new ResumableSub_INSERT_DAILY_DISC_TRAIL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_DAILY_DISC_TRAIL extends BA.ResumableSub {
public ResumableSub_INSERT_DAILY_DISC_TRAIL(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
int _i2 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;
int step4;
int limit4;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1382;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 27;
return;
case 27:
//C
this.state = 1;
;
 //BA.debugLineNum = 1383;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_disc_table_trail WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'")));
 //BA.debugLineNum = 1384;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor2.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1385;BA.debugLine="For i2 = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor2.getRowCount()-1);
_i2 = (int) (0) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 13;
if ((step4 > 0 && _i2 <= limit4) || (step4 < 0 && _i2 >= limit4)) this.state = 6;
if (true) break;

case 29:
//C
this.state = 28;
_i2 = ((int)(0 + _i2 + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1386;BA.debugLine="cursor2.Position = i2";
parent._cursor2.setPosition(_i2);
 //BA.debugLineNum = 1387;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_da";
_cmd = _createcommand("insert_daily_disc_trail",(Object[])(new String[]{parent._cursor2.GetString("group_id"),parent._cursor2.GetString("inventory_date"),parent._cursor2.GetString("item_number"),parent._cursor2.GetString("principal_name"),parent._cursor2.GetString("principal_id"),parent._cursor2.GetString("product_id"),parent._cursor2.GetString("product_variant"),parent._cursor2.GetString("product_description"),parent._cursor2.GetString("unit"),parent._cursor2.GetString("quantity"),parent._cursor2.GetString("total_pieces"),parent._cursor2.GetString("system_count"),parent._cursor2.GetString("date_registered"),parent._cursor2.GetString("time_registered"),parent._cursor2.GetString("status"),parent._cursor2.GetString("date_final"),parent._cursor2.GetString("time_final"),parent._cursor2.GetString("scan_code"),parent._cursor2.GetString("reason"),parent._cursor2.GetString("input_reason"),parent._cursor2.GetString("edit_count"),parent._cursor2.GetString("date_upload"),parent._cursor2.GetString("time_upload"),parent._cursor2.GetString("tab_id"),parent._cursor2.GetString("user_info"),parent._cursor2.GetString("edit_type"),parent._cursor2.GetString("edit_by"),parent._cursor2.GetString("edit_date"),parent._cursor2.GetString("edit_time")}));
 //BA.debugLineNum = 1392;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1393;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1394;BA.debugLine="If js.Success Then";
if (true) break;

case 7:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1397;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1398;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1399;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1400;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1401;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("759572244","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1402;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO S";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1403;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1404;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 31;
return;
case 31:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 29;
;
 //BA.debugLineNum = 1406;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 1409;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 1410;BA.debugLine="If error_trigger = 0 Then";
if (true) break;

case 15:
//if
this.state = 26;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 26;
 //BA.debugLineNum = 1411;BA.debugLine="DELETE_DAILY_REF";
_delete_daily_ref();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1413;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1414;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1415;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 20:
//if
this.state = 25;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 22;
}else {
this.state = 24;
}if (true) break;

case 22:
//C
this.state = 25;
 //BA.debugLineNum = 1416;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1418;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1419;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 25:
//C
this.state = 26;
;
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 1422;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_daily_ref() throws Exception{
ResumableSub_INSERT_DAILY_REF rsub = new ResumableSub_INSERT_DAILY_REF(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_DAILY_REF extends BA.ResumableSub {
public ResumableSub_INSERT_DAILY_REF(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
int _i3 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
int _result = 0;
int step4;
int limit4;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1449;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 27;
return;
case 27:
//C
this.state = 1;
;
 //BA.debugLineNum = 1450;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"'")));
 //BA.debugLineNum = 1451;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1452;BA.debugLine="For i3 = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor3.getRowCount()-1);
_i3 = (int) (0) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 13;
if ((step4 > 0 && _i3 <= limit4) || (step4 < 0 && _i3 >= limit4)) this.state = 6;
if (true) break;

case 29:
//C
this.state = 28;
_i3 = ((int)(0 + _i3 + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1453;BA.debugLine="cursor3.Position = i3";
parent._cursor3.setPosition(_i3);
 //BA.debugLineNum = 1454;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_da";
_cmd = _createcommand("insert_daily_ref",(Object[])(new String[]{parent._cursor3.GetString("group_id"),parent._cursor3.GetString("principal_id"),parent._cursor3.GetString("principal_name"),parent._cursor3.GetString("product_id"),parent._cursor3.GetString("product_variant"),parent._cursor3.GetString("product_description"),parent._cursor3.GetString("date_registered"),parent._cursor3.GetString("time_registered"),parent._cursor3.GetString("tab_id"),parent._cursor3.GetString("user_info")}));
 //BA.debugLineNum = 1458;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1459;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1460;BA.debugLine="If js.Success Then";
if (true) break;

case 7:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1463;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 1464;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("759703312","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1465;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1466;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1467;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO S";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1468;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1469;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 31;
return;
case 31:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = 29;
;
 //BA.debugLineNum = 1471;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 1474;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 15;
;
 //BA.debugLineNum = 1475;BA.debugLine="If error_trigger = 0 Then";
if (true) break;

case 15:
//if
this.state = 26;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 26;
 //BA.debugLineNum = 1476;BA.debugLine="DELETE_DAILY_TEMPLATE";
_delete_daily_template();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1478;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1479;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1480;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 20:
//if
this.state = 25;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 22;
}else {
this.state = 24;
}if (true) break;

case 22:
//C
this.state = 25;
 //BA.debugLineNum = 1481;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1483;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1484;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 25:
//C
this.state = 26;
;
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 1487;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_daily_template() throws Exception{
ResumableSub_INSERT_DAILY_TEMPLATE rsub = new ResumableSub_INSERT_DAILY_TEMPLATE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INSERT_DAILY_TEMPLATE extends BA.ResumableSub {
public ResumableSub_INSERT_DAILY_TEMPLATE(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
int _i4 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
String _query = "";
int _result = 0;
int step4;
int limit4;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1514;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 27;
return;
case 27:
//C
this.state = 1;
;
 //BA.debugLineNum = 1515;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_template_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"'")));
 //BA.debugLineNum = 1516;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 26;
if (parent._cursor4.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1517;BA.debugLine="For i4 = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step4 = 1;
limit4 = (int) (parent._cursor4.getRowCount()-1);
_i4 = (int) (0) ;
this.state = 28;
if (true) break;

case 28:
//C
this.state = 13;
if ((step4 > 0 && _i4 <= limit4) || (step4 < 0 && _i4 >= limit4)) this.state = 6;
if (true) break;

case 29:
//C
this.state = 28;
_i4 = ((int)(0 + _i4 + step4)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1518;BA.debugLine="cursor4.Position = i4";
parent._cursor4.setPosition(_i4);
 //BA.debugLineNum = 1519;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_da";
_cmd = _createcommand("insert_daily_template",(Object[])(new String[]{parent._cursor4.GetString("group_id"),parent._cursor4.GetString("group_name"),parent._cursor4.GetString("month"),parent._cursor4.GetString("year"),parent._cursor4.GetString("date_registered"),parent._cursor4.GetString("time_registered"),parent._cursor4.GetString("edit_count"),parent._cursor4.GetString("date_updated"),parent._cursor4.GetString("time_updated"),parent._cursor4.GetString("user_info"),parent._cursor4.GetString("tab_id")}));
 //BA.debugLineNum = 1523;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1524;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 30;
return;
case 30:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1525;BA.debugLine="If js.Success Then";
if (true) break;

case 7:
//if
this.state = 12;
if (_js._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1527;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1528;BA.debugLine="ToastMessageShow(\"Uploading Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Failed"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1529;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("759834384","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 1530;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO S";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1531;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1532;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 31;
return;
case 31:
//C
this.state = 12;
;
 //BA.debugLineNum = 1533;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 if (true) break;

case 12:
//C
this.state = 29;
;
 //BA.debugLineNum = 1535;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 1537;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploaded Successfully\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploaded Successfully"));
 //BA.debugLineNum = 1538;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1539;BA.debugLine="ToastMessageShow(\"Transaction Uploaded\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Uploaded"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1540;BA.debugLine="If error_trigger = 0 Then";
if (true) break;

case 14:
//if
this.state = 25;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 25;
 //BA.debugLineNum = 1541;BA.debugLine="Dim query As String = \"UPDATE daily_inventory_d";
_query = "UPDATE daily_inventory_disc_table SET status = ?, date_upload = ? , time_upload = ? WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'";
 //BA.debugLineNum = 1542;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"UPLOADED",parent._date,parent._time}));
 //BA.debugLineNum = 1543;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 32;
return;
case 32:
//C
this.state = 25;
;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1545;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1546;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 33;
return;
case 33:
//C
this.state = 19;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1547;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 19:
//if
this.state = 24;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 1548;BA.debugLine="DELETE_DAILY_DISC";
_delete_daily_disc();
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1550;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1551;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = 26;
;
 //BA.debugLineNum = 1554;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 34;
return;
case 34:
//C
this.state = 26;
;
 //BA.debugLineNum = 1555;BA.debugLine="GET_STATUS";
_get_status();
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 1557;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_list() throws Exception{
ResumableSub_LOAD_LIST rsub = new ResumableSub_LOAD_LIST(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_LIST extends BA.ResumableSub {
public ResumableSub_LOAD_LIST(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int _numbercount = 0;
int _row = 0;
int step21;
int limit21;
int step30;
int limit30;
int step43;
int limit43;
int step56;
int limit56;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1003;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1005;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1006;BA.debugLine="LVL_LIST.Background = bg";
parent.mostCurrent._lvl_list.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1007;BA.debugLine="LVL_LIST.Clear";
parent.mostCurrent._lvl_list.Clear();
 //BA.debugLineNum = 1008;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 51;
return;
case 51:
//C
this.state = 1;
;
 //BA.debugLineNum = 1009;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.Typeface = Typeface";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1010;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.TextSize = 20";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 1011;BA.debugLine="LVL_LIST.TwoLinesLayout.label.Height = 8%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1012;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.TextColor = Colors.";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1013;BA.debugLine="LVL_LIST.TwoLinesLayout.Label.Gravity = Gravity.C";
parent.mostCurrent._lvl_list.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 1014;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Typeface = Ty";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 1015;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Top = 6%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 1016;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.TextSize = 14";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTextSize((float) (14));
 //BA.debugLineNum = 1017;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Height = 4%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA));
 //BA.debugLineNum = 1018;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.TextColor = C";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 1019;BA.debugLine="LVL_LIST.TwoLinesLayout.SecondLabel.Gravity = Gra";
parent.mostCurrent._lvl_list.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 1020;BA.debugLine="LVL_LIST.TwoLinesLayout.ItemHeight = 12%y";
parent.mostCurrent._lvl_list.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 1021;BA.debugLine="Dim numbercount As Int";
_numbercount = 0;
 //BA.debugLineNum = 1022;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"' ORDER BY item_number ASC")));
 //BA.debugLineNum = 1023;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 8;
if (parent._cursor2.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1024;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step21 = 1;
limit21 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 52;
if (true) break;

case 52:
//C
this.state = 7;
if ((step21 > 0 && _row <= limit21) || (step21 < 0 && _row >= limit21)) this.state = 6;
if (true) break;

case 53:
//C
this.state = 52;
_row = ((int)(0 + _row + step21)) ;
if (true) break;

case 6:
//C
this.state = 53;
 //BA.debugLineNum = 1025;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 1026;BA.debugLine="numbercount = numbercount + 1";
_numbercount = (int) (_numbercount+1);
 //BA.debugLineNum = 1027;BA.debugLine="LVL_LIST.AddTwoLines2(cursor2.GetString(\"quanti";
parent.mostCurrent._lvl_list.AddTwoLines2(BA.ObjectToCharSequence(parent._cursor2.GetString("quantity")+" "+parent._cursor2.GetString("unit")),BA.ObjectToCharSequence(parent._cursor2.GetString("input_reason")),(Object)(parent._cursor2.GetString("item_number")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 8;
;
 if (true) break;

case 8:
//C
this.state = 9;
;
 //BA.debugLineNum = 1030;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 54;
return;
case 54:
//C
this.state = 9;
;
 //BA.debugLineNum = 1031;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT sum(total_";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT sum(total_pieces) as 'act' FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"' and input_reason IN ('Actual Count','Wrong Count') ORDER BY item_number ASC")));
 //BA.debugLineNum = 1033;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 9:
//if
this.state = 22;
if (parent._cursor3.getRowCount()>0) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 1034;BA.debugLine="For row = 0 To cursor3.RowCount - 1";
if (true) break;

case 12:
//for
this.state = 21;
step30 = 1;
limit30 = (int) (parent._cursor3.getRowCount()-1);
_row = (int) (0) ;
this.state = 55;
if (true) break;

case 55:
//C
this.state = 21;
if ((step30 > 0 && _row <= limit30) || (step30 < 0 && _row >= limit30)) this.state = 14;
if (true) break;

case 56:
//C
this.state = 55;
_row = ((int)(0 + _row + step30)) ;
if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 1035;BA.debugLine="cursor3.Position = row";
parent._cursor3.setPosition(_row);
 //BA.debugLineNum = 1036;BA.debugLine="numbercount = numbercount + 1";
_numbercount = (int) (_numbercount+1);
 //BA.debugLineNum = 1037;BA.debugLine="If cursor3.GetString(\"act\") = Null Or cursor3.G";
if (true) break;

case 15:
//if
this.state = 20;
if (parent._cursor3.GetString("act")== null || (parent._cursor3.GetString("act")).equals("")) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 20;
 //BA.debugLineNum = 1038;BA.debugLine="LABEL_LOAD_ACTUAL.Text = 0";
parent.mostCurrent._label_load_actual.setText(BA.ObjectToCharSequence(0));
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 1040;BA.debugLine="LABEL_LOAD_ACTUAL.Text = Number.Format3(cursor";
parent.mostCurrent._label_load_actual.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble(parent._cursor3.GetString("act"))),(int) (0),(int) (0),(int) (0),".",",","",(int) (0),(int) (15))));
 if (true) break;

case 20:
//C
this.state = 56;
;
 if (true) break;
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
 //BA.debugLineNum = 1044;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 57;
return;
case 57:
//C
this.state = 23;
;
 //BA.debugLineNum = 1045;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT sum(total_";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT sum(total_pieces) as 'add' FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"' and input_reason IN ('Not Prepared - Add','Stock Transfer Not Encoded - Add','Cancelled Not Updated - Add','Already Returned Before Count - Add') ORDER BY item_number ASC")));
 //BA.debugLineNum = 1048;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 23:
//if
this.state = 36;
if (parent._cursor4.getRowCount()>0) { 
this.state = 25;
}if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 1049;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 26:
//for
this.state = 35;
step43 = 1;
limit43 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 58;
if (true) break;

case 58:
//C
this.state = 35;
if ((step43 > 0 && _row <= limit43) || (step43 < 0 && _row >= limit43)) this.state = 28;
if (true) break;

case 59:
//C
this.state = 58;
_row = ((int)(0 + _row + step43)) ;
if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 1050;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 //BA.debugLineNum = 1051;BA.debugLine="numbercount = numbercount + 1";
_numbercount = (int) (_numbercount+1);
 //BA.debugLineNum = 1052;BA.debugLine="If cursor4.GetString(\"add\") =  Null Or cursor4.";
if (true) break;

case 29:
//if
this.state = 34;
if (parent._cursor4.GetString("add")== null || (parent._cursor4.GetString("add")).equals("")) { 
this.state = 31;
}else {
this.state = 33;
}if (true) break;

case 31:
//C
this.state = 34;
 //BA.debugLineNum = 1053;BA.debugLine="LABEL_LOAD_ADD.Text = 0";
parent.mostCurrent._label_load_add.setText(BA.ObjectToCharSequence(0));
 if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 1055;BA.debugLine="LABEL_LOAD_ADD.Text = Number.Format3(cursor4.G";
parent.mostCurrent._label_load_add.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble(parent._cursor4.GetString("add"))),(int) (0),(int) (0),(int) (0),".",",","",(int) (0),(int) (15))));
 if (true) break;

case 34:
//C
this.state = 59;
;
 if (true) break;
if (true) break;

case 35:
//C
this.state = 36;
;
 if (true) break;

case 36:
//C
this.state = 37;
;
 //BA.debugLineNum = 1059;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 60;
return;
case 60:
//C
this.state = 37;
;
 //BA.debugLineNum = 1060;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT sum(total_";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT sum(total_pieces) as 'deduct' FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"' and input_reason IN ('Advance Prepared - Deduct','Stock Transfer Out Not Encoded - Deduct','Over Delivery - Deduct','Cancelled Not Return - Deduct') ORDER BY item_number ASC")));
 //BA.debugLineNum = 1063;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 37:
//if
this.state = 50;
if (parent._cursor5.getRowCount()>0) { 
this.state = 39;
}if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 1064;BA.debugLine="For row = 0 To cursor5.RowCount - 1";
if (true) break;

case 40:
//for
this.state = 49;
step56 = 1;
limit56 = (int) (parent._cursor5.getRowCount()-1);
_row = (int) (0) ;
this.state = 61;
if (true) break;

case 61:
//C
this.state = 49;
if ((step56 > 0 && _row <= limit56) || (step56 < 0 && _row >= limit56)) this.state = 42;
if (true) break;

case 62:
//C
this.state = 61;
_row = ((int)(0 + _row + step56)) ;
if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 1065;BA.debugLine="cursor5.Position = row";
parent._cursor5.setPosition(_row);
 //BA.debugLineNum = 1066;BA.debugLine="numbercount = numbercount + 1";
_numbercount = (int) (_numbercount+1);
 //BA.debugLineNum = 1067;BA.debugLine="If cursor5.GetString(\"deduct\") = Null Or cursor";
if (true) break;

case 43:
//if
this.state = 48;
if (parent._cursor5.GetString("deduct")== null || (parent._cursor5.GetString("deduct")).equals("")) { 
this.state = 45;
}else {
this.state = 47;
}if (true) break;

case 45:
//C
this.state = 48;
 //BA.debugLineNum = 1068;BA.debugLine="LABEL_LOAD_DEDUCT.Text = 0";
parent.mostCurrent._label_load_deduct.setText(BA.ObjectToCharSequence(0));
 if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 1070;BA.debugLine="LABEL_LOAD_DEDUCT.Text = Number.Format3(cursor";
parent.mostCurrent._label_load_deduct.setText(BA.ObjectToCharSequence(parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble(parent._cursor5.GetString("deduct"))),(int) (0),(int) (0),(int) (0),".",",","",(int) (0),(int) (15))));
 if (true) break;

case 48:
//C
this.state = 62;
;
 if (true) break;
if (true) break;

case 49:
//C
this.state = 50;
;
 if (true) break;

case 50:
//C
this.state = -1;
;
 //BA.debugLineNum = 1074;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1075;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_reason() throws Exception{
 //BA.debugLineNum = 856;BA.debugLine="Sub LOAD_REASON";
 //BA.debugLineNum = 857;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"SAVED\" Then";
if ((mostCurrent._label_load_status.getText()).equals("SAVED")) { 
 //BA.debugLineNum = 858;BA.debugLine="CMB_REASON.cmbBox.Clear";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 859;BA.debugLine="CMB_REASON.cmbBox.Add(\"Actual Count\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Actual Count");
 }else {
 //BA.debugLineNum = 861;BA.debugLine="CMB_REASON.cmbBox.Clear";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 862;BA.debugLine="CMB_REASON.cmbBox.Add(\"Not Prepared - Add\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Not Prepared - Add");
 //BA.debugLineNum = 863;BA.debugLine="CMB_REASON.cmbBox.Add(\"Stock Transfer In Not Enc";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Stock Transfer In Not Encoded - Add");
 //BA.debugLineNum = 864;BA.debugLine="CMB_REASON.cmbBox.Add(\"Cancelled Not Updated - A";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Cancelled Not Updated - Add");
 //BA.debugLineNum = 865;BA.debugLine="CMB_REASON.cmbBox.Add(\"Already Returned Before C";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Already Returned Before Count - Add");
 //BA.debugLineNum = 866;BA.debugLine="CMB_REASON.cmbBox.Add(\"Reserved Encoded - Add\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Reserved Encoded - Add");
 //BA.debugLineNum = 867;BA.debugLine="CMB_REASON.cmbBox.Add(\"Short Prepared - Add\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Short Prepared - Add");
 //BA.debugLineNum = 868;BA.debugLine="CMB_REASON.cmbBox.Add(\"Advance Prepared - Deduct";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Advance Prepared - Deduct");
 //BA.debugLineNum = 869;BA.debugLine="CMB_REASON.cmbBox.Add(\"Stock Transfer Out Not En";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Stock Transfer Out Not Encoded - Deduct");
 //BA.debugLineNum = 870;BA.debugLine="CMB_REASON.cmbBox.Add(\"Over Delivery - Deduct\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Over Delivery - Deduct");
 //BA.debugLineNum = 871;BA.debugLine="CMB_REASON.cmbBox.Add(\"Cancelled Not Return - De";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Cancelled Not Return - Deduct");
 //BA.debugLineNum = 872;BA.debugLine="CMB_REASON.cmbBox.Add(\"Over Prepared - Deduct\")";
mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Over Prepared - Deduct");
 };
 //BA.debugLineNum = 874;BA.debugLine="End Sub";
return "";
}
public static String  _load_table_header() throws Exception{
 //BA.debugLineNum = 324;BA.debugLine="Sub LOAD_TABLE_HEADER";
 //BA.debugLineNum = 325;BA.debugLine="NameColumn(0)=TABLE_DAILY_INVENTORY.AddColumn(\"Pr";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_daily_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Variant",mostCurrent._table_daily_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 326;BA.debugLine="NameColumn(1)=TABLE_DAILY_INVENTORY.AddColumn(\"Pr";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_daily_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Description",mostCurrent._table_daily_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 327;BA.debugLine="NameColumn(2)=TABLE_DAILY_INVENTORY.AddColumn(\"Sy";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_daily_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("System Count",mostCurrent._table_daily_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 328;BA.debugLine="NameColumn(3)=TABLE_DAILY_INVENTORY.AddColumn(\"Ad";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._table_daily_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Add Count",mostCurrent._table_daily_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 329;BA.debugLine="NameColumn(4)=TABLE_DAILY_INVENTORY.AddColumn(\"De";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._table_daily_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Deduct Count",mostCurrent._table_daily_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 330;BA.debugLine="NameColumn(5)=TABLE_DAILY_INVENTORY.AddColumn(\"Ac";
mostCurrent._namecolumn[(int) (5)] = mostCurrent._table_daily_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Actual Count",mostCurrent._table_daily_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 331;BA.debugLine="NameColumn(6)=TABLE_DAILY_INVENTORY.AddColumn(\"Va";
mostCurrent._namecolumn[(int) (6)] = mostCurrent._table_daily_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Variance",mostCurrent._table_daily_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 332;BA.debugLine="End Sub";
return "";
}
public static String  _loadtexts() throws Exception{
String _filename = "";
int _iq = 0;
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _reader = null;
 //BA.debugLineNum = 1858;BA.debugLine="Sub LoadTexts";
 //BA.debugLineNum = 1859;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 1860;BA.debugLine="Dim iq As Int";
_iq = 0;
 //BA.debugLineNum = 1864;BA.debugLine="FileName = \"tapecalc_\" & LanguageID & \".txt\"";
_filename = "tapecalc_"+_languageid+".txt";
 //BA.debugLineNum = 1865;BA.debugLine="If File.Exists(ProgPath, FileName) = False Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_progpath,_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1866;BA.debugLine="FileName = \"tapecalc_en.txt\"";
_filename = "tapecalc_en.txt";
 };
 //BA.debugLineNum = 1869;BA.debugLine="Dim Reader As TextReader";
_reader = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1870;BA.debugLine="Reader.Initialize(File.OpenInput(ProgPath, FileNa";
_reader.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_progpath,_filename).getObject()));
 //BA.debugLineNum = 1871;BA.debugLine="iq = 0";
_iq = (int) (0);
 //BA.debugLineNum = 1872;BA.debugLine="Texts(iq) = Reader.ReadLine";
_texts[_iq] = _reader.ReadLine();
 //BA.debugLineNum = 1873;BA.debugLine="Do While Texts(iq) <> Null";
while (_texts[_iq]!= null) {
 //BA.debugLineNum = 1874;BA.debugLine="iq = iq + 1";
_iq = (int) (_iq+1);
 //BA.debugLineNum = 1875;BA.debugLine="Texts(iq) = Reader.ReadLine";
_texts[_iq] = _reader.ReadLine();
 }
;
 //BA.debugLineNum = 1877;BA.debugLine="Reader.Close";
_reader.Close();
 //BA.debugLineNum = 1878;BA.debugLine="End Sub";
return "";
}
public static void  _lvl_list_itemclick(int _position,Object _value) throws Exception{
ResumableSub_LVL_LIST_ItemClick rsub = new ResumableSub_LVL_LIST_ItemClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LVL_LIST_ItemClick extends BA.ResumableSub {
public ResumableSub_LVL_LIST_ItemClick(wingan.app.daily_count parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.daily_count parent;
int _position;
Object _value;
int _row = 0;
int _result = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
String _insert_query = "";
String _query = "";
int step4;
int limit4;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1077;BA.debugLine="If LABEL_LOAD_STATUS.Text <> \"UPLOADED\" Then";
if (true) break;

case 1:
//if
this.state = 36;
if ((parent.mostCurrent._label_load_status.getText()).equals("UPLOADED") == false) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1078;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_description = '"+parent.mostCurrent._label_load_description.getText()+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"' and item_number = '"+BA.ObjectToString(_value)+"' ORDER BY item_number ASC")));
 //BA.debugLineNum = 1079;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 35;
if (parent._cursor3.getRowCount()>0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1080;BA.debugLine="For row = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 34;
step4 = 1;
limit4 = (int) (parent._cursor3.getRowCount()-1);
_row = (int) (0) ;
this.state = 37;
if (true) break;

case 37:
//C
this.state = 34;
if ((step4 > 0 && _row <= limit4) || (step4 < 0 && _row >= limit4)) this.state = 9;
if (true) break;

case 38:
//C
this.state = 37;
_row = ((int)(0 + _row + step4)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1081;BA.debugLine="cursor3.Position = row";
parent._cursor3.setPosition(_row);
 //BA.debugLineNum = 1082;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"SAVED\" Or LABEL_LO";
if (true) break;

case 10:
//if
this.state = 33;
if ((parent.mostCurrent._label_load_status.getText()).equals("SAVED") || (parent.mostCurrent._label_load_status.getText()).equals("FINAL")) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1083;BA.debugLine="Msgbox2Async(\"Item Number : \" & cursor3.GetStr";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Item Number : "+parent._cursor3.GetString("item_number")+anywheresoftware.b4a.keywords.Common.CRLF+"Unit : "+parent._cursor3.GetString("unit")+anywheresoftware.b4a.keywords.Common.CRLF+"Quantity : "+parent._cursor3.GetString("quantity")+anywheresoftware.b4a.keywords.Common.CRLF+"Total Pieces : "+parent._cursor3.GetString("total_pieces")+anywheresoftware.b4a.keywords.Common.CRLF),BA.ObjectToCharSequence("Option"),"EDIT","CANCEL","DELETE",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1088;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 39;
return;
case 39:
//C
this.state = 13;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1089;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 13:
//if
this.state = 32;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 15;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 23;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1091;BA.debugLine="If cursor3.GetString(\"input_reason\") = \"Actu";
if (true) break;

case 16:
//if
this.state = 21;
if ((parent._cursor3.GetString("input_reason")).equals("Actual Count") || (parent._cursor3.GetString("input_reason")).equals("Wrong Count")) { 
this.state = 18;
}else {
this.state = 20;
}if (true) break;

case 18:
//C
this.state = 21;
 //BA.debugLineNum = 1092;BA.debugLine="CMB_REASON.cmbBox.Clear";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 1093;BA.debugLine="CMB_REASON.cmbBox.Add(\"Wrong Count\")";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Wrong Count");
 if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 1095;BA.debugLine="CMB_REASON.SelectedIndex = CMB_REASON.cmbBox";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ (parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor3.GetString("input_reason")));
 if (true) break;

case 21:
//C
this.state = 32;
;
 //BA.debugLineNum = 1097;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 40;
return;
case 40:
//C
this.state = 32;
;
 //BA.debugLineNum = 1098;BA.debugLine="EDITTEXT_QUANTITY.Text = cursor3.GetString(\"q";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("quantity")));
 //BA.debugLineNum = 1099;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 41;
return;
case 41:
//C
this.state = 32;
;
 //BA.debugLineNum = 1100;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Inde";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(parent._cursor3.GetString("unit")));
 //BA.debugLineNum = 1101;BA.debugLine="item_number = cursor3.GetString(\"item_number\"";
parent._item_number = parent._cursor3.GetString("item_number");
 //BA.debugLineNum = 1102;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1103;BA.debugLine="bg.Initialize2(Colors.RGB(0,167,255), 5, 0, C";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (167),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 1104;BA.debugLine="BUTTON_SAVE.Background = bg";
parent.mostCurrent._button_save.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 1105;BA.debugLine="BUTTON_SAVE.Text = \" Edit\"";
parent.mostCurrent._button_save.setText(BA.ObjectToCharSequence(" Edit"));
 //BA.debugLineNum = 1106;BA.debugLine="BUTTON_CANCEL.Visible = True";
parent.mostCurrent._button_cancel.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 1108;BA.debugLine="Msgbox2Async(\"Are you sure you want to delete";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to delete this item?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1109;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 42;
return;
case 42:
//C
this.state = 24;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1110;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 24:
//if
this.state = 31;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 26;
}if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 1111;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"FINAL\" Then";
if (true) break;

case 27:
//if
this.state = 30;
if ((parent.mostCurrent._label_load_status.getText()).equals("FINAL")) { 
this.state = 29;
}if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 1112;BA.debugLine="Dim insert_query As String = \"INSERT INTO da";
_insert_query = "INSERT INTO daily_inventory_disc_table_trail SELECT *,'DELETED' as 'edit_type',? as 'edit_by',? as 'date',? as 'time' from daily_inventory_disc_table WHERE group_id = ? AND inventory_date = ? AND item_number = ?";
 //BA.debugLineNum = 1113;BA.debugLine="connection.ExecNonQuery2(insert_query, Array";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent.mostCurrent._label_load_inv_date.getText(),BA.ObjectToString(_value)}));
 if (true) break;

case 30:
//C
this.state = 31;
;
 //BA.debugLineNum = 1115;BA.debugLine="Dim query As String = \"DELETE from daily_inv";
_query = "DELETE from daily_inventory_disc_table WHERE group_id = ? AND inventory_date = ? AND item_number = ?";
 //BA.debugLineNum = 1116;BA.debugLine="connection.ExecNonQuery2(query,Array As Stri";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent.mostCurrent._label_load_inv_date.getText(),BA.ObjectToString(_value)}));
 //BA.debugLineNum = 1117;BA.debugLine="ToastMessageShow(\"Deleted Successfully\", Tru";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleted Successfully"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1118;BA.debugLine="GET_DAILY";
_get_daily();
 //BA.debugLineNum = 1119;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 43;
return;
case 43:
//C
this.state = 31;
;
 //BA.debugLineNum = 1120;BA.debugLine="LOAD_LIST";
_load_list();
 if (true) break;

case 31:
//C
this.state = 32;
;
 if (true) break;

case 32:
//C
this.state = 33;
;
 if (true) break;

case 33:
//C
this.state = 38;
;
 if (true) break;
if (true) break;

case 34:
//C
this.state = 35;
;
 if (true) break;

case 35:
//C
this.state = 36;
;
 if (true) break;

case 36:
//C
this.state = -1;
;
 //BA.debugLineNum = 1128;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _oncal() throws Exception{
 //BA.debugLineNum = 1576;BA.debugLine="Sub oncal";
 //BA.debugLineNum = 1577;BA.debugLine="LoadTexts";
_loadtexts();
 //BA.debugLineNum = 1579;BA.debugLine="Scale_Calc.SetRate(0.6)";
mostCurrent._scale_calc._setrate /*String*/ (mostCurrent.activityBA,0.6);
 //BA.debugLineNum = 1580;BA.debugLine="ScaleAuto = Scale_Calc.GetScaleDS";
_scaleauto = mostCurrent._scale_calc._getscaleds /*double*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1597;BA.debugLine="lblPaperRoll.Initialize(\"lblPaperRoll\")";
mostCurrent._lblpaperroll.Initialize(mostCurrent.activityBA,"lblPaperRoll");
 //BA.debugLineNum = 1598;BA.debugLine="scvPaperRoll.Panel.AddView(lblPaperRoll, 0, 0, 10";
mostCurrent._scvpaperroll.getPanel().AddView((android.view.View)(mostCurrent._lblpaperroll.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1599;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1600;BA.debugLine="lblPaperRoll.TextSize = 22 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (22*_scaleauto));
 //BA.debugLineNum = 1601;BA.debugLine="lblPaperRoll.Color = Colors.White";
mostCurrent._lblpaperroll.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1602;BA.debugLine="lblPaperRoll.TextColor = Colors.Black";
mostCurrent._lblpaperroll.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1618;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 318;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 319;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 320;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 321;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _operation(String _op) throws Exception{
 //BA.debugLineNum = 1755;BA.debugLine="Sub Operation(Op As String)";
 //BA.debugLineNum = 1756;BA.debugLine="Select Op";
switch (BA.switchObjectToInt(_op,"a","b","c","d","g","s","x","y")) {
case 0: {
 //BA.debugLineNum = 1758;BA.debugLine="Txt = Txt & CRLF & \"+ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"+ ";
 break; }
case 1: {
 //BA.debugLineNum = 1760;BA.debugLine="Txt = Txt & CRLF & \"- \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"- ";
 break; }
case 2: {
 //BA.debugLineNum = 1762;BA.debugLine="Txt = Txt & CRLF & \"× \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"× ";
 break; }
case 3: {
 //BA.debugLineNum = 1764;BA.debugLine="Txt = Txt & CRLF & \"/ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"/ ";
 break; }
case 4: {
 //BA.debugLineNum = 1766;BA.debugLine="Txt = Txt & CRLF & \"x2 \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"x2 ";
 break; }
case 5: {
 //BA.debugLineNum = 1768;BA.debugLine="Txt = Txt & CRLF & \"√ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"√ ";
 break; }
case 6: {
 //BA.debugLineNum = 1770;BA.debugLine="Txt = Txt & CRLF & \"1/x \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"1/x ";
 break; }
case 7: {
 //BA.debugLineNum = 1772;BA.debugLine="Txt = Txt & CRLF & \"% \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"% ";
 break; }
}
;
 //BA.debugLineNum = 1774;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_calcu_click() throws Exception{
 //BA.debugLineNum = 1899;BA.debugLine="Sub PANEL_BG_CALCU_Click";
 //BA.debugLineNum = 1900;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1901;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_input_click() throws Exception{
 //BA.debugLineNum = 1881;BA.debugLine="Sub PANEL_BG_INPUT_Click";
 //BA.debugLineNum = 1882;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1883;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_msgbox_click() throws Exception{
 //BA.debugLineNum = 1885;BA.debugLine="Sub PANEL_BG_MSGBOX_Click";
 //BA.debugLineNum = 1886;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1887;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 11;BA.debugLine="Dim connection As SQL";
_connection = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 12;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim cursor2 As Cursor";
_cursor2 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim cursor3 As Cursor";
_cursor3 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim cursor4 As Cursor";
_cursor4 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim cursor5 As Cursor";
_cursor5 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim cursor6 As Cursor";
_cursor6 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim cursor7 As Cursor";
_cursor7 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim cursor8 As Cursor";
_cursor8 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 23;BA.debugLine="Public inventory_date As String";
_inventory_date = "";
 //BA.debugLineNum = 24;BA.debugLine="Public status As String";
_status = "";
 //BA.debugLineNum = 25;BA.debugLine="Public date As String";
_date = "";
 //BA.debugLineNum = 26;BA.debugLine="Public time As String";
_time = "";
 //BA.debugLineNum = 29;BA.debugLine="Dim ProgPath = File.DirRootExternal & \"/TapeCalc\"";
_progpath = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/TapeCalc";
 //BA.debugLineNum = 30;BA.debugLine="Dim ScaleAuto As Double";
_scaleauto = 0;
 //BA.debugLineNum = 31;BA.debugLine="Dim Texts(8) As String";
_texts = new String[(int) (8)];
java.util.Arrays.fill(_texts,"");
 //BA.debugLineNum = 32;BA.debugLine="Dim LanguageID As String";
_languageid = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim sVal = \"\" As String";
_sval = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim Val = 0 As Double";
_val = 0;
 //BA.debugLineNum = 36;BA.debugLine="Dim Op0 = \"\" As String";
_op0 = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim Result1 = 0 As Double";
_result1 = 0;
 //BA.debugLineNum = 38;BA.debugLine="Dim Txt = \"\" As String";
_txt = "";
 //BA.debugLineNum = 39;BA.debugLine="Dim New1 = 0 As Int";
_new1 = (int) (0);
 //BA.debugLineNum = 42;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 43;BA.debugLine="Dim AStream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 44;BA.debugLine="Dim Ts As Timer";
_ts = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 47;BA.debugLine="Dim principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim product_id As String";
_product_id = "";
 //BA.debugLineNum = 49;BA.debugLine="Dim reason As String";
_reason = "";
 //BA.debugLineNum = 50;BA.debugLine="Dim input_type As String";
_input_type = "";
 //BA.debugLineNum = 51;BA.debugLine="Dim caseper As String";
_caseper = "";
 //BA.debugLineNum = 52;BA.debugLine="Dim pcsper As String";
_pcsper = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim dozper As String";
_dozper = "";
 //BA.debugLineNum = 54;BA.debugLine="Dim boxper As String";
_boxper = "";
 //BA.debugLineNum = 55;BA.debugLine="Dim bagper As String";
_bagper = "";
 //BA.debugLineNum = 56;BA.debugLine="Dim packper As String";
_packper = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim total_pieces As String";
_total_pieces = "";
 //BA.debugLineNum = 58;BA.debugLine="Dim scan_code As String";
_scan_code = "";
 //BA.debugLineNum = 59;BA.debugLine="Dim status_trigger As String";
_status_trigger = "";
 //BA.debugLineNum = 60;BA.debugLine="Dim error_trigger As String";
_error_trigger = "";
 //BA.debugLineNum = 62;BA.debugLine="Dim item_number As String";
_item_number = "";
 //BA.debugLineNum = 64;BA.debugLine="Dim page As String";
_page = "";
 //BA.debugLineNum = 66;BA.debugLine="Private cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private controlBitmap As Bitmap";
_controlbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static String  _serial_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 656;BA.debugLine="Sub Serial_Connected (success As Boolean)";
 //BA.debugLineNum = 657;BA.debugLine="If success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 658;BA.debugLine="Log(\"Scanner is now connected. Waiting for data.";
anywheresoftware.b4a.keywords.Common.LogImpl("758195970","Scanner is now connected. Waiting for data...",0);
 //BA.debugLineNum = 659;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Connected\"";
mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Connected"));
 //BA.debugLineNum = 660;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Green";
mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 661;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 662;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 663;BA.debugLine="ToastMessageShow(\"Scanner Connected\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner Connected"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 664;BA.debugLine="AStream.Initialize(serial1.InputStream, serial1.";
_astream.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"AStream");
 //BA.debugLineNum = 665;BA.debugLine="ScannerOnceConnected=True";
_scanneronceconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 666;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 668;BA.debugLine="If ScannerOnceConnected=False Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 669;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Disconnected\"";
mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Disconnected"));
 //BA.debugLineNum = 670;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Red";
mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 671;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 672;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 673;BA.debugLine="ToastMessageShow(\"Scanner is off, please turn o";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner is off, please turn on"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 674;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 }else {
 //BA.debugLineNum = 676;BA.debugLine="Log(\"Still waiting for the scanner to reconnect";
anywheresoftware.b4a.keywords.Common.LogImpl("758195988","Still waiting for the scanner to reconnect: "+mostCurrent._scannermacaddress,0);
 //BA.debugLineNum = 677;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 678;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 679;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 680;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Connecting...\"";
mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Connecting..."));
 //BA.debugLineNum = 681;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Blue";
mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 };
 };
 //BA.debugLineNum = 684;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 246;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 247;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 248;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 249;BA.debugLine="Dim IN As Int";
_in = 0;
 //BA.debugLineNum = 250;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 251;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 252;BA.debugLine="IN = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 253;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 254;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 255;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 256;BA.debugLine="End Sub";
return "";
}
public static void  _showpaireddevices() throws Exception{
ResumableSub_ShowPairedDevices rsub = new ResumableSub_ShowPairedDevices(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowPairedDevices extends BA.ResumableSub {
public ResumableSub_ShowPairedDevices(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
String _mac = "";
anywheresoftware.b4a.objects.collections.Map _paireddevices = null;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _iq = 0;
int _result = 0;
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
 //BA.debugLineNum = 629;BA.debugLine="Dim mac As String";
_mac = "";
 //BA.debugLineNum = 630;BA.debugLine="Dim PairedDevices As Map";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 631;BA.debugLine="PairedDevices = serial1.GetPairedDevices";
_paireddevices = parent._serial1.GetPairedDevices();
 //BA.debugLineNum = 632;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 633;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 634;BA.debugLine="For Iq = 0 To PairedDevices.Size - 1";
if (true) break;

case 1:
//for
this.state = 4;
step6 = 1;
limit6 = (int) (_paireddevices.getSize()-1);
_iq = (int) (0) ;
this.state = 18;
if (true) break;

case 18:
//C
this.state = 4;
if ((step6 > 0 && _iq <= limit6) || (step6 < 0 && _iq >= limit6)) this.state = 3;
if (true) break;

case 19:
//C
this.state = 18;
_iq = ((int)(0 + _iq + step6)) ;
if (true) break;

case 3:
//C
this.state = 19;
 //BA.debugLineNum = 635;BA.debugLine="mac = PairedDevices.Get(PairedDevices.GetKeyAt(I";
_mac = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_iq)));
 //BA.debugLineNum = 636;BA.debugLine="ls.add(\"Scanner \" & mac.SubString2 (mac.Length -";
_ls.Add((Object)("Scanner "+_mac.substring((int) (_mac.length()-4),_mac.length())));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 638;BA.debugLine="If ls.Size=0 Then";

case 4:
//if
this.state = 7;
if (_ls.getSize()==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 639;BA.debugLine="ls.Add(\"No device(s) found...\")";
_ls.Add((Object)("No device(s) found..."));
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 641;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 642;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 20;
return;
case 20:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 643;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 8:
//if
this.state = 17;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 10;
}if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 644;BA.debugLine="If ls.Get(Result)=\"No device(s) found...\" Then";
if (true) break;

case 11:
//if
this.state = 16;
if ((_ls.Get(_result)).equals((Object)("No device(s) found..."))) { 
this.state = 13;
}else {
this.state = 15;
}if (true) break;

case 13:
//C
this.state = 16;
 //BA.debugLineNum = 645;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 647;BA.debugLine="ScannerMacAddress=PairedDevices.Get(PairedDevic";
parent.mostCurrent._scannermacaddress = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))));
 //BA.debugLineNum = 649;BA.debugLine="Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (R";
anywheresoftware.b4a.keywords.Common.LogImpl("758130453",BA.ObjectToString(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))),0);
 //BA.debugLineNum = 650;BA.debugLine="serial1.Connect(ScannerMacAddress)";
parent._serial1.Connect(processBA,parent.mostCurrent._scannermacaddress);
 //BA.debugLineNum = 651;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 652;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 if (true) break;

case 16:
//C
this.state = 17;
;
 if (true) break;

case 17:
//C
this.state = -1;
;
 //BA.debugLineNum = 655;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_daily_inventory_cellclicked(String _columnid,long _rowid) throws Exception{
 //BA.debugLineNum = 449;BA.debugLine="Sub TABLE_DAILY_INVENTORY_CellClicked (ColumnId As";
 //BA.debugLineNum = 450;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 451;BA.debugLine="Log(ColumnId & RowId)";
anywheresoftware.b4a.keywords.Common.LogImpl("757933826",_columnid+BA.NumberToString(_rowid),0);
 //BA.debugLineNum = 452;BA.debugLine="End Sub";
return "";
}
public static void  _table_daily_inventory_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_DAILY_INVENTORY_CellLongClicked rsub = new ResumableSub_TABLE_DAILY_INVENTORY_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_DAILY_INVENTORY_CellLongClicked extends BA.ResumableSub {
public ResumableSub_TABLE_DAILY_INVENTORY_CellLongClicked(wingan.app.daily_count parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.daily_count parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
int _qrow = 0;
int _row = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int step22;
int limit22;
int step57;
int limit57;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 454;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 455;BA.debugLine="Log(ColumnId & RowId)";
anywheresoftware.b4a.keywords.Common.LogImpl("757999362",_columnid+BA.NumberToString(_rowid),0);
 //BA.debugLineNum = 457;BA.debugLine="Dim RowData As Map = TABLE_DAILY_INVENTORY.GetRow";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_daily_inventory._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 458;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 459;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 461;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"SAVED\" Then";
if (true) break;

case 1:
//if
this.state = 8;
if ((parent.mostCurrent._label_load_status.getText()).equals("SAVED")) { 
this.state = 3;
}else if((parent.mostCurrent._label_load_status.getText()).equals("FINAL")) { 
this.state = 5;
}else {
this.state = 7;
}if (true) break;

case 3:
//C
this.state = 8;
 //BA.debugLineNum = 462;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED\")";
_ls.Add((Object)("BARCODE NOT REGISTERED"));
 //BA.debugLineNum = 463;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 464;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 465;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 466;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 if (true) break;

case 5:
//C
this.state = 8;
 //BA.debugLineNum = 468;BA.debugLine="ls.Add(\"RECONCIALATION\")";
_ls.Add((Object)("RECONCIALATION"));
 if (true) break;

case 7:
//C
this.state = 8;
 //BA.debugLineNum = 470;BA.debugLine="ls.Add(\"VIEWING\")";
_ls.Add((Object)("VIEWING"));
 if (true) break;

case 8:
//C
this.state = 9;
;
 //BA.debugLineNum = 472;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 'sh";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 473;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 56;
return;
case 56:
//C
this.state = 9;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 474;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 9:
//if
this.state = 55;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 476;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+BA.ObjectToString(_rowdata.Get((Object)("Product Description")))+"'")));
 //BA.debugLineNum = 477;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 12:
//if
this.state = 38;
if (parent._cursor3.getRowCount()>0) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 478;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 15:
//for
this.state = 37;
step22 = 1;
limit22 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 57;
if (true) break;

case 57:
//C
this.state = 37;
if ((step22 > 0 && _qrow <= limit22) || (step22 < 0 && _qrow >= limit22)) this.state = 17;
if (true) break;

case 58:
//C
this.state = 57;
_qrow = ((int)(0 + _qrow + step22)) ;
if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 479;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 480;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 481;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 482;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 483;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 485;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 486;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
if (true) break;

case 18:
//if
this.state = 21;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 20;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 487;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 489;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 21:
//if
this.state = 24;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 23;
}if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 490;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 492;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 24:
//if
this.state = 27;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 26;
}if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 493;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 495;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 27:
//if
this.state = 30;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 29;
}if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 496;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 498;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 30:
//if
this.state = 33;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 32;
}if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 499;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 501;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 33:
//if
this.state = 36;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 35;
}if (true) break;

case 35:
//C
this.state = 36;
 //BA.debugLineNum = 502;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 36:
//C
this.state = 58;
;
 //BA.debugLineNum = 505;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 506;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 507;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 508;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 509;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 510;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 if (true) break;
if (true) break;

case 37:
//C
this.state = 38;
;
 if (true) break;

case 38:
//C
this.state = 39;
;
 //BA.debugLineNum = 514;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principal";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 515;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 39:
//if
this.state = 46;
if (parent._cursor6.getRowCount()>0) { 
this.state = 41;
}if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 516;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 42:
//for
this.state = 45;
step57 = 1;
limit57 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 59;
if (true) break;

case 59:
//C
this.state = 45;
if ((step57 > 0 && _row <= limit57) || (step57 < 0 && _row >= limit57)) this.state = 44;
if (true) break;

case 60:
//C
this.state = 59;
_row = ((int)(0 + _row + step57)) ;
if (true) break;

case 44:
//C
this.state = 60;
 //BA.debugLineNum = 517;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 518;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring(";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 45:
//C
this.state = 46;
;
 if (true) break;

case 46:
//C
this.state = 47;
;
 //BA.debugLineNum = 521;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 522;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 523;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 524;BA.debugLine="reason = ls.Get(Result2)";
parent._reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 525;BA.debugLine="input_type = \"MANUAL\"";
parent._input_type = "MANUAL";
 //BA.debugLineNum = 526;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
 //BA.debugLineNum = 527;BA.debugLine="PANEL_BG_INPUT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_input.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 528;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 61;
return;
case 61:
//C
this.state = 47;
;
 //BA.debugLineNum = 529;BA.debugLine="If LABEL_LOAD_STATUS.Text = \"SAVED\" Then";
if (true) break;

case 47:
//if
this.state = 54;
if ((parent.mostCurrent._label_load_status.getText()).equals("SAVED")) { 
this.state = 49;
}else if((parent.mostCurrent._label_load_status.getText()).equals("FINAL")) { 
this.state = 51;
}else {
this.state = 53;
}if (true) break;

case 49:
//C
this.state = 54;
 //BA.debugLineNum = 530;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 531;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 62;
return;
case 62:
//C
this.state = 54;
;
 //BA.debugLineNum = 532;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 533;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 63;
return;
case 63:
//C
this.state = 54;
;
 //BA.debugLineNum = 534;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 535;BA.debugLine="BUTTON_SAVE.Visible = True";
parent.mostCurrent._button_save.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 536;BA.debugLine="CMB_REASON.cmbBox.Enabled = True";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 537;BA.debugLine="CMB_UNIT.cmbBox.Enabled = True";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 538;BA.debugLine="EDITTEXT_QUANTITY.Enabled = True";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 539;BA.debugLine="CMB_UNIT.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (0));
 //BA.debugLineNum = 540;BA.debugLine="CMB_REASON.SelectedIndex = 0";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (0));
 if (true) break;

case 51:
//C
this.state = 54;
 //BA.debugLineNum = 542;BA.debugLine="LOAD_REASON";
_load_reason();
 //BA.debugLineNum = 543;BA.debugLine="OpenSpinner(CMB_REASON.cmbBox)";
_openspinner(parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 544;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 64;
return;
case 64:
//C
this.state = 54;
;
 //BA.debugLineNum = 545;BA.debugLine="CMB_REASON.SelectedIndex = -1";
parent.mostCurrent._cmb_reason._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 546;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 65;
return;
case 65:
//C
this.state = 54;
;
 //BA.debugLineNum = 547;BA.debugLine="BUTTON_SAVE.Visible = True";
parent.mostCurrent._button_save.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 548;BA.debugLine="CMB_REASON.cmbBox.Enabled = True";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 549;BA.debugLine="CMB_UNIT.cmbBox.Enabled = True";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 550;BA.debugLine="EDITTEXT_QUANTITY.Enabled = True";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 551;BA.debugLine="CMB_UNIT.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (0));
 if (true) break;

case 53:
//C
this.state = 54;
 //BA.debugLineNum = 553;BA.debugLine="BUTTON_SAVE.Visible = False";
parent.mostCurrent._button_save.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 554;BA.debugLine="CMB_REASON.cmbBox.Enabled = False";
parent.mostCurrent._cmb_reason._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 555;BA.debugLine="CMB_UNIT.cmbBox.Enabled = False";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 556;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 54:
//C
this.state = 55;
;
 //BA.debugLineNum = 558;BA.debugLine="LOAD_LIST";
_load_list();
 //BA.debugLineNum = 559;BA.debugLine="CLEAR";
_clear();
 if (true) break;

case 55:
//C
this.state = -1;
;
 //BA.debugLineNum = 562;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _table_daily_inventory_dataupdated() throws Exception{
ResumableSub_TABLE_DAILY_INVENTORY_DataUpdated rsub = new ResumableSub_TABLE_DAILY_INVENTORY_DataUpdated(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_DAILY_INVENTORY_DataUpdated extends BA.ResumableSub {
public ResumableSub_TABLE_DAILY_INVENTORY_DataUpdated(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
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
Object[] group2;
int index2;
int groupLen2;
int step4;
int limit4;
int step14;
int limit14;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 408;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 409;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
if (true) break;

case 1:
//for
this.state = 11;
group2 = new Object[]{(Object)(parent.mostCurrent._namecolumn[(int) (0)]),(Object)(parent.mostCurrent._namecolumn[(int) (1)]),(Object)(parent.mostCurrent._namecolumn[(int) (2)]),(Object)(parent.mostCurrent._namecolumn[(int) (3)]),(Object)(parent.mostCurrent._namecolumn[(int) (4)]),(Object)(parent.mostCurrent._namecolumn[(int) (5)]),(Object)(parent.mostCurrent._namecolumn[(int) (6)])};
index2 = 0;
groupLen2 = group2.length;
this.state = 30;
if (true) break;

case 30:
//C
this.state = 11;
if (index2 < groupLen2) {
this.state = 3;
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);}
if (true) break;

case 31:
//C
this.state = 30;
index2++;
if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 410;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 411;BA.debugLine="For i = 0 To TABLE_DAILY_INVENTORY.VisibleRowIds";
if (true) break;

case 4:
//for
this.state = 7;
step4 = 1;
limit4 = parent.mostCurrent._table_daily_inventory._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
this.state = 32;
if (true) break;

case 32:
//C
this.state = 7;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 33:
//C
this.state = 32;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 33;
 //BA.debugLineNum = 412;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 413;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 414;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,parent.mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 416;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";

case 7:
//if
this.state = 10;
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 417;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 418;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 if (true) break;

case 10:
//C
this.state = 31;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 421;BA.debugLine="For i = 0 To TABLE_DAILY_INVENTORY.VisibleRowIds.";

case 11:
//for
this.state = 26;
step14 = 1;
limit14 = (int) (parent.mostCurrent._table_daily_inventory._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
this.state = 34;
if (true) break;

case 34:
//C
this.state = 26;
if ((step14 > 0 && _i <= limit14) || (step14 < 0 && _i >= limit14)) this.state = 13;
if (true) break;

case 35:
//C
this.state = 34;
_i = ((int)(0 + _i + step14)) ;
if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 422;BA.debugLine="Dim RowId As Long = TABLE_DAILY_INVENTORY.Visibl";
_rowid = BA.ObjectToLongNumber(parent.mostCurrent._table_daily_inventory._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 423;BA.debugLine="If RowId > 0 Then";
if (true) break;

case 14:
//if
this.state = 25;
if (_rowid>0) { 
this.state = 16;
}if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 424;BA.debugLine="Dim pnl1 As B4XView = NameColumn(6).CellsLayout";
_pnl1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._namecolumn[(int) (6)].CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 425;BA.debugLine="Dim row As Map = TABLE_DAILY_INVENTORY.GetRow(R";
_row = new anywheresoftware.b4a.objects.collections.Map();
_row = parent.mostCurrent._table_daily_inventory._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 426;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 427;BA.debugLine="Dim OtherColumnValue As String = row.Get(NameCo";
_othercolumnvalue = BA.ObjectToString(_row.Get((Object)(parent.mostCurrent._namecolumn[(int) (6)].Id /*String*/ )));
 //BA.debugLineNum = 428;BA.debugLine="Log(row.Get(NameColumn(6).Id))";
anywheresoftware.b4a.keywords.Common.LogImpl("757868309",BA.ObjectToString(_row.Get((Object)(parent.mostCurrent._namecolumn[(int) (6)].Id /*String*/ ))),0);
 //BA.debugLineNum = 429;BA.debugLine="If OtherColumnValue.SubString2(0,1) = \"-\" Then";
if (true) break;

case 17:
//if
this.state = 24;
if ((_othercolumnvalue.substring((int) (0),(int) (1))).equals("-")) { 
this.state = 19;
}else if((_othercolumnvalue.substring((int) (0),(int) (1))).equals(BA.NumberToString(0))) { 
this.state = 21;
}else {
this.state = 23;
}if (true) break;

case 19:
//C
this.state = 24;
 //BA.debugLineNum = 430;BA.debugLine="clr = xui.Color_Red";
_clr = parent.mostCurrent._xui.Color_Red;
 if (true) break;

case 21:
//C
this.state = 24;
 //BA.debugLineNum = 432;BA.debugLine="clr = xui.Color_Green";
_clr = parent.mostCurrent._xui.Color_Green;
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 434;BA.debugLine="clr = xui.Color_Blue";
_clr = parent.mostCurrent._xui.Color_Blue;
 if (true) break;

case 24:
//C
this.state = 25;
;
 //BA.debugLineNum = 436;BA.debugLine="pnl1.GetView(0).Color = clr";
_pnl1.GetView((int) (0)).setColor(_clr);
 if (true) break;

case 25:
//C
this.state = 35;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 440;BA.debugLine="If ShouldRefresh Then";

case 26:
//if
this.state = 29;
if (_shouldrefresh) { 
this.state = 28;
}if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 441;BA.debugLine="TABLE_DAILY_INVENTORY.Refresh";
parent.mostCurrent._table_daily_inventory._refresh /*String*/ ();
 //BA.debugLineNum = 442;BA.debugLine="XSelections.Clear";
parent.mostCurrent._xselections._clear /*String*/ ();
 //BA.debugLineNum = 443;BA.debugLine="XSelections.Refresh";
parent.mostCurrent._xselections._refresh /*String*/ ();
 if (true) break;

case 29:
//C
this.state = -1;
;
 //BA.debugLineNum = 445;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 36;
return;
case 36:
//C
this.state = -1;
;
 //BA.debugLineNum = 446;BA.debugLine="BUTTON_NEXT.Enabled = TABLE_DAILY_INVENTORY.lblNe";
parent.mostCurrent._button_next.setEnabled(BA.ObjectToBoolean(parent.mostCurrent._table_daily_inventory._lblnext /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getTag()));
 //BA.debugLineNum = 447;BA.debugLine="BUTTON_PREV.Enabled = TABLE_DAILY_INVENTORY.lblBa";
parent.mostCurrent._button_prev.setEnabled(BA.ObjectToBoolean(parent.mostCurrent._table_daily_inventory._lblback /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getTag()));
 //BA.debugLineNum = 448;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 841;BA.debugLine="Sub Timer_Tick";
 //BA.debugLineNum = 842;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 843;BA.debugLine="serial1.Connect(ScannerMacAddress)";
_serial1.Connect(processBA,mostCurrent._scannermacaddress);
 //BA.debugLineNum = 844;BA.debugLine="Log (\"Trying to connect...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("758458115","Trying to connect...",0);
 //BA.debugLineNum = 845;BA.debugLine="LABEL_BLUETOOTH_STATUS.Text = \" Connecting...\"";
mostCurrent._label_bluetooth_status.setText(BA.ObjectToCharSequence(" Connecting..."));
 //BA.debugLineNum = 846;BA.debugLine="LABEL_BLUETOOTH_STATUS.Color = Colors.Blue";
mostCurrent._label_bluetooth_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 847;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 848;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 849;BA.debugLine="End Sub";
return "";
}
public static void  _update_final() throws Exception{
ResumableSub_UPDATE_FINAL rsub = new ResumableSub_UPDATE_FINAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_FINAL extends BA.ResumableSub {
public ResumableSub_UPDATE_FINAL(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
int _trigger = 0;
int _rows = 0;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
int _rowq = 0;
String _query = "";
String _transaction_number = "";
int _i = 0;
int _result = 0;
int step9;
int limit9;
anywheresoftware.b4a.BA.IterableList group19;
int index19;
int groupLen19;
int step25;
int limit25;
int step34;
int limit34;
int step52;
int limit52;
int step61;
int limit61;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1171;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 1172;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1173;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 1174;BA.debugLine="LABEL_HEADER_TEXT.Text = \"Downloading Data\"";
parent.mostCurrent._label_header_text.setText(BA.ObjectToCharSequence("Downloading Data"));
 //BA.debugLineNum = 1175;BA.debugLine="LABEL_MSGBOX2.Text = \"Fetching Data...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Fetching Data..."));
 //BA.debugLineNum = 1176;BA.debugLine="LABEL_MSGBOX1.Text = \"Finalizing system count, Pl";
parent.mostCurrent._label_msgbox1.setText(BA.ObjectToCharSequence("Finalizing system count, Please wait..."));
 //BA.debugLineNum = 1177;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"'")));
 //BA.debugLineNum = 1178;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 75;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1179;BA.debugLine="For rows = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 63;
step9 = 1;
limit9 = (int) (parent._cursor3.getRowCount()-1);
_rows = (int) (0) ;
this.state = 76;
if (true) break;

case 76:
//C
this.state = 63;
if ((step9 > 0 && _rows <= limit9) || (step9 < 0 && _rows >= limit9)) this.state = 6;
if (true) break;

case 77:
//C
this.state = 76;
_rows = ((int)(0 + _rows + step9)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 1180;BA.debugLine="cursor3.Position = rows";
parent._cursor3.setPosition(_rows);
 //BA.debugLineNum = 1181;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 1182;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_sy";
_cmd = _createcommand("select_system_count",(Object[])(new String[]{parent._cursor3.GetString("product_id")}));
 //BA.debugLineNum = 1183;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDo";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 78;
return;
case 78:
//C
this.state = 7;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1184;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 79;
return;
case 79:
//C
this.state = 7;
;
 //BA.debugLineNum = 1185;BA.debugLine="If jr.Success Then";
if (true) break;

case 7:
//if
this.state = 62;
if (_jr._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 61;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 1186;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 1187;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 80;
return;
case 80:
//C
this.state = 10;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 1188;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 10:
//if
this.state = 59;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 12;
}else {
this.state = 38;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 1189;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 13:
//for
this.state = 36;
group19 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index19 = 0;
groupLen19 = group19.getSize();
this.state = 81;
if (true) break;

case 81:
//C
this.state = 36;
if (index19 < groupLen19) {
this.state = 15;
_row = (Object[])(group19.Get(index19));}
if (true) break;

case 82:
//C
this.state = 81;
index19++;
if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 1190;BA.debugLine="LABEL_MSGBOX2.textColor = Colors.Black";
parent.mostCurrent._label_msgbox2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1191;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating: \" & cursor3.";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating: "+parent._cursor3.GetString("product_description")));
 //BA.debugLineNum = 1192;BA.debugLine="Sleep(200)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (200));
this.state = 83;
return;
case 83:
//C
this.state = 16;
;
 //BA.debugLineNum = 1193;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FRO";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_id = '"+parent._cursor3.GetString("product_id")+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"' GROUP BY product_id")));
 //BA.debugLineNum = 1194;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 16:
//if
this.state = 35;
if (parent._cursor2.getRowCount()>0) { 
this.state = 18;
}else {
this.state = 24;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 1195;BA.debugLine="For rowq = 0 To cursor2.RowCount - 1";
if (true) break;

case 19:
//for
this.state = 22;
step25 = 1;
limit25 = (int) (parent._cursor2.getRowCount()-1);
_rowq = (int) (0) ;
this.state = 84;
if (true) break;

case 84:
//C
this.state = 22;
if ((step25 > 0 && _rowq <= limit25) || (step25 < 0 && _rowq >= limit25)) this.state = 21;
if (true) break;

case 85:
//C
this.state = 84;
_rowq = ((int)(0 + _rowq + step25)) ;
if (true) break;

case 21:
//C
this.state = 85;
 //BA.debugLineNum = 1196;BA.debugLine="cursor2.Position = rowq";
parent._cursor2.setPosition(_rowq);
 //BA.debugLineNum = 1197;BA.debugLine="Dim query As String = \"UPDATE daily_invent";
_query = "UPDATE daily_inventory_disc_table SET system_count = ?, date_final = ?, time_final = ? WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_id = '"+parent._cursor3.GetString("product_id")+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'";
 //BA.debugLineNum = 1198;BA.debugLine="connection.ExecNonQuery2(query,Array As St";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("total_pcs"))))]),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 1199;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 86;
return;
case 86:
//C
this.state = 85;
;
 if (true) break;
if (true) break;

case 22:
//C
this.state = 35;
;
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 1202;BA.debugLine="Dim transaction_number As String";
_transaction_number = "";
 //BA.debugLineNum = 1203;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT MAX(";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT MAX(CAST(item_number as INT)) as item_number from daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'")));
 //BA.debugLineNum = 1204;BA.debugLine="For i = 0 To cursor5.RowCount - 1";
if (true) break;

case 25:
//for
this.state = 34;
step34 = 1;
limit34 = (int) (parent._cursor5.getRowCount()-1);
_i = (int) (0) ;
this.state = 87;
if (true) break;

case 87:
//C
this.state = 34;
if ((step34 > 0 && _i <= limit34) || (step34 < 0 && _i >= limit34)) this.state = 27;
if (true) break;

case 88:
//C
this.state = 87;
_i = ((int)(0 + _i + step34)) ;
if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 1205;BA.debugLine="cursor5.Position = i";
parent._cursor5.setPosition(_i);
 //BA.debugLineNum = 1206;BA.debugLine="If cursor5.GetString(\"item_number\") = Null";
if (true) break;

case 28:
//if
this.state = 33;
if (parent._cursor5.GetString("item_number")== null || (parent._cursor5.GetString("item_number")).equals("")) { 
this.state = 30;
}else {
this.state = 32;
}if (true) break;

case 30:
//C
this.state = 33;
 //BA.debugLineNum = 1207;BA.debugLine="transaction_number = 1";
_transaction_number = BA.NumberToString(1);
 if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 1209;BA.debugLine="transaction_number = cursor5.GetString(\"i";
_transaction_number = BA.NumberToString((double)(Double.parseDouble(parent._cursor5.GetString("item_number")))+1);
 if (true) break;

case 33:
//C
this.state = 88;
;
 if (true) break;
if (true) break;

case 34:
//C
this.state = 35;
;
 //BA.debugLineNum = 1212;BA.debugLine="Dim query As String = \"INSERT INTO daily_in";
_query = "INSERT INTO daily_inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1213;BA.debugLine="connection.ExecNonQuery2(query,Array As Str";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent.mostCurrent._label_load_inv_date.getText(),_transaction_number,parent._cursor3.GetString("principal_id"),parent._cursor3.GetString("principal_name"),parent._cursor3.GetString("product_id"),parent._cursor3.GetString("product_variant"),parent._cursor3.GetString("product_description"),"PCS","0","0",BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("total_pcs"))))]),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"FINAL",anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"N/A","N/A","Actual Count",BA.NumberToString(0),"-","-",parent.mostCurrent._login_module._tab_id /*String*/ ,parent.mostCurrent._login_module._username /*String*/ }));
 if (true) break;

case 35:
//C
this.state = 82;
;
 if (true) break;
if (true) break;

case 36:
//C
this.state = 59;
;
 if (true) break;

case 38:
//C
this.state = 39;
 //BA.debugLineNum = 1220;BA.debugLine="Sleep(200)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (200));
this.state = 89;
return;
case 89:
//C
this.state = 39;
;
 //BA.debugLineNum = 1221;BA.debugLine="LABEL_MSGBOX2.textColor = Colors.Black";
parent.mostCurrent._label_msgbox2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1222;BA.debugLine="LABEL_MSGBOX2.Text = \"Updating: \" & cursor3.G";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updating: "+parent._cursor3.GetString("product_description")));
 //BA.debugLineNum = 1223;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_id = '"+parent._cursor3.GetString("product_id")+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"' GROUP BY product_id")));
 //BA.debugLineNum = 1224;BA.debugLine="If cursor2.RowCount > 0 Then";
if (true) break;

case 39:
//if
this.state = 58;
if (parent._cursor2.getRowCount()>0) { 
this.state = 41;
}else {
this.state = 47;
}if (true) break;

case 41:
//C
this.state = 42;
 //BA.debugLineNum = 1225;BA.debugLine="For rowq = 0 To cursor2.RowCount - 1";
if (true) break;

case 42:
//for
this.state = 45;
step52 = 1;
limit52 = (int) (parent._cursor2.getRowCount()-1);
_rowq = (int) (0) ;
this.state = 90;
if (true) break;

case 90:
//C
this.state = 45;
if ((step52 > 0 && _rowq <= limit52) || (step52 < 0 && _rowq >= limit52)) this.state = 44;
if (true) break;

case 91:
//C
this.state = 90;
_rowq = ((int)(0 + _rowq + step52)) ;
if (true) break;

case 44:
//C
this.state = 91;
 //BA.debugLineNum = 1226;BA.debugLine="cursor2.Position = rowq";
parent._cursor2.setPosition(_rowq);
 //BA.debugLineNum = 1227;BA.debugLine="Dim query As String = \"UPDATE daily_invento";
_query = "UPDATE daily_inventory_disc_table SET system_count = ?, status = ?, date_final = ?, time_final = ? WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' AND product_id = '"+parent._cursor3.GetString("product_id")+"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'";
 //BA.debugLineNum = 1228;BA.debugLine="connection.ExecNonQuery2(query,Array As Str";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"0","FINAL",anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())}));
 //BA.debugLineNum = 1229;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 92;
return;
case 92:
//C
this.state = 91;
;
 if (true) break;
if (true) break;

case 45:
//C
this.state = 58;
;
 if (true) break;

case 47:
//C
this.state = 48;
 //BA.debugLineNum = 1232;BA.debugLine="Dim transaction_number As String";
_transaction_number = "";
 //BA.debugLineNum = 1233;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT MAX(C";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT MAX(CAST(item_number as INT)) as item_number from daily_inventory_disc_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'")));
 //BA.debugLineNum = 1234;BA.debugLine="For i = 0 To cursor5.RowCount - 1";
if (true) break;

case 48:
//for
this.state = 57;
step61 = 1;
limit61 = (int) (parent._cursor5.getRowCount()-1);
_i = (int) (0) ;
this.state = 93;
if (true) break;

case 93:
//C
this.state = 57;
if ((step61 > 0 && _i <= limit61) || (step61 < 0 && _i >= limit61)) this.state = 50;
if (true) break;

case 94:
//C
this.state = 93;
_i = ((int)(0 + _i + step61)) ;
if (true) break;

case 50:
//C
this.state = 51;
 //BA.debugLineNum = 1235;BA.debugLine="cursor5.Position = i";
parent._cursor5.setPosition(_i);
 //BA.debugLineNum = 1236;BA.debugLine="If cursor5.GetString(\"item_number\") = Null";
if (true) break;

case 51:
//if
this.state = 56;
if (parent._cursor5.GetString("item_number")== null || (parent._cursor5.GetString("item_number")).equals("")) { 
this.state = 53;
}else {
this.state = 55;
}if (true) break;

case 53:
//C
this.state = 56;
 //BA.debugLineNum = 1237;BA.debugLine="transaction_number = 1";
_transaction_number = BA.NumberToString(1);
 if (true) break;

case 55:
//C
this.state = 56;
 //BA.debugLineNum = 1239;BA.debugLine="transaction_number = cursor5.GetString(\"it";
_transaction_number = BA.NumberToString((double)(Double.parseDouble(parent._cursor5.GetString("item_number")))+1);
 if (true) break;

case 56:
//C
this.state = 94;
;
 if (true) break;
if (true) break;

case 57:
//C
this.state = 58;
;
 //BA.debugLineNum = 1242;BA.debugLine="Dim query As String = \"INSERT INTO daily_inv";
_query = "INSERT INTO daily_inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 1243;BA.debugLine="connection.ExecNonQuery2(query,Array As Stri";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent.mostCurrent._label_load_inv_date.getText(),_transaction_number,parent._cursor3.GetString("principal_id"),parent._cursor3.GetString("principal_name"),parent._cursor3.GetString("product_id"),parent._cursor3.GetString("product_variant"),parent._cursor3.GetString("product_description"),"PCS","0","0","0",anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"FINAL",anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"N/A","N/A","Actual Count",BA.NumberToString(0),"-","-",parent.mostCurrent._login_module._tab_id /*String*/ ,parent.mostCurrent._login_module._username /*String*/ }));
 if (true) break;

case 58:
//C
this.state = 59;
;
 if (true) break;

case 59:
//C
this.state = 62;
;
 if (true) break;

case 61:
//C
this.state = 62;
 //BA.debugLineNum = 1250;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 //BA.debugLineNum = 1251;BA.debugLine="Log(\"error\")";
anywheresoftware.b4a.keywords.Common.LogImpl("759310161","error",0);
 //BA.debugLineNum = 1252;BA.debugLine="Log(cursor3.GetString(\"product_description\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("759310162",parent._cursor3.GetString("product_description"),0);
 //BA.debugLineNum = 1253;BA.debugLine="LABEL_MSGBOX2.TextColor = Colors.red";
parent.mostCurrent._label_msgbox2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 1254;BA.debugLine="LABEL_MSGBOX2.Text = \"ERROR UPDATING: \" & curs";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("ERROR UPDATING: "+parent._cursor3.GetString("product_description")));
 //BA.debugLineNum = 1255;BA.debugLine="rows = cursor3.RowCount";
_rows = parent._cursor3.getRowCount();
 if (true) break;

case 62:
//C
this.state = 77;
;
 //BA.debugLineNum = 1257;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 1259;BA.debugLine="If trigger = 0 Then";

case 63:
//if
this.state = 74;
if (_trigger==0) { 
this.state = 65;
}else {
this.state = 67;
}if (true) break;

case 65:
//C
this.state = 74;
 //BA.debugLineNum = 1260;BA.debugLine="Dim query As String = \"UPDATE daily_inventory_di";
_query = "UPDATE daily_inventory_disc_table SET  status = ? WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' and inventory_date = '"+parent.mostCurrent._label_load_inv_date.getText()+"'";
 //BA.debugLineNum = 1261;BA.debugLine="connection.ExecNonQuery2(query,Array As String(\"";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"FINAL"}));
 //BA.debugLineNum = 1262;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 95;
return;
case 95:
//C
this.state = 74;
;
 //BA.debugLineNum = 1263;BA.debugLine="LABEL_MSGBOX2.Text = \"Updated Successfully\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Updated Successfully"));
 //BA.debugLineNum = 1264;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1265;BA.debugLine="ToastMessageShow(\"Transaction Updated\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Transaction Updated"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1266;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 96;
return;
case 96:
//C
this.state = 74;
;
 //BA.debugLineNum = 1267;BA.debugLine="GET_STATUS";
_get_status();
 //BA.debugLineNum = 1268;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 97;
return;
case 97:
//C
this.state = 74;
;
 //BA.debugLineNum = 1269;BA.debugLine="GET_DAILY";
_get_daily();
 if (true) break;

case 67:
//C
this.state = 68;
 //BA.debugLineNum = 1271;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1272;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 98;
return;
case 98:
//C
this.state = 68;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 1273;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 68:
//if
this.state = 73;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 70;
}else {
this.state = 72;
}if (true) break;

case 70:
//C
this.state = 73;
 //BA.debugLineNum = 1274;BA.debugLine="UPDATE_FINAL";
_update_final();
 if (true) break;

case 72:
//C
this.state = 73;
 //BA.debugLineNum = 1276;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1277;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 73:
//C
this.state = 74;
;
 if (true) break;

case 74:
//C
this.state = 75;
;
 if (true) break;

case 75:
//C
this.state = -1;
;
 //BA.debugLineNum = 1281;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static void  _update_productofftake() throws Exception{
ResumableSub_UPDATE_PRODUCTOFFTAKE rsub = new ResumableSub_UPDATE_PRODUCTOFFTAKE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UPDATE_PRODUCTOFFTAKE extends BA.ResumableSub {
public ResumableSub_UPDATE_PRODUCTOFFTAKE(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1157;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"update_offt";
_cmd = _createcommand("update_offtake_date",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()))});
 //BA.debugLineNum = 1158;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 1159;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 1160;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 1161;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 1162;BA.debugLine="UPDATE_FINAL";
_update_final();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 1164;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1165;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1166;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 9;
return;
case 9:
//C
this.state = 6;
;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 1168;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 1169;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 268;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 269;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 270;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 271;BA.debugLine="End Sub";
return "";
}
public static void  _updatetape() throws Exception{
ResumableSub_UpdateTape rsub = new ResumableSub_UpdateTape(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UpdateTape extends BA.ResumableSub {
public ResumableSub_UpdateTape(wingan.app.daily_count parent) {
this.parent = parent;
}
wingan.app.daily_count parent;
float _hr = 0f;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 1800;BA.debugLine="Dim hr As Float";
_hr = 0f;
 //BA.debugLineNum = 1802;BA.debugLine="lblPaperRoll.Text = Txt";
parent.mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(parent._txt));
 //BA.debugLineNum = 1804;BA.debugLine="hr = stu.MeasureMultilineTextHeight(lblPaperRoll,";
_hr = (float) (parent.mostCurrent._stu.MeasureMultilineTextHeight((android.widget.TextView)(parent.mostCurrent._lblpaperroll.getObject()),BA.ObjectToCharSequence(parent._txt)));
 //BA.debugLineNum = 1805;BA.debugLine="If hr > scvPaperRoll.Height Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_hr>parent.mostCurrent._scvpaperroll.getHeight()) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 1806;BA.debugLine="lblPaperRoll.Height = hr";
parent.mostCurrent._lblpaperroll.setHeight((int) (_hr));
 //BA.debugLineNum = 1807;BA.debugLine="scvPaperRoll.Panel.Height = hr";
parent.mostCurrent._scvpaperroll.getPanel().setHeight((int) (_hr));
 //BA.debugLineNum = 1808;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 4;
;
 //BA.debugLineNum = 1809;BA.debugLine="scvPaperRoll.ScrollPosition = hr";
parent.mostCurrent._scvpaperroll.setScrollPosition((int) (_hr));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1811;BA.debugLine="End Sub";
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
