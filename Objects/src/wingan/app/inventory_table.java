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

public class inventory_table extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static inventory_table mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.inventory_table");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (inventory_table).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.inventory_table");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.inventory_table", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (inventory_table) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (inventory_table) Resume **");
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
		return inventory_table.class;
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
            BA.LogInfo("** Activity (inventory_table) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (inventory_table) Pause event (activity is not paused). **");
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
            inventory_table mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (inventory_table) Resume **");
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
public static anywheresoftware.b4a.phone.Phone _phone = null;
public static anywheresoftware.b4a.obejcts.TTS _tts1 = null;
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
public static String _orig_unit = "";
public static String _orig_quantity = "";
public static String _edit_count = "";
public static String _caseper = "";
public static String _pcsper = "";
public static String _dozper = "";
public static String _boxper = "";
public static String _bagper = "";
public static String _packper = "";
public static String _total_pieces = "";
public static String _error_trigger = "";
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public anywheresoftware.b4a.objects.IME _ctrl = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public wingan.app.b4xtable _table_inventory = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public wingan.app.b4xtableselections _xselections = null;
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
public anywheresoftware.b4a.objects.LabelWrapper _label_load_number = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_status = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_principal = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_description = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_quantity = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_recount = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_edit = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_delete = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_recount = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_msgbox2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_msgbox = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_calcu = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_calcu = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_answer = null;
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
public ResumableSub_Activity_Create(wingan.app.inventory_table parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.inventory_table parent;
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
 //BA.debugLineNum = 110;BA.debugLine="Activity.LoadLayout(\"table_inventory\")";
parent.mostCurrent._activity.LoadLayout("table_inventory",mostCurrent.activityBA);
 //BA.debugLineNum = 112;BA.debugLine="If FirstTime Then";
if (true) break;

case 1:
//if
this.state = 4;
if (_firsttime) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 113;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"upload.";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"upload.png");
 if (true) break;

case 4:
//C
this.state = 5;
;
 //BA.debugLineNum = 116;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 117;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 118;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 119;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 120;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 121;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 122;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 124;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 125;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 126;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 128;BA.debugLine="If connection.IsInitialized = False Then";
if (true) break;

case 5:
//if
this.state = 8;
if (parent._connection.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 7;
}if (true) break;

case 7:
//C
this.state = 8;
 //BA.debugLineNum = 129;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 8:
//C
this.state = 9;
;
 //BA.debugLineNum = 132;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 133;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 134;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 136;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 137;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 139;BA.debugLine="phone.SetScreenOrientation(0)";
parent._phone.SetScreenOrientation(processBA,(int) (0));
 //BA.debugLineNum = 141;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 9;
;
 //BA.debugLineNum = 142;BA.debugLine="Dim Ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 143;BA.debugLine="Ref.Target = EDITTEXT_QUANTITY ' The text field b";
_ref.Target = (Object)(parent.mostCurrent._edittext_quantity.getObject());
 //BA.debugLineNum = 144;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 146;BA.debugLine="LOAD_TABLE_HEADER";
_load_table_header();
 //BA.debugLineNum = 147;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 9;
;
 //BA.debugLineNum = 148;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 //BA.debugLineNum = 150;BA.debugLine="If MONTLHY_INVENTORY2_MODULE.inventory_type = \"CO";
if (true) break;

case 9:
//if
this.state = 14;
if ((parent.mostCurrent._montlhy_inventory2_module._inventory_type /*String*/ ).equals("COUNT")) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 151;BA.debugLine="BUTTON_RECOUNT.Visible = False";
parent.mostCurrent._button_recount.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 153;BA.debugLine="BUTTON_RECOUNT.Visible = True";
parent.mostCurrent._button_recount.setVisible(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 155;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;
 //BA.debugLineNum = 156;BA.debugLine="Sub Activity_CreateMenu(Menu As ACMenu)";
 //BA.debugLineNum = 157;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 158;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 159;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 1240;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 1241;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 1242;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 1244;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 162;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 163;BA.debugLine="If TTS1.IsInitialized = False Then";
if (_tts1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 164;BA.debugLine="TTS1.Initialize(\"TTS1\")";
_tts1.Initialize(processBA,"TTS1");
 };
 //BA.debugLineNum = 166;BA.debugLine="End Sub";
return "";
}
public static void  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
ResumableSub_ACToolBarLight1_MenuItemClick rsub = new ResumableSub_ACToolBarLight1_MenuItemClick(null,_item);
rsub.resume(processBA, null);
}
public static class ResumableSub_ACToolBarLight1_MenuItemClick extends BA.ResumableSub {
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.inventory_table parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.inventory_table parent;
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
 //BA.debugLineNum = 644;BA.debugLine="If Item.Title = \"cart\" Then";
if (true) break;

case 1:
//if
this.state = 10;
if ((_item.getTitle()).equals("cart")) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 645;BA.debugLine="Msgbox2Async(\"Are you sure you want to UPLOAD TH";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to UPLOAD THIS TRANSACTION?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 646;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 11;
return;
case 11:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 647;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 4:
//if
this.state = 9;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 648;BA.debugLine="Delete_Inventory_Ref";
_delete_inventory_ref();
 if (true) break;

case 8:
//C
this.state = 9;
 if (true) break;

case 9:
//C
this.state = 10;
;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 653;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 624;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 625;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 626;BA.debugLine="StartActivity(MONTLHY_INVENTORY2_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._montlhy_inventory2_module.getObject()));
 //BA.debugLineNum = 627;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 628;BA.debugLine="MONTLHY_INVENTORY2_MODULE.cmb_trigger = 0";
mostCurrent._montlhy_inventory2_module._cmb_trigger /*int*/  = (int) (0);
 //BA.debugLineNum = 629;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 619;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 620;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 621;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 622;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 623;BA.debugLine="End Sub";
return null;
}
public static String  _btnback_click() throws Exception{
 //BA.debugLineNum = 1172;BA.debugLine="Sub btnBack_Click";
 //BA.debugLineNum = 1173;BA.debugLine="If sVal.Length > 0 Then";
if (_sval.length()>0) { 
 //BA.debugLineNum = 1174;BA.debugLine="Txt = sVal.SubString2(0, sVal.Length - 1)";
_txt = _sval.substring((int) (0),(int) (_sval.length()-1));
 //BA.debugLineNum = 1175;BA.debugLine="sVal = sVal.SubString2(0, sVal.Length - 1)";
_sval = _sval.substring((int) (0),(int) (_sval.length()-1));
 //BA.debugLineNum = 1176;BA.debugLine="UpdateTape";
_updatetape();
 };
 //BA.debugLineNum = 1178;BA.debugLine="End Sub";
return "";
}
public static String  _btncharsize_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 1198;BA.debugLine="Sub btnCharSize_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 1199;BA.debugLine="If Checked = False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1200;BA.debugLine="lblPaperRoll.TextSize = 16 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (16*_scaleauto));
 }else {
 //BA.debugLineNum = 1202;BA.debugLine="lblPaperRoll.TextSize = 22 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (22*_scaleauto));
 };
 //BA.debugLineNum = 1204;BA.debugLine="End Sub";
return "";
}
public static String  _btnclr_click() throws Exception{
 //BA.debugLineNum = 1153;BA.debugLine="Sub btnClr_Click";
 //BA.debugLineNum = 1160;BA.debugLine="Val = 0";
_val = 0;
 //BA.debugLineNum = 1161;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1162;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1163;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1164;BA.debugLine="Txt = \"\"";
_txt = "";
 //BA.debugLineNum = 1165;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1166;BA.debugLine="lblPaperRoll.Text = \"\"";
mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1167;BA.debugLine="lblPaperRoll.Height = scvPaperRoll.Height";
mostCurrent._lblpaperroll.setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1168;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1169;BA.debugLine="LABEL_LOAD_ANSWER.Text = \"0\"";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1171;BA.debugLine="End Sub";
return "";
}
public static String  _btndigit_click() throws Exception{
String _bs = "";
anywheresoftware.b4a.objects.ConcreteViewWrapper _send = null;
 //BA.debugLineNum = 960;BA.debugLine="Sub btnDigit_Click";
 //BA.debugLineNum = 961;BA.debugLine="Dim bs As String, Send As View";
_bs = "";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 963;BA.debugLine="If New1 = 0 Then";
if (_new1==0) { 
 //BA.debugLineNum = 964;BA.debugLine="New1 = 1";
_new1 = (int) (1);
 };
 //BA.debugLineNum = 967;BA.debugLine="Send = Sender";
_send = (anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 968;BA.debugLine="bs = Send.Tag";
_bs = BA.ObjectToString(_send.getTag());
 //BA.debugLineNum = 970;BA.debugLine="Select bs";
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
 //BA.debugLineNum = 972;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"g","s","m","x")) {
case 0: 
case 1: 
case 2: 
case 3: {
 //BA.debugLineNum = 974;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 977;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 978;BA.debugLine="Txt = Txt & CRLF & CRLF";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 979;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 980;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 981;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 982;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 };
 //BA.debugLineNum = 985;BA.debugLine="If bs = \"3.1415926535897932\" Then";
if ((_bs).equals("3.1415926535897932")) { 
 //BA.debugLineNum = 986;BA.debugLine="If sVal <> \"\" Then";
if ((_sval).equals("") == false) { 
 //BA.debugLineNum = 987;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 989;BA.debugLine="Txt = Txt & cPI";
_txt = _txt+BA.NumberToString(anywheresoftware.b4a.keywords.Common.cPI);
 //BA.debugLineNum = 990;BA.debugLine="sVal = cPI";
_sval = BA.NumberToString(anywheresoftware.b4a.keywords.Common.cPI);
 }else if((_bs).equals(".")) { 
 //BA.debugLineNum = 992;BA.debugLine="If sVal.IndexOf(\".\") < 0 Then";
if (_sval.indexOf(".")<0) { 
 //BA.debugLineNum = 993;BA.debugLine="Txt = Txt & bs";
_txt = _txt+_bs;
 //BA.debugLineNum = 994;BA.debugLine="sVal = sVal & bs";
_sval = _sval+_bs;
 };
 }else {
 //BA.debugLineNum = 997;BA.debugLine="Txt = Txt & bs";
_txt = _txt+_bs;
 //BA.debugLineNum = 998;BA.debugLine="sVal = sVal & bs";
_sval = _sval+_bs;
 };
 break; }
case 12: 
case 13: 
case 14: 
case 15: 
case 16: 
case 17: {
 //BA.debugLineNum = 1001;BA.debugLine="If sVal =\"\" Then";
if ((_sval).equals("")) { 
 //BA.debugLineNum = 1002;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"a","b","c","d","y","")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: {
 //BA.debugLineNum = 1004;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1006;BA.debugLine="sVal = Result1";
_sval = BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1008;BA.debugLine="GetValue(bs)";
_getvalue(_bs);
 break; }
case 18: 
case 19: 
case 20: {
 //BA.debugLineNum = 1010;BA.debugLine="If sVal = \"\" Then";
if ((_sval).equals("")) { 
 //BA.debugLineNum = 1011;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"a","b","c","d","y","")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: {
 //BA.debugLineNum = 1013;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1015;BA.debugLine="sVal = Result1";
_sval = BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1017;BA.debugLine="If Op0 = \"\" Then";
if ((_op0).equals("")) { 
 //BA.debugLineNum = 1018;BA.debugLine="Result1 = sVal";
_result1 = (double)(Double.parseDouble(_sval));
 };
 //BA.debugLineNum = 1020;BA.debugLine="GetValue(bs)";
_getvalue(_bs);
 break; }
case 21: {
 //BA.debugLineNum = 1022;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1023;BA.debugLine="Txt = Txt & CRLF & CRLF";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 1024;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1025;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1026;BA.debugLine="Op0 = \"\"";
_op0 = "";
 };
 break; }
}
;
 //BA.debugLineNum = 1030;BA.debugLine="UpdateTape";
_updatetape();
 //BA.debugLineNum = 1031;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_click() throws Exception{
 //BA.debugLineNum = 1179;BA.debugLine="Sub btnExit_Click";
 //BA.debugLineNum = 1180;BA.debugLine="Val = 0";
_val = 0;
 //BA.debugLineNum = 1181;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1182;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1183;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1184;BA.debugLine="Txt = \"\"";
_txt = "";
 //BA.debugLineNum = 1185;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1186;BA.debugLine="lblPaperRoll.Text = \"\"";
mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1187;BA.debugLine="lblPaperRoll.Height = scvPaperRoll.Height";
mostCurrent._lblpaperroll.setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1188;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1189;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1191;BA.debugLine="If EDITTEXT_QUANTITY.Enabled = False Then";
if (mostCurrent._edittext_quantity.getEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
 }else {
 //BA.debugLineNum = 1194;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_LOAD_ANSWER.Text";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(mostCurrent._label_load_answer.getText()));
 };
 //BA.debugLineNum = 1197;BA.debugLine="End Sub";
return "";
}
public static String  _button_calcu_click() throws Exception{
 //BA.debugLineNum = 1231;BA.debugLine="Sub BUTTON_CALCU_Click";
 //BA.debugLineNum = 1232;BA.debugLine="oncal";
_oncal();
 //BA.debugLineNum = 1233;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,True)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1234;BA.debugLine="PANEL_BG_CALCU.BringToFront";
mostCurrent._panel_bg_calcu.BringToFront();
 //BA.debugLineNum = 1235;BA.debugLine="End Sub";
return "";
}
public static void  _button_delete_click() throws Exception{
ResumableSub_BUTTON_DELETE_Click rsub = new ResumableSub_BUTTON_DELETE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_DELETE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_DELETE_Click(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
int _result = 0;
String _insert_query = "";
String _query = "";
String _unit = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 409;BA.debugLine="If BUTTON_DELETE.Text = \" Delete\" Then";
if (true) break;

case 1:
//if
this.state = 56;
if ((parent.mostCurrent._button_delete.getText()).equals(" Delete")) { 
this.state = 3;
}else {
this.state = 23;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 410;BA.debugLine="Msgbox2Async(\"Are you sure you want to delete th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to delete this item?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 411;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 57;
return;
case 57:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 412;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 4:
//if
this.state = 21;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 6;
}else {
this.state = 20;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 413;BA.debugLine="Dim insert_query As String = \"INSERT INTO inven";
_insert_query = "INSERT INTO inventory_disc_table_trail SELECT *,? as 'edit_by','DELETED' as 'edit_type',? as edit_date,? as edit_time from inventory_disc_table WHERE transaction_number = ? AND transaction_id = ?";
 //BA.debugLineNum = 414;BA.debugLine="connection.ExecNonQuery2(insert_query,Array As";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_number.getText(),parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ }));
 //BA.debugLineNum = 415;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 58;
return;
case 58:
//C
this.state = 7;
;
 //BA.debugLineNum = 416;BA.debugLine="Dim query As String = \"DELETE from inventory_di";
_query = "DELETE from inventory_disc_table WHERE transaction_number = ? AND transaction_id = ?";
 //BA.debugLineNum = 417;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._label_load_number.getText(),parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ }));
 //BA.debugLineNum = 418;BA.debugLine="ProgressDialogShow2(\"Deleting...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Deleting..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 419;BA.debugLine="Sleep(1500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1500));
this.state = 59;
return;
case 59:
//C
this.state = 7;
;
 //BA.debugLineNum = 420;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 421;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 60;
return;
case 60:
//C
this.state = 7;
;
 //BA.debugLineNum = 422;BA.debugLine="Dim unit As String = CMB_UNIT.cmbBox.SelectedIt";
_unit = parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 423;BA.debugLine="If unit = \"PCS\" Then";
if (true) break;

case 7:
//if
this.state = 18;
if ((_unit).equals("PCS")) { 
this.state = 9;
}else if((_unit).equals("DOZ")) { 
this.state = 17;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 424;BA.debugLine="If EDITTEXT_QUANTITY.Text > 1 Then";
if (true) break;

case 10:
//if
this.state = 15;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))>1) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 15;
 //BA.debugLineNum = 425;BA.debugLine="unit = \"PIECES\"";
_unit = "PIECES";
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 427;BA.debugLine="unit = \"PIECE\"";
_unit = "PIECE";
 if (true) break;

case 15:
//C
this.state = 18;
;
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 430;BA.debugLine="unit = \"DOZEN\"";
_unit = "DOZEN";
 if (true) break;

case 18:
//C
this.state = 21;
;
 //BA.debugLineNum = 432;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 61;
return;
case 61:
//C
this.state = 21;
;
 //BA.debugLineNum = 433;BA.debugLine="TTS1.Speak(EDITTEXT_QUANTITY.Text & \" \" & unit";
parent._tts1.Speak(parent.mostCurrent._edittext_quantity.getText()+" "+_unit+" DELETED.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 434;BA.debugLine="PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_recount.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 435;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 62;
return;
case 62:
//C
this.state = 21;
;
 //BA.debugLineNum = 436;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 //BA.debugLineNum = 437;BA.debugLine="ToastMessageShow(\"Updated Successfully\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updated Successfully"),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 20:
//C
this.state = 21;
 if (true) break;

case 21:
//C
this.state = 56;
;
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 442;BA.debugLine="Dim insert_query As String = \"INSERT INTO invent";
_insert_query = "INSERT INTO inventory_disc_table_trail SELECT *,? as 'edit_by','MODIFIED' as 'edit_type',? as edit_date,? as edit_time from inventory_disc_table WHERE transaction_number = ? AND transaction_id = ?";
 //BA.debugLineNum = 443;BA.debugLine="connection.ExecNonQuery2(insert_query,Array As S";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_number.getText(),parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ }));
 //BA.debugLineNum = 445;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = -1 Then CMB_U";
if (true) break;

case 24:
//if
this.state = 29;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 26;
;}if (true) break;

case 26:
//C
this.state = 29;
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 446;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 63;
return;
case 63:
//C
this.state = 30;
;
 //BA.debugLineNum = 447;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbB";
if (true) break;

case 30:
//if
this.state = 43;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 32;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 34;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 36;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 38;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 40;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 42;
}if (true) break;

case 32:
//C
this.state = 43;
 //BA.debugLineNum = 448;BA.debugLine="total_pieces = caseper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 34:
//C
this.state = 43;
 //BA.debugLineNum = 450;BA.debugLine="total_pieces = pcsper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 36:
//C
this.state = 43;
 //BA.debugLineNum = 452;BA.debugLine="total_pieces = dozper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 38:
//C
this.state = 43;
 //BA.debugLineNum = 454;BA.debugLine="total_pieces = boxper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 40:
//C
this.state = 43;
 //BA.debugLineNum = 456;BA.debugLine="total_pieces = bagper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 458;BA.debugLine="total_pieces = packper * EDITTEXT_QUANTITY.text";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 43:
//C
this.state = 44;
;
 //BA.debugLineNum = 462;BA.debugLine="Dim query As String = \"UPDATE inventory_disc_tab";
_query = "UPDATE inventory_disc_table SET unit = ? , quantity = ?, total_pieces = ?, edit_count = ? WHERE transaction_number = ? AND transaction_id = ?";
 //BA.debugLineNum = 463;BA.debugLine="connection.ExecNonQuery2(query,Array As String(C";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,BA.NumberToString((double)(Double.parseDouble(parent._edit_count))+1),parent.mostCurrent._label_load_number.getText(),parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ }));
 //BA.debugLineNum = 464;BA.debugLine="ProgressDialogShow2(\"Updating...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Updating..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 465;BA.debugLine="Sleep(1500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1500));
this.state = 64;
return;
case 64:
//C
this.state = 44;
;
 //BA.debugLineNum = 466;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 467;BA.debugLine="Dim unit As String = CMB_UNIT.cmbBox.SelectedIte";
_unit = parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 468;BA.debugLine="If unit = \"PCS\" Then";
if (true) break;

case 44:
//if
this.state = 55;
if ((_unit).equals("PCS")) { 
this.state = 46;
}else if((_unit).equals("DOZ")) { 
this.state = 54;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 469;BA.debugLine="If EDITTEXT_QUANTITY.Text > 1 Then";
if (true) break;

case 47:
//if
this.state = 52;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))>1) { 
this.state = 49;
}else {
this.state = 51;
}if (true) break;

case 49:
//C
this.state = 52;
 //BA.debugLineNum = 470;BA.debugLine="unit = \"PIECES\"";
_unit = "PIECES";
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 472;BA.debugLine="unit = \"PIECE\"";
_unit = "PIECE";
 if (true) break;

case 52:
//C
this.state = 55;
;
 if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 475;BA.debugLine="unit = \"DOZEN\"";
_unit = "DOZEN";
 if (true) break;

case 55:
//C
this.state = 56;
;
 //BA.debugLineNum = 477;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 65;
return;
case 65:
//C
this.state = 56;
;
 //BA.debugLineNum = 478;BA.debugLine="TTS1.Speak(EDITTEXT_QUANTITY.Text & \" \" & unit &";
parent._tts1.Speak(parent.mostCurrent._edittext_quantity.getText()+" "+_unit+" UPDATED.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 479;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 66;
return;
case 66:
//C
this.state = 56;
;
 //BA.debugLineNum = 480;BA.debugLine="PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_recount.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 481;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 67;
return;
case 67:
//C
this.state = 56;
;
 //BA.debugLineNum = 482;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 //BA.debugLineNum = 483;BA.debugLine="ToastMessageShow(\"Updated Successfully\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updated Successfully"),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 56:
//C
this.state = -1;
;
 //BA.debugLineNum = 485;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_edit_click() throws Exception{
ResumableSub_BUTTON_EDIT_Click rsub = new ResumableSub_BUTTON_EDIT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_EDIT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_EDIT_Click(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
String _my_unit = "";
int _qrow = 0;
int step9;
int limit9;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 538;BA.debugLine="If BUTTON_EDIT.Text = \" Edit\" Then";
if (true) break;

case 1:
//if
this.state = 29;
if ((parent.mostCurrent._button_edit.getText()).equals(" Edit")) { 
this.state = 3;
}else {
this.state = 28;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 539;BA.debugLine="Dim my_unit As String = CMB_UNIT.cmbBox.Selected";
_my_unit = parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 540;BA.debugLine="BUTTON_EDIT.Text = \" Cancel Edit\"";
parent.mostCurrent._button_edit.setText(BA.ObjectToCharSequence(" Cancel Edit"));
 //BA.debugLineNum = 541;BA.debugLine="BUTTON_EDIT.TextColor = Colors.Red";
parent.mostCurrent._button_edit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 542;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 30;
return;
case 30:
//C
this.state = 4;
;
 //BA.debugLineNum = 543;BA.debugLine="BUTTON_DELETE.Text = \" Update\"";
parent.mostCurrent._button_delete.setText(BA.ObjectToCharSequence(" Update"));
 //BA.debugLineNum = 544;BA.debugLine="BUTTON_DELETE.TextColor = Colors.Blue";
parent.mostCurrent._button_delete.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 545;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 546;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 26;
step9 = 1;
limit9 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 31;
if (true) break;

case 31:
//C
this.state = 26;
if ((step9 > 0 && _qrow <= limit9) || (step9 < 0 && _qrow >= limit9)) this.state = 6;
if (true) break;

case 32:
//C
this.state = 31;
_qrow = ((int)(0 + _qrow + step9)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 547;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 548;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"pr";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 549;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetString";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 551;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 552;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0 T";
if (true) break;

case 7:
//if
this.state = 10;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 9;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 553;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 555;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 Th";

case 10:
//if
this.state = 13;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 556;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 558;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 Th";

case 13:
//if
this.state = 16;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 559;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 561;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 Th";

case 16:
//if
this.state = 19;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 562;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 564;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 Th";

case 19:
//if
this.state = 22;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 565;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 567;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0 T";

case 22:
//if
this.state = 25;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 568;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 25:
//C
this.state = 32;
;
 //BA.debugLineNum = 571;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS\"";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 572;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 573;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 574;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 575;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 576;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS\"";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 578;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 33;
return;
case 33:
//C
this.state = 32;
;
 //BA.debugLineNum = 579;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexO";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf(_my_unit));
 if (true) break;
if (true) break;

case 26:
//C
this.state = 29;
;
 //BA.debugLineNum = 581;BA.debugLine="CMB_UNIT.cmbBox.Enabled = True";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 582;BA.debugLine="EDITTEXT_QUANTITY.Enabled = True";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 583;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 34;
return;
case 34:
//C
this.state = 29;
;
 //BA.debugLineNum = 584;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 586;BA.debugLine="BUTTON_EDIT.Text = \" Edit\"";
parent.mostCurrent._button_edit.setText(BA.ObjectToCharSequence(" Edit"));
 //BA.debugLineNum = 587;BA.debugLine="BUTTON_EDIT.TextColor = Colors.Blue";
parent.mostCurrent._button_edit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 588;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 35;
return;
case 35:
//C
this.state = 29;
;
 //BA.debugLineNum = 589;BA.debugLine="BUTTON_DELETE.Text = \" Delete\"";
parent.mostCurrent._button_delete.setText(BA.ObjectToCharSequence(" Delete"));
 //BA.debugLineNum = 590;BA.debugLine="BUTTON_DELETE.TextColor = Colors.Red";
parent.mostCurrent._button_delete.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 591;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 592;BA.debugLine="CMB_UNIT.cmbBox.Add(orig_unit)";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._orig_unit);
 //BA.debugLineNum = 593;BA.debugLine="CMB_UNIT.cmbBox.Enabled = False";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 594;BA.debugLine="EDITTEXT_QUANTITY.Text = orig_quantity";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent._orig_quantity));
 //BA.debugLineNum = 595;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 596;BA.debugLine="ToastMessageShow(\"Editing Canceled\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Editing Canceled"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 29:
//C
this.state = -1;
;
 //BA.debugLineNum = 598;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_exit_calcu_click() throws Exception{
 //BA.debugLineNum = 1228;BA.debugLine="Sub BUTTON_EXIT_CALCU_Click";
 //BA.debugLineNum = 1229;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1230;BA.debugLine="End Sub";
return "";
}
public static void  _button_recount_click() throws Exception{
ResumableSub_BUTTON_RECOUNT_Click rsub = new ResumableSub_BUTTON_RECOUNT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_RECOUNT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_RECOUNT_Click(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
String _query = "";
String _unit = "";
String _insert_query = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 327;BA.debugLine="If LOGIN_MODULE.username <> \"\" Or LOGIN_MODULE.ta";
if (true) break;

case 1:
//if
this.state = 62;
if ((parent.mostCurrent._login_module._username /*String*/ ).equals("") == false || (parent.mostCurrent._login_module._tab_id /*String*/ ).equals("") == false) { 
this.state = 3;
}else {
this.state = 61;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 328;BA.debugLine="If MONTLHY_INVENTORY2_MODULE.inventory_type = \"C";
if (true) break;

case 4:
//if
this.state = 59;
if ((parent.mostCurrent._montlhy_inventory2_module._inventory_type /*String*/ ).equals("COUNT")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 59;
 //BA.debugLineNum = 329;BA.debugLine="Msgbox2Async(\"You cannot recount an item becaus";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You cannot recount an item because your inventory type is COUNT."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 331;BA.debugLine="If BUTTON_EDIT.Text = \" Edit\" Then";
if (true) break;

case 9:
//if
this.state = 58;
if ((parent.mostCurrent._button_edit.getText()).equals(" Edit")) { 
this.state = 11;
}else {
this.state = 25;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 332;BA.debugLine="Dim query As String = \"UPDATE inventory_disc_t";
_query = "UPDATE inventory_disc_table SET inventory_status = ? , recount_by = ? , recount_person1 = ? , recount_person2 = ?, recount_date = ?, recount_time = ? WHERE transaction_number = ? AND transaction_id = ?";
 //BA.debugLineNum = 333;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"RECOUNT",parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._montlhy_inventory2_module._assign1 /*String*/ ,parent.mostCurrent._montlhy_inventory2_module._assign2 /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_number.getText(),parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ }));
 //BA.debugLineNum = 334;BA.debugLine="ProgressDialogShow2(\"Recounting...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Recounting..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 335;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 63;
return;
case 63:
//C
this.state = 12;
;
 //BA.debugLineNum = 336;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 337;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 64;
return;
case 64:
//C
this.state = 12;
;
 //BA.debugLineNum = 338;BA.debugLine="Dim unit As String = CMB_UNIT.cmbBox.SelectedI";
_unit = parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 339;BA.debugLine="If unit = \"PCS\" Then";
if (true) break;

case 12:
//if
this.state = 23;
if ((_unit).equals("PCS")) { 
this.state = 14;
}else if((_unit).equals("DOZ")) { 
this.state = 22;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 340;BA.debugLine="If EDITTEXT_QUANTITY.Text > 1 Then";
if (true) break;

case 15:
//if
this.state = 20;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))>1) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 20;
 //BA.debugLineNum = 341;BA.debugLine="unit = \"PIECES\"";
_unit = "PIECES";
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 343;BA.debugLine="unit = \"PIECE\"";
_unit = "PIECE";
 if (true) break;

case 20:
//C
this.state = 23;
;
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 346;BA.debugLine="unit = \"DOZEN\"";
_unit = "DOZEN";
 if (true) break;

case 23:
//C
this.state = 58;
;
 //BA.debugLineNum = 348;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 65;
return;
case 65:
//C
this.state = 58;
;
 //BA.debugLineNum = 349;BA.debugLine="TTS1.Speak(EDITTEXT_QUANTITY.Text & \" \" & unit";
parent._tts1.Speak(parent.mostCurrent._edittext_quantity.getText()+" "+_unit+" RECOUNTED.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 350;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 66;
return;
case 66:
//C
this.state = 58;
;
 //BA.debugLineNum = 351;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 //BA.debugLineNum = 352;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 67;
return;
case 67:
//C
this.state = 58;
;
 //BA.debugLineNum = 353;BA.debugLine="NEXT_ITEM";
_next_item();
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 355;BA.debugLine="Dim insert_query As String = \"INSERT INTO inve";
_insert_query = "INSERT INTO inventory_disc_table_trail SELECT *,? as 'edit_by','MODIFIED' as 'edit_type',? as edit_date,? as edit_time from inventory_disc_table WHERE transaction_number = ? AND transaction_id = ?";
 //BA.debugLineNum = 356;BA.debugLine="connection.ExecNonQuery2(insert_query,Array As";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_number.getText(),parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ }));
 //BA.debugLineNum = 357;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 68;
return;
case 68:
//C
this.state = 26;
;
 //BA.debugLineNum = 358;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = -1 Then CMB";
if (true) break;

case 26:
//if
this.state = 31;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 28;
;}if (true) break;

case 28:
//C
this.state = 31;
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
if (true) break;

case 31:
//C
this.state = 32;
;
 //BA.debugLineNum = 359;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 69;
return;
case 69:
//C
this.state = 32;
;
 //BA.debugLineNum = 361;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cm";
if (true) break;

case 32:
//if
this.state = 45;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 34;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 36;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 38;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 40;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 42;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 44;
}if (true) break;

case 34:
//C
this.state = 45;
 //BA.debugLineNum = 362;BA.debugLine="total_pieces = caseper * EDITTEXT_QUANTITY.te";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 36:
//C
this.state = 45;
 //BA.debugLineNum = 364;BA.debugLine="total_pieces = pcsper * EDITTEXT_QUANTITY.tex";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 38:
//C
this.state = 45;
 //BA.debugLineNum = 366;BA.debugLine="total_pieces = dozper * EDITTEXT_QUANTITY.tex";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 40:
//C
this.state = 45;
 //BA.debugLineNum = 368;BA.debugLine="total_pieces = boxper * EDITTEXT_QUANTITY.tex";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 42:
//C
this.state = 45;
 //BA.debugLineNum = 370;BA.debugLine="total_pieces = bagper * EDITTEXT_QUANTITY.tex";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 44:
//C
this.state = 45;
 //BA.debugLineNum = 372;BA.debugLine="total_pieces = packper * EDITTEXT_QUANTITY.te";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 45:
//C
this.state = 46;
;
 //BA.debugLineNum = 375;BA.debugLine="Dim query As String = \"UPDATE inventory_disc_t";
_query = "UPDATE inventory_disc_table SET unit = ? , quantity = ?, total_pieces = ?, edit_count = ?, inventory_status = ? , recount_by = ? , recount_person1 = ? , recount_person2 = ?, recount_date = ?, recount_time = ? WHERE transaction_number = ? AND transaction_id = ?";
 //BA.debugLineNum = 376;BA.debugLine="connection.ExecNonQuery2(query,Array As String";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,BA.NumberToString((double)(Double.parseDouble(parent._edit_count))+1),"RECOUNT",parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._montlhy_inventory2_module._assign1 /*String*/ ,parent.mostCurrent._montlhy_inventory2_module._assign2 /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._label_load_number.getText(),parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ }));
 //BA.debugLineNum = 377;BA.debugLine="ProgressDialogShow2(\"Recounting...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Recounting..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 378;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 70;
return;
case 70:
//C
this.state = 46;
;
 //BA.debugLineNum = 379;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 380;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 71;
return;
case 71:
//C
this.state = 46;
;
 //BA.debugLineNum = 381;BA.debugLine="Dim unit As String = CMB_UNIT.cmbBox.SelectedI";
_unit = parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 382;BA.debugLine="If unit = \"PCS\" Then";
if (true) break;

case 46:
//if
this.state = 57;
if ((_unit).equals("PCS")) { 
this.state = 48;
}else if((_unit).equals("DOZ")) { 
this.state = 56;
}if (true) break;

case 48:
//C
this.state = 49;
 //BA.debugLineNum = 383;BA.debugLine="If EDITTEXT_QUANTITY.Text > 1 Then";
if (true) break;

case 49:
//if
this.state = 54;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))>1) { 
this.state = 51;
}else {
this.state = 53;
}if (true) break;

case 51:
//C
this.state = 54;
 //BA.debugLineNum = 384;BA.debugLine="unit = \"PIECES\"";
_unit = "PIECES";
 if (true) break;

case 53:
//C
this.state = 54;
 //BA.debugLineNum = 386;BA.debugLine="unit = \"PIECE\"";
_unit = "PIECE";
 if (true) break;

case 54:
//C
this.state = 57;
;
 if (true) break;

case 56:
//C
this.state = 57;
 //BA.debugLineNum = 389;BA.debugLine="unit = \"DOZEN\"";
_unit = "DOZEN";
 if (true) break;

case 57:
//C
this.state = 58;
;
 //BA.debugLineNum = 391;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 72;
return;
case 72:
//C
this.state = 58;
;
 //BA.debugLineNum = 392;BA.debugLine="TTS1.Speak(EDITTEXT_QUANTITY.Text & \" \" & unit";
parent._tts1.Speak(parent.mostCurrent._edittext_quantity.getText()+" "+_unit+" UPDATED and RECOUNTED.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 393;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 73;
return;
case 73:
//C
this.state = 58;
;
 //BA.debugLineNum = 394;BA.debugLine="BUTTON_EDIT.Text = \" Edit\"";
parent.mostCurrent._button_edit.setText(BA.ObjectToCharSequence(" Edit"));
 //BA.debugLineNum = 395;BA.debugLine="BUTTON_EDIT.TextColor = Colors.Blue";
parent.mostCurrent._button_edit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 396;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 74;
return;
case 74:
//C
this.state = 58;
;
 //BA.debugLineNum = 397;BA.debugLine="BUTTON_DELETE.Text = \" Delete\"";
parent.mostCurrent._button_delete.setText(BA.ObjectToCharSequence(" Delete"));
 //BA.debugLineNum = 398;BA.debugLine="BUTTON_DELETE.TextColor = Colors.Red";
parent.mostCurrent._button_delete.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 399;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 //BA.debugLineNum = 400;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 75;
return;
case 75:
//C
this.state = 58;
;
 //BA.debugLineNum = 401;BA.debugLine="NEXT_ITEM";
_next_item();
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
 //BA.debugLineNum = 405;BA.debugLine="Msgbox2Async(\"TABLET ID AND USERNAME CANNOT READ";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 62:
//C
this.state = -1;
;
 //BA.debugLineNum = 407;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _calcresult1(String _op) throws Exception{
String _res = "";
 //BA.debugLineNum = 1116;BA.debugLine="Sub CalcResult1(Op As String)";
 //BA.debugLineNum = 1117;BA.debugLine="Select Op";
switch (BA.switchObjectToInt(_op,"a","b","c","d","g","s","x","y")) {
case 0: {
 //BA.debugLineNum = 1119;BA.debugLine="Result1 = Result1 + Val";
_result1 = _result1+_val;
 break; }
case 1: {
 //BA.debugLineNum = 1121;BA.debugLine="Result1 = Result1 - Val";
_result1 = _result1-_val;
 break; }
case 2: {
 //BA.debugLineNum = 1123;BA.debugLine="Result1 = Result1 * Val";
_result1 = _result1*_val;
 break; }
case 3: {
 //BA.debugLineNum = 1125;BA.debugLine="Result1 = Result1 / Val";
_result1 = _result1/(double)_val;
 break; }
case 4: {
 //BA.debugLineNum = 1127;BA.debugLine="Result1 = Result1 * Result1";
_result1 = _result1*_result1;
 break; }
case 5: {
 //BA.debugLineNum = 1129;BA.debugLine="Result1 = Sqrt(Result1)";
_result1 = anywheresoftware.b4a.keywords.Common.Sqrt(_result1);
 break; }
case 6: {
 //BA.debugLineNum = 1131;BA.debugLine="If Result1 <> 0 Then";
if (_result1!=0) { 
 //BA.debugLineNum = 1132;BA.debugLine="Result1 = 1 / Result1";
_result1 = 1/(double)_result1;
 };
 break; }
case 7: {
 //BA.debugLineNum = 1135;BA.debugLine="Result1 = Result1 * Val / 100";
_result1 = _result1*_val/(double)100;
 break; }
}
;
 //BA.debugLineNum = 1137;BA.debugLine="Dim res As String = Result1";
_res = BA.NumberToString(_result1);
 //BA.debugLineNum = 1138;BA.debugLine="LABEL_LOAD_ANSWER.Text = res";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence(_res));
 //BA.debugLineNum = 1139;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_unit_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_UNIT_SelectedIndexChanged rsub = new ResumableSub_CMB_UNIT_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_UNIT_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_UNIT_SelectedIndexChanged(wingan.app.inventory_table parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.inventory_table parent;
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
 //BA.debugLineNum = 601;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 602;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 603;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 604;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 605;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 606;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 661;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 662;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 663;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 664;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 665;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 666;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 667;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 656;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 657;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 658;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,inventory_table.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 659;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 660;BA.debugLine="End Sub";
return null;
}
public static void  _delete_inventory_disc() throws Exception{
ResumableSub_Delete_Inventory_Disc rsub = new ResumableSub_Delete_Inventory_Disc(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Delete_Inventory_Disc extends BA.ResumableSub {
public ResumableSub_Delete_Inventory_Disc(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
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
 //BA.debugLineNum = 795;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_inve";
_cmd = _createcommand("delete_inventory_disc",new Object[]{(Object)(parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ )});
 //BA.debugLineNum = 796;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 797;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 798;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 799;BA.debugLine="Insert_Inventory_Disc";
_insert_inventory_disc();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 801;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 802;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 803;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 804;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 805;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 14;
return;
case 14:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 806;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 807;BA.debugLine="Delete_Inventory_Ref";
_delete_inventory_ref();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 809;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 810;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 813;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 814;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _js) throws Exception{
}
public static void  _delete_inventory_disc_trail() throws Exception{
ResumableSub_Delete_Inventory_Disc_Trail rsub = new ResumableSub_Delete_Inventory_Disc_Trail(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Delete_Inventory_Disc_Trail extends BA.ResumableSub {
public ResumableSub_Delete_Inventory_Disc_Trail(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
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
 //BA.debugLineNum = 854;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_inve";
_cmd = _createcommand("delete_inventory_disc_trail",new Object[]{(Object)(parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ )});
 //BA.debugLineNum = 855;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 856;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 7;
return;
case 7:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 857;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 858;BA.debugLine="Insert_Inventory_Disc_Trail";
_insert_inventory_disc_trail();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 860;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 861;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 863;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 864;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_inventory_ref() throws Exception{
ResumableSub_Delete_Inventory_Ref rsub = new ResumableSub_Delete_Inventory_Ref(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Delete_Inventory_Ref extends BA.ResumableSub {
public ResumableSub_Delete_Inventory_Ref(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
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
 //BA.debugLineNum = 669;BA.debugLine="error_trigger = 0";
parent._error_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 670;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 671;BA.debugLine="PANEL_BG_MSGBOX.BringToFront";
parent.mostCurrent._panel_bg_msgbox.BringToFront();
 //BA.debugLineNum = 672;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 1;
;
 //BA.debugLineNum = 673;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_inve";
_cmd = _createcommand("delete_inventory_ref",new Object[]{(Object)(parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ )});
 //BA.debugLineNum = 674;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 675;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 14;
return;
case 14:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 676;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 677;BA.debugLine="Insert_Inventory_Ref";
_insert_inventory_ref();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 679;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 680;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 681;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 6;
;
 //BA.debugLineNum = 682;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 683;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 684;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 685;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 686;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 687;BA.debugLine="Delete_Inventory_Ref";
_delete_inventory_ref();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 689;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 690;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 693;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 694;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _delete_inventory_ref_trail() throws Exception{
ResumableSub_Delete_Inventory_Ref_Trail rsub = new ResumableSub_Delete_Inventory_Ref_Trail(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Delete_Inventory_Ref_Trail extends BA.ResumableSub {
public ResumableSub_Delete_Inventory_Ref_Trail(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
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
 //BA.debugLineNum = 735;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"delete_inve";
_cmd = _createcommand("delete_inventory_ref_trail",new Object[]{(Object)(parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ )});
 //BA.debugLineNum = 736;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(Ar";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 737;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 13;
return;
case 13:
//C
this.state = 1;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 738;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 739;BA.debugLine="Insert_Inventory_Ref_Trail";
_insert_inventory_ref_trail();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 741;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO SE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 742;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 743;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 14;
return;
case 14:
//C
this.state = 6;
;
 //BA.debugLineNum = 744;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 745;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 746;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 15;
return;
case 15:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 747;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 748;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 749;BA.debugLine="Delete_Inventory_Ref";
_delete_inventory_ref();
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 751;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 752;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 755;BA.debugLine="js.Release";
_js._release /*String*/ ();
 //BA.debugLineNum = 756;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 634;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 635;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 636;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 637;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 638;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 641;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 642;BA.debugLine="End Sub";
return null;
}
public static String  _getvalue(String _op) throws Exception{
 //BA.debugLineNum = 1032;BA.debugLine="Sub GetValue(Op As String)";
 //BA.debugLineNum = 1033;BA.debugLine="If Op0 = \"e\" And (Op = \"s\" Or Op = \"g\" Or Op = \"x";
if ((_op0).equals("e") && ((_op).equals("s") || (_op).equals("g") || (_op).equals("x"))) { 
 //BA.debugLineNum = 1034;BA.debugLine="Val = Result1";
_val = _result1;
 }else {
 //BA.debugLineNum = 1036;BA.debugLine="Val = sVal";
_val = (double)(Double.parseDouble(_sval));
 };
 //BA.debugLineNum = 1039;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1041;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1042;BA.debugLine="If Op0 = \"a\" Or Op0 = \"b\" Or Op0 = \"c\" Or Op0 =";
if ((_op0).equals("a") || (_op0).equals("b") || (_op0).equals("c") || (_op0).equals("d") || (_op0).equals("y")) { 
 //BA.debugLineNum = 1043;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1044;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1046;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1047;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1048;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1049;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1050;BA.debugLine="Op0 = \"e\"";
_op0 = "e";
 //BA.debugLineNum = 1051;BA.debugLine="Op = \"e\"";
_op = "e";
 //BA.debugLineNum = 1052;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1055;BA.debugLine="If New1 = 1 Then";
if (_new1==1) { 
 //BA.debugLineNum = 1056;BA.debugLine="Result1 = Val";
_result1 = _val;
 //BA.debugLineNum = 1057;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1058;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1059;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1060;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1061;BA.debugLine="Op = \"e\"";
_op = "e";
 };
 //BA.debugLineNum = 1063;BA.debugLine="UpdateTape";
_updatetape();
 //BA.debugLineNum = 1064;BA.debugLine="New1 = 2";
_new1 = (int) (2);
 //BA.debugLineNum = 1065;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1066;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1069;BA.debugLine="If Op = \"e\" Then";
if ((_op).equals("e")) { 
 //BA.debugLineNum = 1070;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1071;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1073;BA.debugLine="Txt = Txt & CRLF & \" =  \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+" =  ";
 //BA.debugLineNum = 1074;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1075;BA.debugLine="Txt = Txt & Result1";
_txt = _txt+BA.NumberToString(_result1);
 }else {
 //BA.debugLineNum = 1077;BA.debugLine="If Op0 = \"g\" Or Op0 = \"s\" Or Op0 = \"x\" Then";
if ((_op0).equals("g") || (_op0).equals("s") || (_op0).equals("x")) { 
 //BA.debugLineNum = 1078;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1079;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1080;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1083;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1084;BA.debugLine="If Op0<>\"e\" Then";
if ((_op0).equals("e") == false) { 
 //BA.debugLineNum = 1085;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1087;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1088;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1089;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1090;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1091;BA.debugLine="Op = \"e\"";
_op = "e";
 };
 };
 //BA.debugLineNum = 1094;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1095;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 54;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 59;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 61;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 62;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private TABLE_INVENTORY As B4XTable";
mostCurrent._table_inventory = new wingan.app.b4xtable();
 //BA.debugLineNum = 65;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 66;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 67;BA.debugLine="Private NameColumn(9) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (9)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 69;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 72;BA.debugLine="Dim btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn";
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
 //BA.debugLineNum = 73;BA.debugLine="Dim btnBack, btnClr, btnExit As Button";
mostCurrent._btnback = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnclr = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnexit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Dim lblPaperRoll As Label";
mostCurrent._lblpaperroll = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Dim scvPaperRoll As ScrollView";
mostCurrent._scvpaperroll = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Dim pnlKeyboard As Panel";
mostCurrent._pnlkeyboard = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Dim stu As StringUtils";
mostCurrent._stu = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 79;BA.debugLine="Private LABEL_LOAD_NUMBER As Label";
mostCurrent._label_load_number = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private LABEL_LOAD_STATUS As Label";
mostCurrent._label_load_status = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private LABEL_LOAD_PRINCIPAL As Label";
mostCurrent._label_load_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private LABEL_LOAD_VARIANT As Label";
mostCurrent._label_load_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private LABEL_LOAD_DESCRIPTION As Label";
mostCurrent._label_load_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 85;BA.debugLine="Private EDITTEXT_QUANTITY As EditText";
mostCurrent._edittext_quantity = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private PANEL_BG_RECOUNT As Panel";
mostCurrent._panel_bg_recount = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Private BUTTON_EDIT As Button";
mostCurrent._button_edit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 88;BA.debugLine="Private BUTTON_DELETE As Button";
mostCurrent._button_delete = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private BUTTON_RECOUNT As Button";
mostCurrent._button_recount = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Private LABEL_MSGBOX2 As Label";
mostCurrent._label_msgbox2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Private PANEL_BG_MSGBOX As Panel";
mostCurrent._panel_bg_msgbox = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private PANEL_BG_CALCU As Panel";
mostCurrent._panel_bg_calcu = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private BUTTON_CALCU As Button";
mostCurrent._button_calcu = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private LABEL_LOAD_ANSWER As Label";
mostCurrent._label_load_answer = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 95;BA.debugLine="End Sub";
return "";
}
public static void  _insert_inventory_disc() throws Exception{
ResumableSub_Insert_Inventory_Disc rsub = new ResumableSub_Insert_Inventory_Disc(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Insert_Inventory_Disc extends BA.ResumableSub {
public ResumableSub_Insert_Inventory_Disc(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
int _i3 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
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
 //BA.debugLineNum = 816;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM inv";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_disc_table WHERE transaction_id = '"+parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ +"'")));
 //BA.debugLineNum = 817;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 25;
if (parent._cursor3.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 818;BA.debugLine="For i3 = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step3 = 1;
limit3 = (int) (parent._cursor3.getRowCount()-1);
_i3 = (int) (0) ;
this.state = 26;
if (true) break;

case 26:
//C
this.state = 13;
if ((step3 > 0 && _i3 <= limit3) || (step3 < 0 && _i3 >= limit3)) this.state = 6;
if (true) break;

case 27:
//C
this.state = 26;
_i3 = ((int)(0 + _i3 + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 819;BA.debugLine="cursor3.Position = i3";
parent._cursor3.setPosition(_i3);
 //BA.debugLineNum = 820;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_in";
_cmd = _createcommand("insert_inventory_disc",new Object[]{(Object)(parent._cursor3.GetString("transaction_id")),(Object)(parent._cursor3.GetString("transaction_number")),(Object)(parent._cursor3.GetString("principal_id")),(Object)(parent._cursor3.GetString("principal_name")),(Object)(parent._cursor3.GetString("product_id")),(Object)(parent._cursor3.GetString("product_variant")),(Object)(parent._cursor3.GetString("product_description")),(Object)(parent._cursor3.GetString("unit")),(Object)(parent._cursor3.GetString("quantity")),(Object)(parent._cursor3.GetString("total_pieces")),(Object)(parent._cursor3.GetString("position")),(Object)(parent._cursor3.GetString("input_type")),(Object)(parent._cursor3.GetString("reason")),(Object)(parent._cursor3.GetString("scan_code")),(Object)(parent._cursor3.GetString("inventory_status")),(Object)(parent._cursor3.GetString("count_by")),(Object)(parent._cursor3.GetString("count_person1")),(Object)(parent._cursor3.GetString("count_person2")),(Object)(parent._cursor3.GetString("recount_by")),(Object)(parent._cursor3.GetString("recount_person2")),(Object)(parent._cursor3.GetString("recount_person1")),(Object)(parent._cursor3.GetString("date_registered")),(Object)(parent._cursor3.GetString("time_registered")),(Object)(parent._cursor3.GetString("recount_date")),(Object)(parent._cursor3.GetString("recount_time")),(Object)(parent._cursor3.GetString("edit_count")),(Object)(parent._cursor3.GetString("tab_id"))});
 //BA.debugLineNum = 825;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 826;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 28;
return;
case 28:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 827;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 828;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading Data: \" & curs";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading Data: "+parent._cursor3.GetString("product_description")));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 830;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("763242255","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 831;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 832;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 833;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 29;
return;
case 29:
//C
this.state = 12;
;
 //BA.debugLineNum = 834;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 835;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 if (true) break;

case 12:
//C
this.state = 27;
;
 //BA.debugLineNum = 837;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 839;BA.debugLine="If error_trigger = 0 Then";

case 13:
//if
this.state = 24;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 24;
 //BA.debugLineNum = 840;BA.debugLine="Delete_Inventory_Disc_Trail";
_delete_inventory_disc_trail();
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 842;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 843;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 18;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 844;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 18:
//if
this.state = 23;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 20;
}else {
this.state = 22;
}if (true) break;

case 20:
//C
this.state = 23;
 //BA.debugLineNum = 845;BA.debugLine="Delete_Inventory_Ref";
_delete_inventory_ref();
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 847;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 848;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 23:
//C
this.state = 24;
;
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = -1;
;
 //BA.debugLineNum = 852;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_inventory_disc_trail() throws Exception{
ResumableSub_Insert_Inventory_Disc_Trail rsub = new ResumableSub_Insert_Inventory_Disc_Trail(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Insert_Inventory_Disc_Trail extends BA.ResumableSub {
public ResumableSub_Insert_Inventory_Disc_Trail(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
int _i4 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
String _query = "";
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
 //BA.debugLineNum = 866;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM inv";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_disc_table_trail WHERE transaction_id = '"+parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ +"'")));
 //BA.debugLineNum = 867;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 14;
if (parent._cursor4.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 868;BA.debugLine="For i4 = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step3 = 1;
limit3 = (int) (parent._cursor4.getRowCount()-1);
_i4 = (int) (0) ;
this.state = 26;
if (true) break;

case 26:
//C
this.state = 13;
if ((step3 > 0 && _i4 <= limit3) || (step3 < 0 && _i4 >= limit3)) this.state = 6;
if (true) break;

case 27:
//C
this.state = 26;
_i4 = ((int)(0 + _i4 + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 869;BA.debugLine="cursor4.Position = i4";
parent._cursor4.setPosition(_i4);
 //BA.debugLineNum = 870;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_in";
_cmd = _createcommand("insert_inventory_disc_trail",new Object[]{(Object)(parent._cursor4.GetString("transaction_id")),(Object)(parent._cursor4.GetString("transaction_number")),(Object)(parent._cursor4.GetString("principal_id")),(Object)(parent._cursor4.GetString("principal_name")),(Object)(parent._cursor4.GetString("product_id")),(Object)(parent._cursor4.GetString("product_variant")),(Object)(parent._cursor4.GetString("product_description")),(Object)(parent._cursor4.GetString("unit")),(Object)(parent._cursor4.GetString("quantity")),(Object)(parent._cursor4.GetString("total_pieces")),(Object)(parent._cursor4.GetString("position")),(Object)(parent._cursor4.GetString("input_type")),(Object)(parent._cursor4.GetString("reason")),(Object)(parent._cursor4.GetString("scan_code")),(Object)(parent._cursor4.GetString("inventory_status")),(Object)(parent._cursor4.GetString("count_by")),(Object)(parent._cursor4.GetString("count_person1")),(Object)(parent._cursor4.GetString("count_person2")),(Object)(parent._cursor4.GetString("recount_by")),(Object)(parent._cursor4.GetString("recount_person1")),(Object)(parent._cursor4.GetString("recount_person2")),(Object)(parent._cursor4.GetString("date_registered")),(Object)(parent._cursor4.GetString("time_registered")),(Object)(parent._cursor4.GetString("recount_date")),(Object)(parent._cursor4.GetString("recount_time")),(Object)(parent._cursor4.GetString("edit_count")),(Object)(parent._cursor4.GetString("tab_id")),(Object)(parent._cursor4.GetString("edit_by")),(Object)(parent._cursor4.GetString("edit_type")),(Object)(parent._cursor4.GetString("edit_date")),(Object)(parent._cursor4.GetString("edit_time"))});
 //BA.debugLineNum = 876;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 877;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 28;
return;
case 28:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 878;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 879;BA.debugLine="LABEL_MSGBOX2.Text = \"Data uploading complete.";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data uploading complete..."));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 881;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("763373328","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 882;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 883;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 884;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 29;
return;
case 29:
//C
this.state = 12;
;
 //BA.debugLineNum = 885;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 886;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 if (true) break;

case 12:
//C
this.state = 27;
;
 //BA.debugLineNum = 888;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 if (true) break;
;
 //BA.debugLineNum = 891;BA.debugLine="If error_trigger = 0 Then";

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
 //BA.debugLineNum = 892;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 30;
return;
case 30:
//C
this.state = 25;
;
 //BA.debugLineNum = 893;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading Successful...\"";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading Successful..."));
 //BA.debugLineNum = 894;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 31;
return;
case 31:
//C
this.state = 25;
;
 //BA.debugLineNum = 895;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 896;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 32;
return;
case 32:
//C
this.state = 25;
;
 //BA.debugLineNum = 897;BA.debugLine="Dim query As String = \"UPDATE inventory_ref_table";
_query = "UPDATE inventory_ref_table SET transaction_status = 'UPLOADED' WHERE transaction_id = ?";
 //BA.debugLineNum = 898;BA.debugLine="connection.ExecNonQuery2(query,Array As String(MO";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ }));
 //BA.debugLineNum = 899;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 33;
return;
case 33:
//C
this.state = 25;
;
 //BA.debugLineNum = 900;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 901;BA.debugLine="StartActivity(MONTHLY_INVENTORY_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._monthly_inventory_module.getObject()));
 //BA.debugLineNum = 902;BA.debugLine="SetAnimation(\"zoom_enter\", \"zoom_exit\")";
_setanimation("zoom_enter","zoom_exit");
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 904;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 905;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 34;
return;
case 34:
//C
this.state = 19;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 906;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 907;BA.debugLine="Delete_Inventory_Ref";
_delete_inventory_ref();
 if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 909;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 910;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = -1;
;
 //BA.debugLineNum = 913;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_inventory_ref() throws Exception{
ResumableSub_Insert_Inventory_Ref rsub = new ResumableSub_Insert_Inventory_Ref(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Insert_Inventory_Ref extends BA.ResumableSub {
public ResumableSub_Insert_Inventory_Ref(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
String _date = "";
String _time = "";
int _i1 = 0;
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
 //BA.debugLineNum = 696;BA.debugLine="Dim Date As String = DateTime.Date(DateTime.Now)";
_date = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 697;BA.debugLine="Dim Time As String = DateTime.Time(DateTime.Now)";
_time = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 698;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM inv";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_ref_table WHERE transaction_id = '"+parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ +"'")));
 //BA.debugLineNum = 699;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 25;
if (parent._cursor1.getRowCount()>0) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 700;BA.debugLine="For i1 = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step5 = 1;
limit5 = (int) (parent._cursor1.getRowCount()-1);
_i1 = (int) (0) ;
this.state = 26;
if (true) break;

case 26:
//C
this.state = 13;
if ((step5 > 0 && _i1 <= limit5) || (step5 < 0 && _i1 >= limit5)) this.state = 6;
if (true) break;

case 27:
//C
this.state = 26;
_i1 = ((int)(0 + _i1 + step5)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 701;BA.debugLine="cursor1.Position = i1";
parent._cursor1.setPosition(_i1);
 //BA.debugLineNum = 702;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_in";
_cmd = _createcommand("insert_inventory_ref",new Object[]{(Object)(parent._cursor1.GetString("transaction_id")),(Object)(parent._cursor1.GetString("principal_name")),(Object)(parent._cursor1.GetString("principal_id")),(Object)(parent._cursor1.GetString("inventory_date")),(Object)(parent._cursor1.GetString("warehouse")),(Object)(parent._cursor1.GetString("area")),(Object)(parent._cursor1.GetString("user_info")),(Object)(parent._cursor1.GetString("tab_id")),(Object)(parent._cursor1.GetString("date_registered")),(Object)(parent._cursor1.GetString("time_registered")),(Object)(parent._cursor1.GetString("edit_count")),(Object)(parent._cursor1.GetString("date_updated")),(Object)(parent._cursor1.GetString("time_updated")),(Object)(parent._cursor1.GetString("transaction_status")),(Object)(_date+" "+_time),(Object)(parent.mostCurrent._login_module._username /*String*/ )});
 //BA.debugLineNum = 705;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 706;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 28;
return;
case 28:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 707;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 708;BA.debugLine="LABEL_MSGBOX2.Text = \"Uploading transaction :";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Uploading transaction : "+parent._cursor1.GetString("principal_name")+" "+parent._cursor1.GetString("warehouse")+" "+parent._cursor1.GetString("inventory_date")+"..."));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 710;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 711;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("762980112","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 712;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 713;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 714;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 29;
return;
case 29:
//C
this.state = 12;
;
 //BA.debugLineNum = 715;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 12:
//C
this.state = 27;
;
 //BA.debugLineNum = 717;BA.debugLine="js.Release";
_js._release /*String*/ ();
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 719;BA.debugLine="If error_trigger = 0 Then";

case 13:
//if
this.state = 24;
if ((parent._error_trigger).equals(BA.NumberToString(0))) { 
this.state = 15;
}else {
this.state = 17;
}if (true) break;

case 15:
//C
this.state = 24;
 //BA.debugLineNum = 720;BA.debugLine="Delete_Inventory_Ref_Trail";
_delete_inventory_ref_trail();
 if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 722;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 723;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 18;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 724;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 18:
//if
this.state = 23;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 20;
}else {
this.state = 22;
}if (true) break;

case 20:
//C
this.state = 23;
 //BA.debugLineNum = 725;BA.debugLine="Delete_Inventory_Ref";
_delete_inventory_ref();
 if (true) break;

case 22:
//C
this.state = 23;
 //BA.debugLineNum = 727;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 728;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Failed"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 23:
//C
this.state = 24;
;
 if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = -1;
;
 //BA.debugLineNum = 733;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _insert_inventory_ref_trail() throws Exception{
ResumableSub_Insert_Inventory_Ref_Trail rsub = new ResumableSub_Insert_Inventory_Ref_Trail(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Insert_Inventory_Ref_Trail extends BA.ResumableSub {
public ResumableSub_Insert_Inventory_Ref_Trail(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
int _i2 = 0;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _js = null;
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
 //BA.debugLineNum = 758;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM inv";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_ref_table_trail WHERE transaction_id = '"+parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ +"'")));
 //BA.debugLineNum = 759;BA.debugLine="If cursor2.RowCount > 0 Then";
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
 //BA.debugLineNum = 760;BA.debugLine="For i2 = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step3 = 1;
limit3 = (int) (parent._cursor2.getRowCount()-1);
_i2 = (int) (0) ;
this.state = 27;
if (true) break;

case 27:
//C
this.state = 13;
if ((step3 > 0 && _i2 <= limit3) || (step3 < 0 && _i2 >= limit3)) this.state = 6;
if (true) break;

case 28:
//C
this.state = 27;
_i2 = ((int)(0 + _i2 + step3)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 761;BA.debugLine="cursor2.Position = i2";
parent._cursor2.setPosition(_i2);
 //BA.debugLineNum = 762;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"insert_in";
_cmd = _createcommand("insert_inventory_ref_trail",new Object[]{(Object)(parent._cursor2.GetString("transaction_id")),(Object)(parent._cursor2.GetString("principal_name")),(Object)(parent._cursor2.GetString("principal_id")),(Object)(parent._cursor2.GetString("inventory_date")),(Object)(parent._cursor2.GetString("warehouse")),(Object)(parent._cursor2.GetString("area")),(Object)(parent._cursor2.GetString("user_info")),(Object)(parent._cursor2.GetString("tab_id")),(Object)(parent._cursor2.GetString("date_registered")),(Object)(parent._cursor2.GetString("time_registered")),(Object)(parent._cursor2.GetString("transaction_status")),(Object)(parent._cursor2.GetString("edit_count")),(Object)(parent._cursor2.GetString("edit_type")),(Object)(parent._cursor2.GetString("edit_by"))});
 //BA.debugLineNum = 765;BA.debugLine="Dim js As HttpJob = CreateRequest.ExecuteBatch(";
_js = _createrequest()._executebatch /*wingan.app.httpjob*/ (anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(_cmd)}),anywheresoftware.b4a.keywords.Common.Null);
 //BA.debugLineNum = 766;BA.debugLine="Wait For(js) JobDone(js As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_js));
this.state = 29;
return;
case 29:
//C
this.state = 7;
_js = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 767;BA.debugLine="If js.Success Then";
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
 //BA.debugLineNum = 768;BA.debugLine="LABEL_MSGBOX2.Text = \"Data ready for upload...";
parent.mostCurrent._label_msgbox2.setText(BA.ObjectToCharSequence("Data ready for upload... "));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 770;BA.debugLine="Log(\"ERROR: \" & js.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("763111181","ERROR: "+_js._errormessage /*String*/ ,0);
 //BA.debugLineNum = 771;BA.debugLine="Msgbox2Async(\"SYSTEM NETWORK CAN'T CONNECT TO";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("SYSTEM NETWORK CAN'T CONNECT TO SERVER."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 772;BA.debugLine="ToastMessageShow(\"Uploading Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Uploading Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 773;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 30;
return;
case 30:
//C
this.state = 12;
;
 //BA.debugLineNum = 774;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 775;BA.debugLine="error_trigger = 1";
parent._error_trigger = BA.NumberToString(1);
 if (true) break;

case 12:
//C
this.state = 28;
;
 //BA.debugLineNum = 777;BA.debugLine="js.Release";
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
 //BA.debugLineNum = 780;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 31;
return;
case 31:
//C
this.state = 15;
;
 //BA.debugLineNum = 781;BA.debugLine="If error_trigger = 0 Then";
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
 //BA.debugLineNum = 782;BA.debugLine="Delete_Inventory_Disc";
_delete_inventory_disc();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 784;BA.debugLine="Msgbox2Async(\"Uploading failed, do you want to t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Uploading failed, do you want to try uploading again?"),BA.ObjectToCharSequence("WARNING!"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 785;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 32;
return;
case 32:
//C
this.state = 20;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 786;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 787;BA.debugLine="Delete_Inventory_Ref";
_delete_inventory_ref();
 if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 789;BA.debugLine="PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_msgbox.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 790;BA.debugLine="ToastMessageShow(\"Updating Failed\", False)";
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
 //BA.debugLineNum = 793;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_inventory_table() throws Exception{
ResumableSub_LOAD_INVENTORY_TABLE rsub = new ResumableSub_LOAD_INVENTORY_TABLE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_INVENTORY_TABLE extends BA.ResumableSub {
public ResumableSub_LOAD_INVENTORY_TABLE(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
anywheresoftware.b4a.objects.collections.List _data = null;
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
Object[] _row = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 192;BA.debugLine="ProgressDialogShow2(\"Loading...\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 193;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 1;
;
 //BA.debugLineNum = 194;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 195;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 196;BA.debugLine="Dim rs As ResultSet = connection.ExecQuery(\"SELEC";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_disc_table WHERE transaction_id = '"+parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ +"' ORDER BY inventory_status ASC")));
 //BA.debugLineNum = 197;BA.debugLine="Do While rs.NextRow";
if (true) break;

case 1:
//do while
this.state = 4;
while (_rs.NextRow()) {
this.state = 3;
if (true) break;
}
if (true) break;

case 3:
//C
this.state = 1;
 //BA.debugLineNum = 198;BA.debugLine="Dim row(9) As Object";
_row = new Object[(int) (9)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 199;BA.debugLine="row(0) = rs.GetString(\"transaction_number\")";
_row[(int) (0)] = (Object)(_rs.GetString("transaction_number"));
 //BA.debugLineNum = 200;BA.debugLine="row(1) = rs.GetString(\"position\")";
_row[(int) (1)] = (Object)(_rs.GetString("position"));
 //BA.debugLineNum = 201;BA.debugLine="row(2) = rs.GetString(\"principal_name\")";
_row[(int) (2)] = (Object)(_rs.GetString("principal_name"));
 //BA.debugLineNum = 202;BA.debugLine="row(3) = rs.GetString(\"product_variant\")";
_row[(int) (3)] = (Object)(_rs.GetString("product_variant"));
 //BA.debugLineNum = 205;BA.debugLine="row(4) = rs.GetString(\"product_description\")";
_row[(int) (4)] = (Object)(_rs.GetString("product_description"));
 //BA.debugLineNum = 206;BA.debugLine="row(5) = rs.GetString(\"unit\")";
_row[(int) (5)] = (Object)(_rs.GetString("unit"));
 //BA.debugLineNum = 207;BA.debugLine="row(6) = rs.GetString(\"quantity\")";
_row[(int) (6)] = (Object)(_rs.GetString("quantity"));
 //BA.debugLineNum = 208;BA.debugLine="row(7) = rs.GetString(\"total_pieces\")";
_row[(int) (7)] = (Object)(_rs.GetString("total_pieces"));
 //BA.debugLineNum = 209;BA.debugLine="row(8) = rs.GetString(\"inventory_status\")";
_row[(int) (8)] = (Object)(_rs.GetString("inventory_status"));
 //BA.debugLineNum = 210;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 4:
//C
this.state = 5;
;
 //BA.debugLineNum = 212;BA.debugLine="rs.Close";
_rs.Close();
 //BA.debugLineNum = 213;BA.debugLine="TABLE_INVENTORY.SetData(Data)";
parent.mostCurrent._table_inventory._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 214;BA.debugLine="If XSelections.IsInitialized = False Then";
if (true) break;

case 5:
//if
this.state = 8;
if (parent.mostCurrent._xselections.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 7;
}if (true) break;

case 7:
//C
this.state = 8;
 //BA.debugLineNum = 215;BA.debugLine="XSelections.Initialize(TABLE_INVENTORY)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_inventory);
 //BA.debugLineNum = 216;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_P";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 218;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 219;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_table_header() throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub LOAD_TABLE_HEADER";
 //BA.debugLineNum = 178;BA.debugLine="NameColumn(0)=TABLE_INVENTORY.AddColumn(\"#\", TABL";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("#",mostCurrent._table_inventory._column_type_numbers /*int*/ );
 //BA.debugLineNum = 179;BA.debugLine="NameColumn(1)=TABLE_INVENTORY.AddColumn(\"Position";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Position",mostCurrent._table_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 180;BA.debugLine="NameColumn(2)=TABLE_INVENTORY.AddColumn(\"Principa";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Principal",mostCurrent._table_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 181;BA.debugLine="NameColumn(3)=TABLE_INVENTORY.AddColumn(\"Product";
mostCurrent._namecolumn[(int) (3)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Variant",mostCurrent._table_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 182;BA.debugLine="NameColumn(4)=TABLE_INVENTORY.AddColumn(\"Product";
mostCurrent._namecolumn[(int) (4)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Description",mostCurrent._table_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 183;BA.debugLine="NameColumn(5)=TABLE_INVENTORY.AddColumn(\"Unit\", T";
mostCurrent._namecolumn[(int) (5)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Unit",mostCurrent._table_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 184;BA.debugLine="NameColumn(6)=TABLE_INVENTORY.AddColumn(\"Qty\", TA";
mostCurrent._namecolumn[(int) (6)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Qty",mostCurrent._table_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 185;BA.debugLine="NameColumn(7)=TABLE_INVENTORY.AddColumn(\"Total Pi";
mostCurrent._namecolumn[(int) (7)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Total Pieces",mostCurrent._table_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 186;BA.debugLine="NameColumn(8)=TABLE_INVENTORY.AddColumn(\"Status\",";
mostCurrent._namecolumn[(int) (8)] = mostCurrent._table_inventory._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Status",mostCurrent._table_inventory._column_type_text /*int*/ );
 //BA.debugLineNum = 188;BA.debugLine="TABLE_INVENTORY.NumberOfFrozenColumns = 2";
mostCurrent._table_inventory._numberoffrozencolumns /*int*/  = (int) (2);
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return "";
}
public static String  _loadtexts() throws Exception{
String _filename = "";
int _iq = 0;
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _reader = null;
 //BA.debugLineNum = 1205;BA.debugLine="Sub LoadTexts";
 //BA.debugLineNum = 1206;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 1207;BA.debugLine="Dim iq As Int";
_iq = 0;
 //BA.debugLineNum = 1211;BA.debugLine="FileName = \"tapecalc_\" & LanguageID & \".txt\"";
_filename = "tapecalc_"+_languageid+".txt";
 //BA.debugLineNum = 1212;BA.debugLine="If File.Exists(ProgPath, FileName) = False Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_progpath,_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1213;BA.debugLine="FileName = \"tapecalc_en.txt\"";
_filename = "tapecalc_en.txt";
 };
 //BA.debugLineNum = 1216;BA.debugLine="Dim Reader As TextReader";
_reader = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1217;BA.debugLine="Reader.Initialize(File.OpenInput(ProgPath, FileNa";
_reader.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_progpath,_filename).getObject()));
 //BA.debugLineNum = 1218;BA.debugLine="iq = 0";
_iq = (int) (0);
 //BA.debugLineNum = 1219;BA.debugLine="Texts(iq) = Reader.ReadLine";
_texts[_iq] = _reader.ReadLine();
 //BA.debugLineNum = 1220;BA.debugLine="Do While Texts(iq) <> Null";
while (_texts[_iq]!= null) {
 //BA.debugLineNum = 1221;BA.debugLine="iq = iq + 1";
_iq = (int) (_iq+1);
 //BA.debugLineNum = 1222;BA.debugLine="Texts(iq) = Reader.ReadLine";
_texts[_iq] = _reader.ReadLine();
 }
;
 //BA.debugLineNum = 1224;BA.debugLine="Reader.Close";
_reader.Close();
 //BA.debugLineNum = 1225;BA.debugLine="End Sub";
return "";
}
public static void  _next_item() throws Exception{
ResumableSub_NEXT_ITEM rsub = new ResumableSub_NEXT_ITEM(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_NEXT_ITEM extends BA.ResumableSub {
public ResumableSub_NEXT_ITEM(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
int _result = 0;
String _query = "";
int _i = 0;
int step9;
int limit9;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 488;BA.debugLine="Msgbox2Async(\"Item #(\"&LABEL_LOAD_NUMBER.Text&\"),";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Item #("+parent.mostCurrent._label_load_number.getText()+"), Product : "+parent.mostCurrent._label_load_description.getText()+", "+parent.mostCurrent._edittext_quantity.getText()+" "+parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"  been recounted. Would you like to proceed to next item?."),BA.ObjectToCharSequence("Next Item"),"Next","","Back",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 489;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 23;
return;
case 23:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 490;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 22;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else {
this.state = 21;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 491;BA.debugLine="ProgressDialogShow2(\"Loading next item...\", Fals";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading next item..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 492;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 24;
return;
case 24:
//C
this.state = 4;
;
 //BA.debugLineNum = 493;BA.debugLine="Dim query As String = \"SELECT * FROM inventory_d";
_query = "SELECT * FROM inventory_disc_table WHERE transaction_id = ? ORDER BY inventory_status ASC LIMIT 1";
 //BA.debugLineNum = 494;BA.debugLine="cursor3 = connection.ExecQuery2(query,Array As S";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery2(_query,new String[]{parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ })));
 //BA.debugLineNum = 495;BA.debugLine="If cursor3.RowCount = 1 Then";
if (true) break;

case 4:
//if
this.state = 19;
if (parent._cursor3.getRowCount()==1) { 
this.state = 6;
}else {
this.state = 18;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 496;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 16;
step9 = 1;
limit9 = (int) (parent._cursor3.getRowCount()-1);
_i = (int) (0) ;
this.state = 25;
if (true) break;

case 25:
//C
this.state = 16;
if ((step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9)) this.state = 9;
if (true) break;

case 26:
//C
this.state = 25;
_i = ((int)(0 + _i + step9)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 497;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 27;
return;
case 27:
//C
this.state = 10;
;
 //BA.debugLineNum = 498;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 499;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor3.GetString(";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("principal_name")));
 //BA.debugLineNum = 500;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 501;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_description")));
 //BA.debugLineNum = 502;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 503;BA.debugLine="CMB_UNIT.cmbBox.Add(cursor3.GetString(\"unit\"))";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor3.GetString("unit"));
 //BA.debugLineNum = 504;BA.debugLine="EDITTEXT_QUANTITY.Text = cursor3.GetString(\"qu";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("quantity")));
 //BA.debugLineNum = 505;BA.debugLine="LABEL_LOAD_NUMBER.Text = cursor3.GetString(\"tr";
parent.mostCurrent._label_load_number.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("transaction_number")));
 //BA.debugLineNum = 507;BA.debugLine="If cursor3.GetString(\"inventory_status\") = \"CO";
if (true) break;

case 10:
//if
this.state = 15;
if ((parent._cursor3.GetString("inventory_status")).equals("COUNT")) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 15;
 //BA.debugLineNum = 508;BA.debugLine="LABEL_LOAD_STATUS.Text = \" COUNTED\"";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence(" COUNTED"));
 //BA.debugLineNum = 509;BA.debugLine="LABEL_LOAD_STATUS.Color = Colors.Red";
parent.mostCurrent._label_load_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 511;BA.debugLine="LABEL_LOAD_STATUS.Text = \" RECOUNT\"";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence(" RECOUNT"));
 //BA.debugLineNum = 512;BA.debugLine="LABEL_LOAD_STATUS.Color = Colors.Green";
parent.mostCurrent._label_load_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 if (true) break;

case 15:
//C
this.state = 26;
;
 if (true) break;
if (true) break;

case 16:
//C
this.state = 19;
;
 //BA.debugLineNum = 515;BA.debugLine="ProgressDialogHide()";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 516;BA.debugLine="CMB_UNIT.cmbBox.Enabled = False";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 517;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 519;BA.debugLine="Msgbox2Async(\"You've reach the last number of t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You've reach the last number of transaction."),BA.ObjectToCharSequence(""),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 520;BA.debugLine="PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_recount.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 19:
//C
this.state = 22;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 523;BA.debugLine="PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_recount.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 524;BA.debugLine="ToastMessageShow(\"Exit next item.\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Exit next item."),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 22:
//C
this.state = -1;
;
 //BA.debugLineNum = 526;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _oncal() throws Exception{
 //BA.debugLineNum = 917;BA.debugLine="Sub oncal";
 //BA.debugLineNum = 918;BA.debugLine="LoadTexts";
_loadtexts();
 //BA.debugLineNum = 920;BA.debugLine="Scale_Calc.SetRate(0.6)";
mostCurrent._scale_calc._setrate /*String*/ (mostCurrent.activityBA,0.6);
 //BA.debugLineNum = 921;BA.debugLine="ScaleAuto = Scale_Calc.GetScaleDS";
_scaleauto = mostCurrent._scale_calc._getscaleds /*double*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 938;BA.debugLine="lblPaperRoll.Initialize(\"lblPaperRoll\")";
mostCurrent._lblpaperroll.Initialize(mostCurrent.activityBA,"lblPaperRoll");
 //BA.debugLineNum = 939;BA.debugLine="scvPaperRoll.Panel.AddView(lblPaperRoll, 0, 0, 10";
mostCurrent._scvpaperroll.getPanel().AddView((android.view.View)(mostCurrent._lblpaperroll.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 940;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 941;BA.debugLine="lblPaperRoll.TextSize = 22 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (22*_scaleauto));
 //BA.debugLineNum = 942;BA.debugLine="lblPaperRoll.Color = Colors.White";
mostCurrent._lblpaperroll.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 943;BA.debugLine="lblPaperRoll.TextColor = Colors.Black";
mostCurrent._lblpaperroll.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 959;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 171;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 172;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 173;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 174;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _operation(String _op) throws Exception{
 //BA.debugLineNum = 1096;BA.debugLine="Sub Operation(Op As String)";
 //BA.debugLineNum = 1097;BA.debugLine="Select Op";
switch (BA.switchObjectToInt(_op,"a","b","c","d","g","s","x","y")) {
case 0: {
 //BA.debugLineNum = 1099;BA.debugLine="Txt = Txt & CRLF & \"+ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"+ ";
 break; }
case 1: {
 //BA.debugLineNum = 1101;BA.debugLine="Txt = Txt & CRLF & \"- \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"- ";
 break; }
case 2: {
 //BA.debugLineNum = 1103;BA.debugLine="Txt = Txt & CRLF & \"× \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"× ";
 break; }
case 3: {
 //BA.debugLineNum = 1105;BA.debugLine="Txt = Txt & CRLF & \"/ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"/ ";
 break; }
case 4: {
 //BA.debugLineNum = 1107;BA.debugLine="Txt = Txt & CRLF & \"x2 \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"x2 ";
 break; }
case 5: {
 //BA.debugLineNum = 1109;BA.debugLine="Txt = Txt & CRLF & \"√ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"√ ";
 break; }
case 6: {
 //BA.debugLineNum = 1111;BA.debugLine="Txt = Txt & CRLF & \"1/x \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"1/x ";
 break; }
case 7: {
 //BA.debugLineNum = 1113;BA.debugLine="Txt = Txt & CRLF & \"% \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"% ";
 break; }
}
;
 //BA.debugLineNum = 1115;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_calcu_click() throws Exception{
 //BA.debugLineNum = 1236;BA.debugLine="Sub PANEL_BG_CALCU_Click";
 //BA.debugLineNum = 1237;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1238;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_recount_click() throws Exception{
 //BA.debugLineNum = 320;BA.debugLine="Sub PANEL_BG_RECOUNT_Click";
 //BA.debugLineNum = 321;BA.debugLine="PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_recount.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 322;BA.debugLine="TABLE_INVENTORY.Refresh";
mostCurrent._table_inventory._refresh /*String*/ ();
 //BA.debugLineNum = 323;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 //BA.debugLineNum = 324;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
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
 //BA.debugLineNum = 19;BA.debugLine="Dim phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 22;BA.debugLine="Dim TTS1 As TTS";
_tts1 = new anywheresoftware.b4a.obejcts.TTS();
 //BA.debugLineNum = 25;BA.debugLine="Dim ProgPath = File.DirRootExternal & \"/TapeCalc\"";
_progpath = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/TapeCalc";
 //BA.debugLineNum = 26;BA.debugLine="Dim ScaleAuto As Double";
_scaleauto = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim Texts(8) As String";
_texts = new String[(int) (8)];
java.util.Arrays.fill(_texts,"");
 //BA.debugLineNum = 28;BA.debugLine="Dim LanguageID As String";
_languageid = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim sVal = \"\" As String";
_sval = "";
 //BA.debugLineNum = 31;BA.debugLine="Dim Val = 0 As Double";
_val = 0;
 //BA.debugLineNum = 32;BA.debugLine="Dim Op0 = \"\" As String";
_op0 = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim Result1 = 0 As Double";
_result1 = 0;
 //BA.debugLineNum = 34;BA.debugLine="Dim Txt = \"\" As String";
_txt = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim New1 = 0 As Int";
_new1 = (int) (0);
 //BA.debugLineNum = 37;BA.debugLine="Dim orig_unit As String";
_orig_unit = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim orig_quantity As String";
_orig_quantity = "";
 //BA.debugLineNum = 39;BA.debugLine="Dim edit_count As String";
_edit_count = "";
 //BA.debugLineNum = 41;BA.debugLine="Dim caseper As String";
_caseper = "";
 //BA.debugLineNum = 42;BA.debugLine="Dim pcsper As String";
_pcsper = "";
 //BA.debugLineNum = 43;BA.debugLine="Dim dozper As String";
_dozper = "";
 //BA.debugLineNum = 44;BA.debugLine="Dim boxper As String";
_boxper = "";
 //BA.debugLineNum = 45;BA.debugLine="Dim bagper As String";
_bagper = "";
 //BA.debugLineNum = 46;BA.debugLine="Dim packper As String";
_packper = "";
 //BA.debugLineNum = 47;BA.debugLine="Dim total_pieces As String";
_total_pieces = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim error_trigger As String";
_error_trigger = "";
 //BA.debugLineNum = 50;BA.debugLine="Private cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 608;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 609;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 610;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 611;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 612;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 613;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 614;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 615;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 616;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 617;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 618;BA.debugLine="End Sub";
return "";
}
public static void  _table_inventory_cellclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_INVENTORY_CellClicked rsub = new ResumableSub_TABLE_INVENTORY_CellClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_INVENTORY_CellClicked extends BA.ResumableSub {
public ResumableSub_TABLE_INVENTORY_CellClicked(wingan.app.inventory_table parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.inventory_table parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _num = "";
String _num1 = "";
String _query = "";
int _i = 0;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
int step10;
int limit10;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 258;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 259;BA.debugLine="Log(ColumnId & RowId)";
anywheresoftware.b4a.keywords.Common.LogImpl("761800450",_columnid+BA.NumberToString(_rowid),0);
 //BA.debugLineNum = 261;BA.debugLine="Dim RowData As Map = TABLE_INVENTORY.GetRow(RowId";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_inventory._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 262;BA.debugLine="Dim num As String = RowData.Get(\"#\")";
_num = BA.ObjectToString(_rowdata.Get((Object)("#")));
 //BA.debugLineNum = 263;BA.debugLine="Dim num1 As String = num.SubString2(0,num.IndexOf";
_num1 = _num.substring((int) (0),_num.indexOf("."));
 //BA.debugLineNum = 264;BA.debugLine="Log(num.SubString2(0,num.IndexOf(\".\")))";
anywheresoftware.b4a.keywords.Common.LogImpl("761800455",_num.substring((int) (0),_num.indexOf(".")),0);
 //BA.debugLineNum = 266;BA.debugLine="Dim query As String = \"SELECT * FROM inventory_di";
_query = "SELECT * FROM inventory_disc_table WHERE transaction_number = ? and transaction_id = ?";
 //BA.debugLineNum = 267;BA.debugLine="cursor1 = connection.ExecQuery2(query,Array As St";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery2(_query,new String[]{_num1,parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ })));
 //BA.debugLineNum = 268;BA.debugLine="If cursor1.RowCount = 1 Then";
if (true) break;

case 1:
//if
this.state = 20;
if (parent._cursor1.getRowCount()==1) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 269;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 13;
step10 = 1;
limit10 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 21;
if (true) break;

case 21:
//C
this.state = 13;
if ((step10 > 0 && _i <= limit10) || (step10 < 0 && _i >= limit10)) this.state = 6;
if (true) break;

case 22:
//C
this.state = 21;
_i = ((int)(0 + _i + step10)) ;
if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 270;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 7;
;
 //BA.debugLineNum = 271;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 272;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor1.GetString(\"";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("principal_name")));
 //BA.debugLineNum = 273;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor1.GetString(\"pr";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("product_variant")));
 //BA.debugLineNum = 274;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor1.GetString";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("product_description")));
 //BA.debugLineNum = 275;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 276;BA.debugLine="CMB_UNIT.cmbBox.Add(cursor1.GetString(\"unit\"))";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor1.GetString("unit"));
 //BA.debugLineNum = 277;BA.debugLine="EDITTEXT_QUANTITY.Text = cursor1.GetString(\"qua";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("quantity")));
 //BA.debugLineNum = 278;BA.debugLine="LABEL_LOAD_NUMBER.Text = cursor1.GetString(\"tra";
parent.mostCurrent._label_load_number.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("transaction_number")));
 //BA.debugLineNum = 279;BA.debugLine="edit_count = cursor1.GetString(\"edit_count\")";
parent._edit_count = parent._cursor1.GetString("edit_count");
 //BA.debugLineNum = 281;BA.debugLine="If cursor1.GetString(\"inventory_status\") = \"COU";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent._cursor1.GetString("inventory_status")).equals("COUNT")) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 282;BA.debugLine="LABEL_LOAD_STATUS.Text = \" COUNTED\"";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence(" COUNTED"));
 //BA.debugLineNum = 283;BA.debugLine="LABEL_LOAD_STATUS.Color = Colors.Red";
parent.mostCurrent._label_load_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 285;BA.debugLine="LABEL_LOAD_STATUS.Text = \" RECOUNT\"";
parent.mostCurrent._label_load_status.setText(BA.ObjectToCharSequence(" RECOUNT"));
 //BA.debugLineNum = 286;BA.debugLine="LABEL_LOAD_STATUS.Color = Colors.Green";
parent.mostCurrent._label_load_status.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 if (true) break;

case 12:
//C
this.state = 22;
;
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 289;BA.debugLine="CMB_UNIT.cmbBox.Enabled = False";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 290;BA.debugLine="EDITTEXT_QUANTITY.Enabled = False";
parent.mostCurrent._edittext_quantity.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 291;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 292;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 293;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 14;
;
 //BA.debugLineNum = 294;BA.debugLine="CMB_UNIT.cmbBox.DropdownTextColor = Colors.Black";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 295;BA.debugLine="CMB_UNIT.cmbBox.TextColor = Colors.Black";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 296;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 297;BA.debugLine="PANEL_BG_RECOUNT.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_recount.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 298;BA.debugLine="orig_unit = CMB_UNIT.cmbBox.SelectedItem";
parent._orig_unit = parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 299;BA.debugLine="orig_quantity  = EDITTEXT_QUANTITY.Text";
parent._orig_quantity = parent.mostCurrent._edittext_quantity.getText();
 //BA.debugLineNum = 302;BA.debugLine="If MONTLHY_INVENTORY2_MODULE.inventory_type = \"C";
if (true) break;

case 14:
//if
this.state = 19;
if ((parent.mostCurrent._montlhy_inventory2_module._inventory_type /*String*/ ).equals("COUNT") && (parent.mostCurrent._label_load_status.getText()).equals(" RECOUNT")) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 19;
 //BA.debugLineNum = 303;BA.debugLine="BUTTON_EDIT.Visible = False";
parent.mostCurrent._button_edit.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 304;BA.debugLine="BUTTON_DELETE.Visible = False";
parent.mostCurrent._button_delete.setVisible(anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 306;BA.debugLine="BUTTON_EDIT.Visible = True";
parent.mostCurrent._button_edit.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 307;BA.debugLine="BUTTON_DELETE.Visible = True";
parent.mostCurrent._button_delete.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 308;BA.debugLine="BUTTON_EDIT.Text = \" Edit\"";
parent.mostCurrent._button_edit.setText(BA.ObjectToCharSequence(" Edit"));
 //BA.debugLineNum = 309;BA.debugLine="BUTTON_EDIT.TextColor = Colors.Blue";
parent.mostCurrent._button_edit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 310;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 19;
;
 //BA.debugLineNum = 311;BA.debugLine="BUTTON_DELETE.Text = \" Delete\"";
parent.mostCurrent._button_delete.setText(BA.ObjectToCharSequence(" Delete"));
 //BA.debugLineNum = 312;BA.debugLine="BUTTON_DELETE.TextColor = Colors.Red";
parent.mostCurrent._button_delete.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 if (true) break;

case 19:
//C
this.state = 20;
;
 if (true) break;

case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 315;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_inventory_celllongclicked(String _columnid,long _rowid) throws Exception{
 //BA.debugLineNum = 316;BA.debugLine="Sub TABLE_INVENTORY_CellLongClicked (ColumnId As S";
 //BA.debugLineNum = 318;BA.debugLine="End Sub";
return "";
}
public static String  _table_inventory_dataupdated() throws Exception{
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
 //BA.debugLineNum = 220;BA.debugLine="Sub TABLE_INVENTORY_DataUpdated";
 //BA.debugLineNum = 221;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 222;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)]),(Object)(mostCurrent._namecolumn[(int) (3)]),(Object)(mostCurrent._namecolumn[(int) (4)]),(Object)(mostCurrent._namecolumn[(int) (5)]),(Object)(mostCurrent._namecolumn[(int) (6)]),(Object)(mostCurrent._namecolumn[(int) (7)]),(Object)(mostCurrent._namecolumn[(int) (8)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 223;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 224;BA.debugLine="For i = 0 To TABLE_INVENTORY.VisibleRowIds.Size";
{
final int step4 = 1;
final int limit4 = mostCurrent._table_inventory._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 225;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 226;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 227;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 229;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 230;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 231;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 234;BA.debugLine="For i = 0 To TABLE_INVENTORY.VisibleRowIds.Size -";
{
final int step14 = 1;
final int limit14 = (int) (mostCurrent._table_inventory._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit14 ;_i = _i + step14 ) {
 //BA.debugLineNum = 235;BA.debugLine="Dim RowId As Long = TABLE_INVENTORY.VisibleRowId";
_rowid = BA.ObjectToLongNumber(mostCurrent._table_inventory._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 236;BA.debugLine="If RowId > 0 Then";
if (_rowid>0) { 
 //BA.debugLineNum = 237;BA.debugLine="Dim pnl1 As B4XView = NameColumn(8).CellsLayout";
_pnl1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._namecolumn[(int) (8)].CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 238;BA.debugLine="Dim row As Map = TABLE_INVENTORY.GetRow(RowId)";
_row = new anywheresoftware.b4a.objects.collections.Map();
_row = mostCurrent._table_inventory._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 239;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 240;BA.debugLine="Dim OtherColumnValue As String = row.Get(NameCo";
_othercolumnvalue = BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (8)].Id /*String*/ )));
 //BA.debugLineNum = 241;BA.debugLine="Log(row.Get(NameColumn(8).Id))";
anywheresoftware.b4a.keywords.Common.LogImpl("761734933",BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (8)].Id /*String*/ ))),0);
 //BA.debugLineNum = 242;BA.debugLine="If OtherColumnValue = (\"COUNT\") Then";
if ((_othercolumnvalue).equals(("COUNT"))) { 
 //BA.debugLineNum = 243;BA.debugLine="clr = xui.Color_Red";
_clr = mostCurrent._xui.Color_Red;
 }else if((_othercolumnvalue).equals(("RECOUNT"))) { 
 //BA.debugLineNum = 245;BA.debugLine="clr = xui.Color_Green";
_clr = mostCurrent._xui.Color_Green;
 }else {
 //BA.debugLineNum = 247;BA.debugLine="clr = xui.Color_Transparent";
_clr = mostCurrent._xui.Color_Transparent;
 };
 //BA.debugLineNum = 249;BA.debugLine="pnl1.GetView(0).Color = clr";
_pnl1.GetView((int) (0)).setColor(_clr);
 };
 }
};
 //BA.debugLineNum = 252;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 253;BA.debugLine="TABLE_INVENTORY.Refresh";
mostCurrent._table_inventory._refresh /*String*/ ();
 //BA.debugLineNum = 254;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 };
 //BA.debugLineNum = 256;BA.debugLine="End Sub";
return "";
}
public static String  _tts1_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 528;BA.debugLine="Sub TTS1_Ready (Success As Boolean)";
 //BA.debugLineNum = 529;BA.debugLine="If Success Then";
if (_success) { 
 }else {
 };
 //BA.debugLineNum = 535;BA.debugLine="End Sub";
return "";
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 630;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 631;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 632;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 633;BA.debugLine="End Sub";
return "";
}
public static void  _updatetape() throws Exception{
ResumableSub_UpdateTape rsub = new ResumableSub_UpdateTape(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UpdateTape extends BA.ResumableSub {
public ResumableSub_UpdateTape(wingan.app.inventory_table parent) {
this.parent = parent;
}
wingan.app.inventory_table parent;
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
 //BA.debugLineNum = 1141;BA.debugLine="Dim hr As Float";
_hr = 0f;
 //BA.debugLineNum = 1143;BA.debugLine="lblPaperRoll.Text = Txt";
parent.mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(parent._txt));
 //BA.debugLineNum = 1145;BA.debugLine="hr = stu.MeasureMultilineTextHeight(lblPaperRoll,";
_hr = (float) (parent.mostCurrent._stu.MeasureMultilineTextHeight((android.widget.TextView)(parent.mostCurrent._lblpaperroll.getObject()),BA.ObjectToCharSequence(parent._txt)));
 //BA.debugLineNum = 1146;BA.debugLine="If hr > scvPaperRoll.Height Then";
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
 //BA.debugLineNum = 1147;BA.debugLine="lblPaperRoll.Height = hr";
parent.mostCurrent._lblpaperroll.setHeight((int) (_hr));
 //BA.debugLineNum = 1148;BA.debugLine="scvPaperRoll.Panel.Height = hr";
parent.mostCurrent._scvpaperroll.getPanel().setHeight((int) (_hr));
 //BA.debugLineNum = 1149;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 4;
;
 //BA.debugLineNum = 1150;BA.debugLine="scvPaperRoll.ScrollPosition = hr";
parent.mostCurrent._scvpaperroll.setScrollPosition((int) (_hr));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1152;BA.debugLine="End Sub";
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
