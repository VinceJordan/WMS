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

public class montlhy_inventory2_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static montlhy_inventory2_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.montlhy_inventory2_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (montlhy_inventory2_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.montlhy_inventory2_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.montlhy_inventory2_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (montlhy_inventory2_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (montlhy_inventory2_module) Resume **");
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
		return montlhy_inventory2_module.class;
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
            BA.LogInfo("** Activity (montlhy_inventory2_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (montlhy_inventory2_module) Pause event (activity is not paused). **");
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
            montlhy_inventory2_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (montlhy_inventory2_module) Resume **");
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
public static anywheresoftware.b4a.obejcts.TTS _tts1 = null;
public static String _principal_id = "";
public static String _product_id = "";
public static String _assign1 = "";
public static String _assign2 = "";
public static String _inventory_type = "";
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
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public static anywheresoftware.b4a.objects.Timer _ts = null;
public static int _cmb_trigger = 0;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _tablebitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _calcbitmap = null;
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
public anywheresoftware.b4a.objects.IME _ctrl = null;
public static String _scannermacaddress = "";
public static boolean _scanneronceconnected = false;
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
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xsearchtemplate _searchtemplate = null;
public wingan.app.b4xsearchtemplate _searchtemplate2 = null;
public wingan.app.b4xsearchtemplate _searchtemplate3 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _sv_monthly = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public wingan.app.b4xcombobox _cmb_invtype = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_person1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_person2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_quantity = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_description = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_principal = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_barcode = null;
public wingan.app.b4xcombobox _cmb_position = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_input = null;
public wingan.app.b4xswitch _switch_continues = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_continues = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_answer = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_calcu = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_add = null;
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
public ResumableSub_Activity_Create(wingan.app.montlhy_inventory2_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.montlhy_inventory2_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
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
 //BA.debugLineNum = 129;BA.debugLine="Activity.LoadLayout(\"monthly2\")";
parent.mostCurrent._activity.LoadLayout("monthly2",mostCurrent.activityBA);
 //BA.debugLineNum = 131;BA.debugLine="tableBitmap = LoadBitmap(File.DirAssets, \"table.p";
parent._tablebitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"table.png");
 //BA.debugLineNum = 132;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 133;BA.debugLine="calcBitmap = LoadBitmap(File.DirAssets, \"calcu.pn";
parent._calcbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"calcu.png");
 //BA.debugLineNum = 135;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 136;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 137;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 138;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 139;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 140;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 141;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 143;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 144;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 145;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 147;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 148;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 150;BA.debugLine="SV_MONTHLY.Panel.LoadLayout(\"monthly2_scrollview\"";
parent.mostCurrent._sv_monthly.getPanel().LoadLayout("monthly2_scrollview",mostCurrent.activityBA);
 //BA.debugLineNum = 151;BA.debugLine="SV_MONTHLY.Panel.Height = PANEL_INPUT.Top + PANEL";
parent.mostCurrent._sv_monthly.getPanel().setHeight((int) (parent.mostCurrent._panel_input.getTop()+parent.mostCurrent._panel_input.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 //BA.debugLineNum = 154;BA.debugLine="serial1.Initialize(\"Serial\")";
parent._serial1.Initialize("Serial");
 //BA.debugLineNum = 155;BA.debugLine="Ts.Initialize(\"Timer\", 2000)";
parent._ts.Initialize(processBA,"Timer",(long) (2000));
 //BA.debugLineNum = 161;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 162;BA.debugLine="Dialog.Initialize(Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 163;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 164;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 165;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 166;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 167;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 168;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 169;BA.debugLine="Dialog.ButtonsTextColor = Colors.Black";
parent.mostCurrent._dialog._buttonstextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 171;BA.debugLine="SearchTemplate.Initialize";
parent.mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 172;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 173;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 174;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 175;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
parent.mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 176;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
parent.mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 178;BA.debugLine="SearchTemplate2.Initialize";
parent.mostCurrent._searchtemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 179;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextBackgr";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 180;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextColor";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 181;BA.debugLine="SearchTemplate2.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 182;BA.debugLine="SearchTemplate2.ItemHightlightColor = Colors.Whit";
parent.mostCurrent._searchtemplate2._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 183;BA.debugLine="SearchTemplate2.TextHighlightColor = Colors.RGB(8";
parent.mostCurrent._searchtemplate2._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 185;BA.debugLine="SearchTemplate3.Initialize";
parent.mostCurrent._searchtemplate3._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 186;BA.debugLine="SearchTemplate3.CustomListView1.DefaultTextBackgr";
parent.mostCurrent._searchtemplate3._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 187;BA.debugLine="SearchTemplate3.CustomListView1.DefaultTextColor";
parent.mostCurrent._searchtemplate3._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 188;BA.debugLine="SearchTemplate3.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate3._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 189;BA.debugLine="SearchTemplate3.ItemHightlightColor = Colors.Whit";
parent.mostCurrent._searchtemplate3._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 190;BA.debugLine="SearchTemplate3.TextHighlightColor = Colors.RGB(8";
parent.mostCurrent._searchtemplate3._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 193;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 194;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 196;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 197;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 198;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 200;BA.debugLine="CONTINUES";
_continues();
 //BA.debugLineNum = 202;BA.debugLine="POPULATE_INVTYPE";
_populate_invtype();
 //BA.debugLineNum = 203;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 204;BA.debugLine="POPULATE_INVPOSITION";
_populate_invposition();
 //BA.debugLineNum = 206;BA.debugLine="PRINCIPAL_SELECT";
_principal_select();
 //BA.debugLineNum = 208;BA.debugLine="COUNT_TRANSACTION";
_count_transaction();
 //BA.debugLineNum = 209;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 210;BA.debugLine="INPUT_MANUAL";
_input_manual();
 //BA.debugLineNum = 212;BA.debugLine="INV_COLOR";
_inv_color();
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
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
public ResumableSub_Activity_CreateMenu(wingan.app.montlhy_inventory2_module parent,de.amberhome.objects.appcompat.ACMenuWrapper _menu) {
this.parent = parent;
this._menu = _menu;
}
wingan.app.montlhy_inventory2_module parent;
de.amberhome.objects.appcompat.ACMenuWrapper _menu;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item1 = null;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 215;BA.debugLine="Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item1 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item1 = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("calcu"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 216;BA.debugLine="item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS";
_item1.setShowAsAction(_item1.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 217;BA.debugLine="UpdateIcon(\"calcu\", calcBitmap)";
_updateicon("calcu",parent._calcbitmap);
 //BA.debugLineNum = 218;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (2),(int) (2),BA.ObjectToCharSequence("table"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 219;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 220;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 221;BA.debugLine="Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item1 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item1 = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 222;BA.debugLine="item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS";
_item1.setShowAsAction(_item1.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 223;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 224;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 1361;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 1362;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 1363;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 1365;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 244;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 245;BA.debugLine="Log (\"Activity paused. Disconnecting...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("764749569","Activity paused. Disconnecting...",0);
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 228;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("764684033","Resuming...",0);
 //BA.debugLineNum = 229;BA.debugLine="If TTS1.IsInitialized = False Then";
if (true) break;

case 1:
//if
this.state = 4;
if (parent._tts1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 230;BA.debugLine="TTS1.Initialize(\"TTS1\")";
parent._tts1.Initialize(processBA,"TTS1");
 if (true) break;

case 4:
//C
this.state = 5;
;
 //BA.debugLineNum = 233;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 5;
;
 //BA.debugLineNum = 234;BA.debugLine="If cmb_trigger = 1 Then";
if (true) break;

case 5:
//if
this.state = 14;
if (parent._cmb_trigger==1) { 
this.state = 7;
}else {
this.state = 13;
}if (true) break;

case 7:
//C
this.state = 8;
 //BA.debugLineNum = 235;BA.debugLine="OpenSpinner(CMB_INVTYPE.cmbBox)";
_openspinner(parent.mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 236;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 237;BA.debugLine="If ScannerOnceConnected=True Then";
if (true) break;

case 8:
//if
this.state = 11;
if (parent._scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 10;
}if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 238;BA.debugLine="Ts.Enabled=True";
parent._ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 11:
//C
this.state = 14;
;
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 241;BA.debugLine="COUNT_TRANSACTION";
_count_transaction();
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
 //BA.debugLineNum = 997;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 998;BA.debugLine="If Item.Title = \"table\" Then";
if ((_item.getTitle()).equals("table")) { 
 //BA.debugLineNum = 999;BA.debugLine="If CMB_INVTYPE.cmbBox.SelectedIndex = -1 Then CM";
if (mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));};
 //BA.debugLineNum = 1001;BA.debugLine="If LABEL_LOAD_PERSON1.Text = \"Assign Person\" And";
if ((mostCurrent._label_load_person1.getText()).equals("Assign Person") && (mostCurrent._label_load_person2.getText()).equals("Assign Person") && mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("RECOUNT")) { 
 //BA.debugLineNum = 1002;BA.debugLine="Msgbox2Async(\"Please assign atleast one person.";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please assign atleast one person."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1003;BA.debugLine="SV_MONTHLY.ScrollToNow(9%x)";
mostCurrent._sv_monthly.ScrollToNow(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (9),mostCurrent.activityBA));
 }else {
 //BA.debugLineNum = 1005;BA.debugLine="CLEAR_INPUT";
_clear_input();
 //BA.debugLineNum = 1007;BA.debugLine="assign1 = LABEL_LOAD_PERSON1.Text";
_assign1 = mostCurrent._label_load_person1.getText();
 //BA.debugLineNum = 1008;BA.debugLine="assign2 = LABEL_LOAD_PERSON2.Text";
_assign2 = mostCurrent._label_load_person2.getText();
 //BA.debugLineNum = 1009;BA.debugLine="inventory_type = CMB_INVTYPE.cmbBox.SelectedIte";
_inventory_type = mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 1012;BA.debugLine="StartActivity(INVENTORY_TABLE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._inventory_table.getObject()));
 };
 }else if((_item.getTitle()).equals("cart")) { 
 //BA.debugLineNum = 1016;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("767174419","Resuming...",0);
 //BA.debugLineNum = 1017;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 1018;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 1019;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 1020;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1021;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1022;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 1023;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 1025;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 1026;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 }else if((_item.getTitle()).equals("calcu")) { 
 //BA.debugLineNum = 1029;BA.debugLine="oncal";
_oncal();
 //BA.debugLineNum = 1030;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,True)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1031;BA.debugLine="PANEL_BG_CALCU.BringToFront";
mostCurrent._panel_bg_calcu.BringToFront();
 //BA.debugLineNum = 1032;BA.debugLine="LABEL_LOAD_ANSWER.Text = \"0\"";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence("0"));
 };
 //BA.debugLineNum = 1034;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 965;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 966;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 967;BA.debugLine="StartActivity(MONTHLY_INVENTORY_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._monthly_inventory_module.getObject()));
 //BA.debugLineNum = 968;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 969;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _addbadgetoicon(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp,int _number1) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvs1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _mbmp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _target = null;
 //BA.debugLineNum = 983;BA.debugLine="Sub AddBadgeToIcon(bmp As Bitmap, Number1 As Int)";
 //BA.debugLineNum = 984;BA.debugLine="Dim cvs1 As Canvas";
_cvs1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 985;BA.debugLine="Dim mbmp As Bitmap";
_mbmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 986;BA.debugLine="mbmp.InitializeMutable(32dip, 32dip)";
_mbmp.InitializeMutable(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 987;BA.debugLine="cvs1.Initialize2(mbmp)";
_cvs1.Initialize2((android.graphics.Bitmap)(_mbmp.getObject()));
 //BA.debugLineNum = 988;BA.debugLine="Dim target As Rect";
_target = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 989;BA.debugLine="target.Initialize(0, 0, mbmp.Width, mbmp.Height)";
_target.Initialize((int) (0),(int) (0),_mbmp.getWidth(),_mbmp.getHeight());
 //BA.debugLineNum = 990;BA.debugLine="cvs1.DrawBitmap(bmp, Null, target)";
_cvs1.DrawBitmap((android.graphics.Bitmap)(_bmp.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_target.getObject()));
 //BA.debugLineNum = 991;BA.debugLine="If Number1 > 0 Then";
if (_number1>0) { 
 //BA.debugLineNum = 992;BA.debugLine="cvs1.DrawCircle(mbmp.Width - 8dip, 8dip, 8dip, C";
_cvs1.DrawCircle((float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 993;BA.debugLine="cvs1.DrawText(Min(Number1, 1000), mbmp.Width - 8";
_cvs1.DrawText(mostCurrent.activityBA,BA.NumberToString(anywheresoftware.b4a.keywords.Common.Min(_number1,1000)),(float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (11))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (9),anywheresoftware.b4a.keywords.Common.Colors.White,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 };
 //BA.debugLineNum = 995;BA.debugLine="Return mbmp";
if (true) return _mbmp;
 //BA.debugLineNum = 996;BA.debugLine="End Sub";
return null;
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 584;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 585;BA.debugLine="Log(\"Connection broken...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("765798145","Connection broken...",0);
 //BA.debugLineNum = 586;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 587;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 588;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 589;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 590;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 591;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 592;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 593;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 595;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 };
 //BA.debugLineNum = 597;BA.debugLine="End Sub";
return "";
}
public static void  _astream_newdata(byte[] _buffer) throws Exception{
ResumableSub_AStream_NewData rsub = new ResumableSub_AStream_NewData(null,_buffer);
rsub.resume(processBA, null);
}
public static class ResumableSub_AStream_NewData extends BA.ResumableSub {
public ResumableSub_AStream_NewData(wingan.app.montlhy_inventory2_module parent,byte[] _buffer) {
this.parent = parent;
this._buffer = _buffer;
}
wingan.app.montlhy_inventory2_module parent;
byte[] _buffer;
String _add_query = "";
int _trigger = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _row = 0;
int _result = 0;
int _row1 = 0;
int _qrow = 0;
int step14;
int limit14;
int step28;
int limit28;
int step36;
int limit36;
int step40;
int limit40;
int step55;
int limit55;
int step104;
int limit104;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 459;BA.debugLine="Log(\"Received: \" & BytesToString(Buffer, 0, Buffe";
anywheresoftware.b4a.keywords.Common.LogImpl("765732609","Received: "+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"),0);
 //BA.debugLineNum = 460;BA.debugLine="LABEL_LOAD_BARCODE.Text = BytesToString(Buffer, 0";
parent.mostCurrent._label_load_barcode.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")));
 //BA.debugLineNum = 462;BA.debugLine="Dim add_query As String";
_add_query = "";
 //BA.debugLineNum = 463;BA.debugLine="If MONTHLY_INVENTORY_MODULE.principal_id = \"ALL\"";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._monthly_inventory_module._principal_id /*String*/ ).equals("ALL")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 464;BA.debugLine="add_query = \"\"";
_add_query = "";
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 466;BA.debugLine="add_query = \" AND principal_id = '\"&MONTHLY_INVE";
_add_query = " AND principal_id = '"+parent.mostCurrent._monthly_inventory_module._principal_id /*String*/ +"'";
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 468;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 469;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0'"+_add_query+" ORDER BY product_id")));
 //BA.debugLineNum = 470;BA.debugLine="If cursor2.RowCount >= 2 Then";
if (true) break;

case 7:
//if
this.state = 46;
if (parent._cursor2.getRowCount()>=2) { 
this.state = 9;
}else if(parent._cursor2.getRowCount()==1) { 
this.state = 21;
}else {
this.state = 27;
}if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 471;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 472;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 473;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 10:
//for
this.state = 13;
step14 = 1;
limit14 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 97;
if (true) break;

case 97:
//C
this.state = 13;
if ((step14 > 0 && _row <= limit14) || (step14 < 0 && _row >= limit14)) this.state = 12;
if (true) break;

case 98:
//C
this.state = 97;
_row = ((int)(0 + _row + step14)) ;
if (true) break;

case 12:
//C
this.state = 98;
 //BA.debugLineNum = 474;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 475;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 476;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 13:
//C
this.state = 14;
;
 //BA.debugLineNum = 478;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) '";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 479;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 99;
return;
case 99:
//C
this.state = 14;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 480;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
if (true) break;

case 14:
//if
this.state = 19;
if (_result!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 19;
 //BA.debugLineNum = 481;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 482;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 484;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 if (true) break;

case 19:
//C
this.state = 46;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 489;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 22:
//for
this.state = 25;
step28 = 1;
limit28 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 100;
if (true) break;

case 100:
//C
this.state = 25;
if ((step28 > 0 && _row <= limit28) || (step28 < 0 && _row >= limit28)) this.state = 24;
if (true) break;

case 101:
//C
this.state = 100;
_row = ((int)(0 + _row + step28)) ;
if (true) break;

case 24:
//C
this.state = 101;
 //BA.debugLineNum = 490;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 491;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor2.GetString";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 492;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;
if (true) break;

case 25:
//C
this.state = 46;
;
 if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 495;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 496;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 28:
//if
this.state = 45;
if (parent._cursor4.getRowCount()>0) { 
this.state = 30;
}else {
this.state = 44;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 497;BA.debugLine="For row = 0 To cursor4.RowCount - 1";
if (true) break;

case 31:
//for
this.state = 42;
step36 = 1;
limit36 = (int) (parent._cursor4.getRowCount()-1);
_row = (int) (0) ;
this.state = 102;
if (true) break;

case 102:
//C
this.state = 42;
if ((step36 > 0 && _row <= limit36) || (step36 < 0 && _row >= limit36)) this.state = 33;
if (true) break;

case 103:
//C
this.state = 102;
_row = ((int)(0 + _row + step36)) ;
if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 498;BA.debugLine="cursor4.Position = row";
parent._cursor4.setPosition(_row);
 //BA.debugLineNum = 499;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT princip";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._cursor4.GetString("principal_id")+"'")));
 //BA.debugLineNum = 500;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 34:
//if
this.state = 41;
if (parent._cursor5.getRowCount()>0) { 
this.state = 36;
}if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 501;BA.debugLine="For row1 = 0 To cursor5.RowCount - 1";
if (true) break;

case 37:
//for
this.state = 40;
step40 = 1;
limit40 = (int) (parent._cursor5.getRowCount()-1);
_row1 = (int) (0) ;
this.state = 104;
if (true) break;

case 104:
//C
this.state = 40;
if ((step40 > 0 && _row1 <= limit40) || (step40 < 0 && _row1 >= limit40)) this.state = 39;
if (true) break;

case 105:
//C
this.state = 104;
_row1 = ((int)(0 + _row1 + step40)) ;
if (true) break;

case 39:
//C
this.state = 105;
 //BA.debugLineNum = 502;BA.debugLine="cursor5.Position = row1";
parent._cursor5.setPosition(_row1);
 if (true) break;
if (true) break;

case 40:
//C
this.state = 41;
;
 //BA.debugLineNum = 504;BA.debugLine="Msgbox2Async(\"The product you scanned :\"& CRL";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you scanned :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent._cursor4.GetString("product_desc")+" "+anywheresoftware.b4a.keywords.Common.CRLF+"belongs to principal :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent._cursor5.GetString("principal_name")+""+anywheresoftware.b4a.keywords.Common.CRLF+"which your selected principal is :"+anywheresoftware.b4a.keywords.Common.CRLF+""+parent.mostCurrent._monthly_inventory_module._principal_name /*String*/ +""),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 41:
//C
this.state = 103;
;
 if (true) break;
if (true) break;

case 42:
//C
this.state = 45;
;
 if (true) break;

case 44:
//C
this.state = 45;
 //BA.debugLineNum = 508;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not RE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 45:
//C
this.state = 46;
;
 //BA.debugLineNum = 510;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 //BA.debugLineNum = 511;BA.debugLine="CLEAR_INPUT";
_clear_input();
 if (true) break;
;
 //BA.debugLineNum = 513;BA.debugLine="If trigger = 0 Then";

case 46:
//if
this.state = 96;
if (_trigger==0) { 
this.state = 48;
}if (true) break;

case 48:
//C
this.state = 49;
 //BA.debugLineNum = 514;BA.debugLine="scan_code = BytesToString(Buffer, 0, Buffer.Leng";
parent._scan_code = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 //BA.debugLineNum = 515;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 516;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 49:
//for
this.state = 87;
step55 = 1;
limit55 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 106;
if (true) break;

case 106:
//C
this.state = 87;
if ((step55 > 0 && _qrow <= limit55) || (step55 < 0 && _qrow >= limit55)) this.state = 51;
if (true) break;

case 107:
//C
this.state = 106;
_qrow = ((int)(0 + _qrow + step55)) ;
if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 517;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 518;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"pr";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 519;BA.debugLine="principal_id = cursor3.GetString(\"principal_id\"";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 520;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 522;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 523;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0 T";
if (true) break;

case 52:
//if
this.state = 55;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 54;
}if (true) break;

case 54:
//C
this.state = 55;
 //BA.debugLineNum = 524;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 526;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 Th";

case 55:
//if
this.state = 58;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 57;
}if (true) break;

case 57:
//C
this.state = 58;
 //BA.debugLineNum = 527;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 529;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 Th";

case 58:
//if
this.state = 61;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 60;
}if (true) break;

case 60:
//C
this.state = 61;
 //BA.debugLineNum = 530;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 532;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 Th";

case 61:
//if
this.state = 64;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 63;
}if (true) break;

case 63:
//C
this.state = 64;
 //BA.debugLineNum = 533;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 535;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 Th";

case 64:
//if
this.state = 67;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 66;
}if (true) break;

case 66:
//C
this.state = 67;
 //BA.debugLineNum = 536;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 538;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0 T";

case 67:
//if
this.state = 70;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 69;
}if (true) break;

case 69:
//C
this.state = 70;
 //BA.debugLineNum = 539;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 70:
//C
this.state = 71;
;
 //BA.debugLineNum = 541;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 108;
return;
case 108:
//C
this.state = 71;
;
 //BA.debugLineNum = 542;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"case_bar";
if (true) break;

case 71:
//if
this.state = 74;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("case_bar_code"))) { 
this.state = 73;
}if (true) break;

case 73:
//C
this.state = 74;
 //BA.debugLineNum = 543;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE"));
 if (true) break;
;
 //BA.debugLineNum = 545;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"bar_code";

case 74:
//if
this.state = 77;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("bar_code"))) { 
this.state = 76;
}if (true) break;

case 76:
//C
this.state = 77;
 //BA.debugLineNum = 546;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS"));
 if (true) break;
;
 //BA.debugLineNum = 548;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"box_bar_";

case 77:
//if
this.state = 80;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("box_bar_code"))) { 
this.state = 79;
}if (true) break;

case 79:
//C
this.state = 80;
 //BA.debugLineNum = 549;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX"));
 if (true) break;
;
 //BA.debugLineNum = 551;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"pack_bar";

case 80:
//if
this.state = 83;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("pack_bar_code"))) { 
this.state = 82;
}if (true) break;

case 82:
//C
this.state = 83;
 //BA.debugLineNum = 552;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK"));
 if (true) break;
;
 //BA.debugLineNum = 554;BA.debugLine="If scan_code.Trim = cursor3.GetString(\"bag_bar_";

case 83:
//if
this.state = 86;
if ((parent._scan_code.trim()).equals(parent._cursor3.GetString("bag_bar_code"))) { 
this.state = 85;
}if (true) break;

case 85:
//C
this.state = 86;
 //BA.debugLineNum = 555;BA.debugLine="CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.Index";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG"));
 if (true) break;

case 86:
//C
this.state = 107;
;
 //BA.debugLineNum = 559;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS\"";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 560;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 561;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 562;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 563;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 564;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS\"";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 if (true) break;
if (true) break;

case 87:
//C
this.state = 88;
;
 //BA.debugLineNum = 567;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principal";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 568;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 88:
//if
this.state = 95;
if (parent._cursor6.getRowCount()>0) { 
this.state = 90;
}if (true) break;

case 90:
//C
this.state = 91;
 //BA.debugLineNum = 569;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 91:
//for
this.state = 94;
step104 = 1;
limit104 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 109;
if (true) break;

case 109:
//C
this.state = 94;
if ((step104 > 0 && _row <= limit104) || (step104 < 0 && _row >= limit104)) this.state = 93;
if (true) break;

case 110:
//C
this.state = 109;
_row = ((int)(0 + _row + step104)) ;
if (true) break;

case 93:
//C
this.state = 110;
 //BA.debugLineNum = 570;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 571;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring(";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 94:
//C
this.state = 95;
;
 //BA.debugLineNum = 573;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 574;BA.debugLine="reason = \"N/A\"";
parent._reason = "N/A";
 //BA.debugLineNum = 575;BA.debugLine="input_type = \"BARCODE\"";
parent._input_type = "BARCODE";
 //BA.debugLineNum = 576;BA.debugLine="SV_MONTHLY.ScrollToNow(53%x)";
parent.mostCurrent._sv_monthly.ScrollToNow(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (53),mostCurrent.activityBA));
 //BA.debugLineNum = 577;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 578;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 111;
return;
case 111:
//C
this.state = 95;
;
 //BA.debugLineNum = 579;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 if (true) break;

case 95:
//C
this.state = 96;
;
 if (true) break;

case 96:
//C
this.state = -1;
;
 //BA.debugLineNum = 583;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _inputlist_result(int _result) throws Exception{
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 598;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 599;BA.debugLine="Log(\"Connection terminated...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("765863681","Connection terminated...",0);
 //BA.debugLineNum = 600;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 601;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 960;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 961;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 962;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 963;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 964;BA.debugLine="End Sub";
return null;
}
public static String  _btnback_click() throws Exception{
 //BA.debugLineNum = 1292;BA.debugLine="Sub btnBack_Click";
 //BA.debugLineNum = 1293;BA.debugLine="If sVal.Length > 0 Then";
if (_sval.length()>0) { 
 //BA.debugLineNum = 1294;BA.debugLine="Txt = sVal.SubString2(0, sVal.Length - 1)";
_txt = _sval.substring((int) (0),(int) (_sval.length()-1));
 //BA.debugLineNum = 1295;BA.debugLine="sVal = sVal.SubString2(0, sVal.Length - 1)";
_sval = _sval.substring((int) (0),(int) (_sval.length()-1));
 //BA.debugLineNum = 1296;BA.debugLine="UpdateTape";
_updatetape();
 };
 //BA.debugLineNum = 1298;BA.debugLine="End Sub";
return "";
}
public static String  _btncharsize_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 1313;BA.debugLine="Sub btnCharSize_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 1314;BA.debugLine="If Checked = False Then";
if (_checked==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1315;BA.debugLine="lblPaperRoll.TextSize = 16 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (16*_scaleauto));
 }else {
 //BA.debugLineNum = 1317;BA.debugLine="lblPaperRoll.TextSize = 22 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (22*_scaleauto));
 };
 //BA.debugLineNum = 1319;BA.debugLine="End Sub";
return "";
}
public static String  _btnclr_click() throws Exception{
 //BA.debugLineNum = 1273;BA.debugLine="Sub btnClr_Click";
 //BA.debugLineNum = 1280;BA.debugLine="Val = 0";
_val = 0;
 //BA.debugLineNum = 1281;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1282;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1283;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1284;BA.debugLine="Txt = \"\"";
_txt = "";
 //BA.debugLineNum = 1285;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1286;BA.debugLine="lblPaperRoll.Text = \"\"";
mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1287;BA.debugLine="lblPaperRoll.Height = scvPaperRoll.Height";
mostCurrent._lblpaperroll.setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1288;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1289;BA.debugLine="LABEL_LOAD_ANSWER.Text = \"0\"";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 1291;BA.debugLine="End Sub";
return "";
}
public static String  _btndigit_click() throws Exception{
String _bs = "";
anywheresoftware.b4a.objects.ConcreteViewWrapper _send = null;
 //BA.debugLineNum = 1080;BA.debugLine="Sub btnDigit_Click";
 //BA.debugLineNum = 1081;BA.debugLine="Dim bs As String, Send As View";
_bs = "";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 1083;BA.debugLine="If New1 = 0 Then";
if (_new1==0) { 
 //BA.debugLineNum = 1084;BA.debugLine="New1 = 1";
_new1 = (int) (1);
 };
 //BA.debugLineNum = 1087;BA.debugLine="Send = Sender";
_send = (anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 1088;BA.debugLine="bs = Send.Tag";
_bs = BA.ObjectToString(_send.getTag());
 //BA.debugLineNum = 1090;BA.debugLine="Select bs";
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
 //BA.debugLineNum = 1092;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"g","s","m","x")) {
case 0: 
case 1: 
case 2: 
case 3: {
 //BA.debugLineNum = 1094;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1097;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1098;BA.debugLine="Txt = Txt & CRLF & CRLF";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 1099;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1100;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1101;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1102;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 };
 //BA.debugLineNum = 1105;BA.debugLine="If bs = \"3.1415926535897932\" Then";
if ((_bs).equals("3.1415926535897932")) { 
 //BA.debugLineNum = 1106;BA.debugLine="If sVal <> \"\" Then";
if ((_sval).equals("") == false) { 
 //BA.debugLineNum = 1107;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1109;BA.debugLine="Txt = Txt & cPI";
_txt = _txt+BA.NumberToString(anywheresoftware.b4a.keywords.Common.cPI);
 //BA.debugLineNum = 1110;BA.debugLine="sVal = cPI";
_sval = BA.NumberToString(anywheresoftware.b4a.keywords.Common.cPI);
 }else if((_bs).equals(".")) { 
 //BA.debugLineNum = 1112;BA.debugLine="If sVal.IndexOf(\".\") < 0 Then";
if (_sval.indexOf(".")<0) { 
 //BA.debugLineNum = 1113;BA.debugLine="Txt = Txt & bs";
_txt = _txt+_bs;
 //BA.debugLineNum = 1114;BA.debugLine="sVal = sVal & bs";
_sval = _sval+_bs;
 };
 }else {
 //BA.debugLineNum = 1117;BA.debugLine="Txt = Txt & bs";
_txt = _txt+_bs;
 //BA.debugLineNum = 1118;BA.debugLine="sVal = sVal & bs";
_sval = _sval+_bs;
 };
 break; }
case 12: 
case 13: 
case 14: 
case 15: 
case 16: 
case 17: {
 //BA.debugLineNum = 1121;BA.debugLine="If sVal =\"\" Then";
if ((_sval).equals("")) { 
 //BA.debugLineNum = 1122;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"a","b","c","d","y","")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: {
 //BA.debugLineNum = 1124;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1126;BA.debugLine="sVal = Result1";
_sval = BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1128;BA.debugLine="GetValue(bs)";
_getvalue(_bs);
 break; }
case 18: 
case 19: 
case 20: {
 //BA.debugLineNum = 1130;BA.debugLine="If sVal = \"\" Then";
if ((_sval).equals("")) { 
 //BA.debugLineNum = 1131;BA.debugLine="Select Op0";
switch (BA.switchObjectToInt(_op0,"a","b","c","d","y","")) {
case 0: 
case 1: 
case 2: 
case 3: 
case 4: 
case 5: {
 //BA.debugLineNum = 1133;BA.debugLine="Return";
if (true) return "";
 break; }
}
;
 //BA.debugLineNum = 1135;BA.debugLine="sVal = Result1";
_sval = BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1137;BA.debugLine="If Op0 = \"\" Then";
if ((_op0).equals("")) { 
 //BA.debugLineNum = 1138;BA.debugLine="Result1 = sVal";
_result1 = (double)(Double.parseDouble(_sval));
 };
 //BA.debugLineNum = 1140;BA.debugLine="GetValue(bs)";
_getvalue(_bs);
 break; }
case 21: {
 //BA.debugLineNum = 1142;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1143;BA.debugLine="Txt = Txt & CRLF & CRLF";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF;
 //BA.debugLineNum = 1144;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1145;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1146;BA.debugLine="Op0 = \"\"";
_op0 = "";
 };
 break; }
}
;
 //BA.debugLineNum = 1150;BA.debugLine="UpdateTape";
_updatetape();
 //BA.debugLineNum = 1151;BA.debugLine="End Sub";
return "";
}
public static String  _btnexit_click() throws Exception{
 //BA.debugLineNum = 1299;BA.debugLine="Sub btnExit_Click";
 //BA.debugLineNum = 1300;BA.debugLine="Val = 0";
_val = 0;
 //BA.debugLineNum = 1301;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1302;BA.debugLine="Result1 = 0";
_result1 = 0;
 //BA.debugLineNum = 1303;BA.debugLine="New1 = 0";
_new1 = (int) (0);
 //BA.debugLineNum = 1304;BA.debugLine="Txt = \"\"";
_txt = "";
 //BA.debugLineNum = 1305;BA.debugLine="Op0 = \"\"";
_op0 = "";
 //BA.debugLineNum = 1306;BA.debugLine="lblPaperRoll.Text = \"\"";
mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 1307;BA.debugLine="lblPaperRoll.Height = scvPaperRoll.Height";
mostCurrent._lblpaperroll.setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1308;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1309;BA.debugLine="EDITTEXT_QUANTITY.Text = LABEL_LOAD_ANSWER.Text";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(mostCurrent._label_load_answer.getText()));
 //BA.debugLineNum = 1310;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1311;BA.debugLine="SV_MONTHLY.ScrollToNow(53%x)";
mostCurrent._sv_monthly.ScrollToNow(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (53),mostCurrent.activityBA));
 //BA.debugLineNum = 1312;BA.debugLine="End Sub";
return "";
}
public static void  _button_add_click() throws Exception{
ResumableSub_BUTTON_ADD_Click rsub = new ResumableSub_BUTTON_ADD_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_ADD_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_ADD_Click(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
String _transaction_number = "";
String _pass = "";
String _input_number = "";
int _ir = 0;
int _result = 0;
int _i = 0;
String _query = "";
String _unit = "";
int step27;
int limit27;
int step62;
int limit62;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 744;BA.debugLine="Dim transaction_number As String";
_transaction_number = "";
 //BA.debugLineNum = 745;BA.debugLine="Dim pass As String";
_pass = "";
 //BA.debugLineNum = 746;BA.debugLine="Dim input_number As String";
_input_number = "";
 //BA.debugLineNum = 747;BA.debugLine="If LOGIN_MODULE.username <> \"\" Or LOGIN_MODULE.ta";
if (true) break;

case 1:
//if
this.state = 138;
if ((parent.mostCurrent._login_module._username /*String*/ ).equals("") == false || (parent.mostCurrent._login_module._tab_id /*String*/ ).equals("") == false) { 
this.state = 3;
}else {
this.state = 137;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 748;BA.debugLine="If LABEL_LOAD_PERSON1.Text = \"Assign Person\" And";
if (true) break;

case 4:
//if
this.state = 135;
if ((parent.mostCurrent._label_load_person1.getText()).equals("Assign Person") && (parent.mostCurrent._label_load_person2.getText()).equals("Assign Person")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 135;
 //BA.debugLineNum = 749;BA.debugLine="Msgbox2Async(\"Please assign atleast one person.\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please assign atleast one person."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 750;BA.debugLine="SV_MONTHLY.ScrollToNow(9%x)";
parent.mostCurrent._sv_monthly.ScrollToNow(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 751;BA.debugLine="OpenLabel(LABEL_LOAD_PERSON1)";
_openlabel(parent.mostCurrent._label_load_person1);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 753;BA.debugLine="If LABEL_LOAD_DESCRIPTION.Text = \"\" Then";
if (true) break;

case 9:
//if
this.state = 134;
if ((parent.mostCurrent._label_load_description.getText()).equals("")) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 11:
//C
this.state = 134;
 //BA.debugLineNum = 754;BA.debugLine="Msgbox2Async(\"Scan or Choose a product first!\",";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Scan or Choose a product first!"),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 755;BA.debugLine="SV_MONTHLY.ScrollToNow(53%x)";
parent.mostCurrent._sv_monthly.ScrollToNow(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (53),mostCurrent.activityBA));
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 757;BA.debugLine="If EDITTEXT_QUANTITY.Text = \"\" Or EDITTEXT_QUAN";
if (true) break;

case 14:
//if
this.state = 133;
if ((parent.mostCurrent._edittext_quantity.getText()).equals("") || (double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))<=0) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 133;
 //BA.debugLineNum = 758;BA.debugLine="Msgbox2Async(\"You cannot input a zero value qu";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("You cannot input a zero value quantity."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 759;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 760;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 139;
return;
case 139:
//C
this.state = 133;
;
 //BA.debugLineNum = 761;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 762;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 140;
return;
case 140:
//C
this.state = 133;
;
 //BA.debugLineNum = 763;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 765;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = -1 Then";
if (true) break;

case 19:
//if
this.state = 22;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 766;BA.debugLine="CMB_UNIT.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;

case 22:
//C
this.state = 23;
;
 //BA.debugLineNum = 769;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM inventory_disc_table WHERE transaction_id = '"+parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ +"' and product_description = '"+parent.mostCurrent._label_load_description.getText()+"' and unit = '"+parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"'")));
 //BA.debugLineNum = 770;BA.debugLine="If cursor1.RowCount > 0 Then";
if (true) break;

case 23:
//if
this.state = 38;
if (parent._cursor1.getRowCount()>0) { 
this.state = 25;
}else {
this.state = 37;
}if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 771;BA.debugLine="For ir = 0 To cursor1.RowCount - 1";
if (true) break;

case 26:
//for
this.state = 29;
step27 = 1;
limit27 = (int) (parent._cursor1.getRowCount()-1);
_ir = (int) (0) ;
this.state = 141;
if (true) break;

case 141:
//C
this.state = 29;
if ((step27 > 0 && _ir <= limit27) || (step27 < 0 && _ir >= limit27)) this.state = 28;
if (true) break;

case 142:
//C
this.state = 141;
_ir = ((int)(0 + _ir + step27)) ;
if (true) break;

case 28:
//C
this.state = 142;
 //BA.debugLineNum = 772;BA.debugLine="cursor1.Position = ir";
parent._cursor1.setPosition(_ir);
 //BA.debugLineNum = 773;BA.debugLine="input_number = input_number & \" , \" & cursor";
_input_number = _input_number+" , "+parent._cursor1.GetString("transaction_number");
 if (true) break;
if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 775;BA.debugLine="Msgbox2Async(\"The product you adding \" & LABE";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you adding "+parent.mostCurrent._label_load_description.getText()+" has existing transaction at number(s)"+_input_number.substring((int) (3))+" the same unit of "+parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+""+anywheresoftware.b4a.keywords.Common.CRLF+"Do you want to add this another transaction?"),BA.ObjectToCharSequence("Warning"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 776;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 143;
return;
case 143:
//C
this.state = 30;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 777;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 30:
//if
this.state = 35;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 32;
}else {
this.state = 34;
}if (true) break;

case 32:
//C
this.state = 35;
 //BA.debugLineNum = 778;BA.debugLine="pass = 1";
_pass = BA.NumberToString(1);
 if (true) break;

case 34:
//C
this.state = 35;
 //BA.debugLineNum = 780;BA.debugLine="pass = 0";
_pass = BA.NumberToString(0);
 if (true) break;

case 35:
//C
this.state = 38;
;
 if (true) break;

case 37:
//C
this.state = 38;
 //BA.debugLineNum = 783;BA.debugLine="pass = 1";
_pass = BA.NumberToString(1);
 if (true) break;
;
 //BA.debugLineNum = 787;BA.debugLine="If pass = 1 Then";

case 38:
//if
this.state = 132;
if ((_pass).equals(BA.NumberToString(1))) { 
this.state = 40;
}if (true) break;

case 40:
//C
this.state = 41;
 //BA.debugLineNum = 789;BA.debugLine="If CMB_INVTYPE.cmbBox.SelectedIndex = -1 Then";
if (true) break;

case 41:
//if
this.state = 44;
if (parent.mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 43;
}if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 790;BA.debugLine="CMB_INVTYPE.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;
;
 //BA.debugLineNum = 793;BA.debugLine="If CMB_POSITION.cmbBox.SelectedIndex = -1 The";

case 44:
//if
this.state = 47;
if (parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
this.state = 46;
}if (true) break;

case 46:
//C
this.state = 47;
 //BA.debugLineNum = 794;BA.debugLine="CMB_POSITION.cmbBox.SelectedIndex = 0";
parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));
 if (true) break;
;
 //BA.debugLineNum = 797;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.c";

case 47:
//if
this.state = 60;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 49;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 51;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 53;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 55;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 57;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 59;
}if (true) break;

case 49:
//C
this.state = 60;
 //BA.debugLineNum = 798;BA.debugLine="total_pieces = caseper * EDITTEXT_QUANTITY.t";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._caseper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 51:
//C
this.state = 60;
 //BA.debugLineNum = 800;BA.debugLine="total_pieces = pcsper * EDITTEXT_QUANTITY.te";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._pcsper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 53:
//C
this.state = 60;
 //BA.debugLineNum = 802;BA.debugLine="total_pieces = dozper * EDITTEXT_QUANTITY.te";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._dozper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 55:
//C
this.state = 60;
 //BA.debugLineNum = 804;BA.debugLine="total_pieces = boxper * EDITTEXT_QUANTITY.te";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._boxper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 57:
//C
this.state = 60;
 //BA.debugLineNum = 806;BA.debugLine="total_pieces = bagper * EDITTEXT_QUANTITY.te";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._bagper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 59:
//C
this.state = 60;
 //BA.debugLineNum = 808;BA.debugLine="total_pieces = packper * EDITTEXT_QUANTITY.t";
parent._total_pieces = BA.NumberToString((double)(Double.parseDouble(parent._packper))*(double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText())));
 if (true) break;

case 60:
//C
this.state = 61;
;
 //BA.debugLineNum = 811;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT MAX(CA";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as transaction_number from inventory_disc_table WHERE transaction_id = '"+parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ +"'")));
 //BA.debugLineNum = 812;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 61:
//for
this.state = 70;
step62 = 1;
limit62 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 144;
if (true) break;

case 144:
//C
this.state = 70;
if ((step62 > 0 && _i <= limit62) || (step62 < 0 && _i >= limit62)) this.state = 63;
if (true) break;

case 145:
//C
this.state = 144;
_i = ((int)(0 + _i + step62)) ;
if (true) break;

case 63:
//C
this.state = 64;
 //BA.debugLineNum = 813;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 814;BA.debugLine="If cursor2.GetString(\"transaction_number\") =";
if (true) break;

case 64:
//if
this.state = 69;
if (parent._cursor2.GetString("transaction_number")== null || (parent._cursor2.GetString("transaction_number")).equals("")) { 
this.state = 66;
}else {
this.state = 68;
}if (true) break;

case 66:
//C
this.state = 69;
 //BA.debugLineNum = 815;BA.debugLine="transaction_number =1";
_transaction_number = BA.NumberToString(1);
 if (true) break;

case 68:
//C
this.state = 69;
 //BA.debugLineNum = 817;BA.debugLine="transaction_number = cursor2.GetString(\"tra";
_transaction_number = BA.NumberToString((double)(Double.parseDouble(parent._cursor2.GetString("transaction_number")))+1);
 if (true) break;

case 69:
//C
this.state = 145;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 821;BA.debugLine="If CMB_INVTYPE.cmbBox.SelectedIndex = CMB_INV";

case 70:
//if
this.state = 75;
if (parent.mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("COUNT")) { 
this.state = 72;
}else {
this.state = 74;
}if (true) break;

case 72:
//C
this.state = 75;
 //BA.debugLineNum = 822;BA.debugLine="Dim query As String = \"INSERT INTO inventory";
_query = "INSERT INTO inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 823;BA.debugLine="connection.ExecNonQuery2(query,Array As Stri";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ ,_transaction_number,parent._principal_id,parent.mostCurrent._label_load_principal.getText(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._input_type,parent._reason,parent._scan_code,parent.mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._label_load_person1.getText(),parent.mostCurrent._label_load_person2.getText(),"-","-","-",anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"-","-","0",parent.mostCurrent._login_module._tab_id /*String*/ }));
 if (true) break;

case 74:
//C
this.state = 75;
 //BA.debugLineNum = 829;BA.debugLine="Dim query As String = \"INSERT INTO inventory";
_query = "INSERT INTO inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 830;BA.debugLine="connection.ExecNonQuery2(query,Array As Stri";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._monthly_inventory_module._transaction_id /*String*/ ,_transaction_number,parent._principal_id,parent.mostCurrent._label_load_principal.getText(),parent._product_id,parent.mostCurrent._label_load_variant.getText(),parent.mostCurrent._label_load_description.getText(),parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._edittext_quantity.getText(),parent._total_pieces,parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent._input_type,parent._reason,parent._scan_code,parent.mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._label_load_person1.getText(),parent.mostCurrent._label_load_person2.getText(),parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._label_load_person1.getText(),parent.mostCurrent._label_load_person2.getText(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),"0",parent.mostCurrent._login_module._tab_id /*String*/ }));
 if (true) break;

case 75:
//C
this.state = 76;
;
 //BA.debugLineNum = 836;BA.debugLine="Dim unit As String = CMB_UNIT.cmbBox.Selected";
_unit = parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 837;BA.debugLine="If unit = \"PCS\" Then";
if (true) break;

case 76:
//if
this.state = 87;
if ((_unit).equals("PCS")) { 
this.state = 78;
}else if((_unit).equals("DOZ")) { 
this.state = 86;
}if (true) break;

case 78:
//C
this.state = 79;
 //BA.debugLineNum = 838;BA.debugLine="If EDITTEXT_QUANTITY.Text > 1 Then";
if (true) break;

case 79:
//if
this.state = 84;
if ((double)(Double.parseDouble(parent.mostCurrent._edittext_quantity.getText()))>1) { 
this.state = 81;
}else {
this.state = 83;
}if (true) break;

case 81:
//C
this.state = 84;
 //BA.debugLineNum = 839;BA.debugLine="unit = \"PIECES\"";
_unit = "PIECES";
 if (true) break;

case 83:
//C
this.state = 84;
 //BA.debugLineNum = 841;BA.debugLine="unit = \"PIECE\"";
_unit = "PIECE";
 if (true) break;

case 84:
//C
this.state = 87;
;
 if (true) break;

case 86:
//C
this.state = 87;
 //BA.debugLineNum = 844;BA.debugLine="unit = \"DOZEN\"";
_unit = "DOZEN";
 if (true) break;

case 87:
//C
this.state = 88;
;
 //BA.debugLineNum = 846;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 146;
return;
case 146:
//C
this.state = 88;
;
 //BA.debugLineNum = 847;BA.debugLine="TTS1.Speak(EDITTEXT_QUANTITY.Text & \" \" & uni";
parent._tts1.Speak(parent.mostCurrent._edittext_quantity.getText()+" "+_unit+" ADDED.",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 848;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 147;
return;
case 147:
//C
this.state = 88;
;
 //BA.debugLineNum = 849;BA.debugLine="COUNT_TRANSACTION";
_count_transaction();
 //BA.debugLineNum = 850;BA.debugLine="ToastMessageShow(\"Product Added\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Added"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 852;BA.debugLine="If SWITCH_CONTINUES.Value = True Then";
if (true) break;

case 88:
//if
this.state = 131;
if (parent.mostCurrent._switch_continues._getvalue /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 90;
}else {
this.state = 130;
}if (true) break;

case 90:
//C
this.state = 91;
 //BA.debugLineNum = 854;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.";
if (true) break;

case 91:
//if
this.state = 104;
if (parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
this.state = 93;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
this.state = 95;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
this.state = 97;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
this.state = 99;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
this.state = 101;
}else if(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
this.state = 103;
}if (true) break;

case 93:
//C
this.state = 104;
 //BA.debugLineNum = 855;BA.debugLine="caseper = 0";
parent._caseper = BA.NumberToString(0);
 if (true) break;

case 95:
//C
this.state = 104;
 //BA.debugLineNum = 857;BA.debugLine="pcsper = 0";
parent._pcsper = BA.NumberToString(0);
 if (true) break;

case 97:
//C
this.state = 104;
 //BA.debugLineNum = 859;BA.debugLine="boxper = 0";
parent._boxper = BA.NumberToString(0);
 if (true) break;

case 99:
//C
this.state = 104;
 //BA.debugLineNum = 861;BA.debugLine="dozper = 0";
parent._dozper = BA.NumberToString(0);
 if (true) break;

case 101:
//C
this.state = 104;
 //BA.debugLineNum = 863;BA.debugLine="bagper = 0";
parent._bagper = BA.NumberToString(0);
 if (true) break;

case 103:
//C
this.state = 104;
 //BA.debugLineNum = 865;BA.debugLine="packper = 0";
parent._packper = BA.NumberToString(0);
 if (true) break;

case 104:
//C
this.state = 105;
;
 //BA.debugLineNum = 867;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 868;BA.debugLine="If caseper > 0 Then";
if (true) break;

case 105:
//if
this.state = 108;
if ((double)(Double.parseDouble(parent._caseper))>0) { 
this.state = 107;
}if (true) break;

case 107:
//C
this.state = 108;
 //BA.debugLineNum = 869;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 871;BA.debugLine="If pcsper > 0 Then";

case 108:
//if
this.state = 111;
if ((double)(Double.parseDouble(parent._pcsper))>0) { 
this.state = 110;
}if (true) break;

case 110:
//C
this.state = 111;
 //BA.debugLineNum = 872;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 874;BA.debugLine="If boxper > 0 Then";

case 111:
//if
this.state = 114;
if ((double)(Double.parseDouble(parent._boxper))>0) { 
this.state = 113;
}if (true) break;

case 113:
//C
this.state = 114;
 //BA.debugLineNum = 875;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 877;BA.debugLine="If dozper > 0 Then";

case 114:
//if
this.state = 117;
if ((double)(Double.parseDouble(parent._dozper))>0) { 
this.state = 116;
}if (true) break;

case 116:
//C
this.state = 117;
 //BA.debugLineNum = 878;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 880;BA.debugLine="If bagper > 0 Then";

case 117:
//if
this.state = 120;
if ((double)(Double.parseDouble(parent._bagper))>0) { 
this.state = 119;
}if (true) break;

case 119:
//C
this.state = 120;
 //BA.debugLineNum = 881;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 883;BA.debugLine="If packper > 0 Then";

case 120:
//if
this.state = 123;
if ((double)(Double.parseDouble(parent._packper))>0) { 
this.state = 122;
}if (true) break;

case 122:
//C
this.state = 123;
 //BA.debugLineNum = 884;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;
;
 //BA.debugLineNum = 886;BA.debugLine="If 	caseper = 0 And pcsper = 0 And boxper =";

case 123:
//if
this.state = 128;
if ((parent._caseper).equals(BA.NumberToString(0)) && (parent._pcsper).equals(BA.NumberToString(0)) && (parent._boxper).equals(BA.NumberToString(0)) && (parent._dozper).equals(BA.NumberToString(0)) && (parent._packper).equals(BA.NumberToString(0)) && (parent._bagper).equals(BA.NumberToString(0))) { 
this.state = 125;
}else {
this.state = 127;
}if (true) break;

case 125:
//C
this.state = 128;
 //BA.debugLineNum = 887;BA.debugLine="CLEAR_INPUT";
_clear_input();
 //BA.debugLineNum = 888;BA.debugLine="ToastMessageShow(\"No unit remaining\", False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No unit remaining"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 127:
//C
this.state = 128;
 //BA.debugLineNum = 890;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 148;
return;
case 148:
//C
this.state = 128;
;
 //BA.debugLineNum = 891;BA.debugLine="CMB_UNIT.SelectedIndex = 0";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (0));
 //BA.debugLineNum = 892;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 if (true) break;

case 128:
//C
this.state = 131;
;
 //BA.debugLineNum = 894;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 149;
return;
case 149:
//C
this.state = 131;
;
 if (true) break;

case 130:
//C
this.state = 131;
 //BA.debugLineNum = 897;BA.debugLine="CLEAR_INPUT";
_clear_input();
 if (true) break;

case 131:
//C
this.state = 132;
;
 if (true) break;

case 132:
//C
this.state = 133;
;
 if (true) break;

case 133:
//C
this.state = 134;
;
 if (true) break;

case 134:
//C
this.state = 135;
;
 if (true) break;

case 135:
//C
this.state = 138;
;
 if (true) break;

case 137:
//C
this.state = 138;
 //BA.debugLineNum = 904;BA.debugLine="Msgbox2Async(\"TABLET ID AND USERNAME CANNOT READ";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 138:
//C
this.state = -1;
;
 //BA.debugLineNum = 906;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _button_clear_click() throws Exception{
 //BA.debugLineNum = 916;BA.debugLine="Sub BUTTON_CLEAR_Click";
 //BA.debugLineNum = 917;BA.debugLine="CLEAR_INPUT";
_clear_input();
 //BA.debugLineNum = 918;BA.debugLine="End Sub";
return "";
}
public static String  _button_exit_calcu_click() throws Exception{
 //BA.debugLineNum = 1343;BA.debugLine="Sub BUTTON_EXIT_CALCU_Click";
 //BA.debugLineNum = 1344;BA.debugLine="PANEL_BG_CALCU.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_calcu.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1345;BA.debugLine="EDITTEXT_QUANTITY.Text = 0";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(0));
 //BA.debugLineNum = 1346;BA.debugLine="End Sub";
return "";
}
public static void  _button_manual_click() throws Exception{
ResumableSub_BUTTON_MANUAL_Click rsub = new ResumableSub_BUTTON_MANUAL_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_MANUAL_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_MANUAL_Click(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
int _result = 0;
int _qrow = 0;
int _row = 0;
int step16;
int limit16;
int step51;
int limit51;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 625;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 626;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 627;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED\")";
_ls.Add((Object)("BARCODE NOT REGISTERED"));
 //BA.debugLineNum = 628;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 629;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 630;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 631;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 //BA.debugLineNum = 632;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 'sh";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 633;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 40;
return;
case 40:
//C
this.state = 1;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 634;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 1:
//if
this.state = 39;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 635;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sea";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate2),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 636;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 637;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 41;
return;
case 41:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 638;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 4:
//if
this.state = 38;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 640;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._searchtemplate2._selecteditem /*String*/ +"'")));
 //BA.debugLineNum = 641;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 29;
step16 = 1;
limit16 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 42;
if (true) break;

case 42:
//C
this.state = 29;
if ((step16 > 0 && _qrow <= limit16) || (step16 < 0 && _qrow >= limit16)) this.state = 9;
if (true) break;

case 43:
//C
this.state = 42;
_qrow = ((int)(0 + _qrow + step16)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 642;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 643;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 644;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 645;BA.debugLine="principal_id = cursor3.GetString(\"principal_id";
parent._principal_id = parent._cursor3.GetString("principal_id");
 //BA.debugLineNum = 646;BA.debugLine="product_id = cursor3.GetString(\"product_id\")";
parent._product_id = parent._cursor3.GetString("product_id");
 //BA.debugLineNum = 648;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 649;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
if (true) break;

case 10:
//if
this.state = 13;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 12;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 650;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 652;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 13:
//if
this.state = 16;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 653;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 655;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 16:
//if
this.state = 19;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 656;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 658;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 19:
//if
this.state = 22;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 659;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 661;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 22:
//if
this.state = 25;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 662;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 664;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 25:
//if
this.state = 28;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 665;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 28:
//C
this.state = 43;
;
 //BA.debugLineNum = 668;BA.debugLine="caseper = cursor3.GetString(\"CASE_UNIT_PER_PCS";
parent._caseper = parent._cursor3.GetString("CASE_UNIT_PER_PCS");
 //BA.debugLineNum = 669;BA.debugLine="pcsper = cursor3.GetString(\"PCS_UNIT_PER_PCS\")";
parent._pcsper = parent._cursor3.GetString("PCS_UNIT_PER_PCS");
 //BA.debugLineNum = 670;BA.debugLine="dozper = cursor3.GetString(\"DOZ_UNIT_PER_PCS\")";
parent._dozper = parent._cursor3.GetString("DOZ_UNIT_PER_PCS");
 //BA.debugLineNum = 671;BA.debugLine="boxper = cursor3.GetString(\"BOX_UNIT_PER_PCS\")";
parent._boxper = parent._cursor3.GetString("BOX_UNIT_PER_PCS");
 //BA.debugLineNum = 672;BA.debugLine="bagper = cursor3.GetString(\"BAG_UNIT_PER_PCS\")";
parent._bagper = parent._cursor3.GetString("BAG_UNIT_PER_PCS");
 //BA.debugLineNum = 673;BA.debugLine="packper = cursor3.GetString(\"PACK_UNIT_PER_PCS";
parent._packper = parent._cursor3.GetString("PACK_UNIT_PER_PCS");
 //BA.debugLineNum = 675;BA.debugLine="LABEL_LOAD_BARCODE.Text = cursor3.GetString(\"b";
parent.mostCurrent._label_load_barcode.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("bar_code")+("(PCS)")));
 if (true) break;
if (true) break;

case 29:
//C
this.state = 30;
;
 //BA.debugLineNum = 678;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principa";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._principal_id+"'")));
 //BA.debugLineNum = 679;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 30:
//if
this.state = 37;
if (parent._cursor6.getRowCount()>0) { 
this.state = 32;
}if (true) break;

case 32:
//C
this.state = 33;
 //BA.debugLineNum = 680;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 33:
//for
this.state = 36;
step51 = 1;
limit51 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 44;
if (true) break;

case 44:
//C
this.state = 36;
if ((step51 > 0 && _row <= limit51) || (step51 < 0 && _row >= limit51)) this.state = 35;
if (true) break;

case 45:
//C
this.state = 44;
_row = ((int)(0 + _row + step51)) ;
if (true) break;

case 35:
//C
this.state = 45;
 //BA.debugLineNum = 681;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 682;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring";
parent.mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(parent._cursor6.GetString("principal_name")));
 if (true) break;
if (true) break;

case 36:
//C
this.state = 37;
;
 //BA.debugLineNum = 684;BA.debugLine="reason = ls.Get(Result2)";
parent._reason = BA.ObjectToString(_ls.Get(_result2));
 //BA.debugLineNum = 685;BA.debugLine="input_type = \"MANUAL\"";
parent._input_type = "MANUAL";
 //BA.debugLineNum = 686;BA.debugLine="SV_MONTHLY.ScrollToNow(53%x)";
parent.mostCurrent._sv_monthly.ScrollToNow(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (53),mostCurrent.activityBA));
 //BA.debugLineNum = 687;BA.debugLine="OpenSpinner(CMB_UNIT.cmbBox)";
_openspinner(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 688;BA.debugLine="CTRL.HideKeyboard";
parent.mostCurrent._ctrl.HideKeyboard(mostCurrent.activityBA);
 //BA.debugLineNum = 689;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 46;
return;
case 46:
//C
this.state = 37;
;
 //BA.debugLineNum = 690;BA.debugLine="CMB_UNIT.SelectedIndex = -1";
parent.mostCurrent._cmb_unit._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 691;BA.debugLine="scan_code = \"N/A\"";
parent._scan_code = "N/A";
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
 if (true) break;

case 39:
//C
this.state = -1;
;
 //BA.debugLineNum = 697;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static String  _button_see_table_click() throws Exception{
 //BA.debugLineNum = 930;BA.debugLine="Sub BUTTON_SEE_TABLE_Click";
 //BA.debugLineNum = 931;BA.debugLine="If CMB_INVTYPE.cmbBox.SelectedIndex = -1 Then CMB";
if (mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==-1) { 
mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex((int) (0));};
 //BA.debugLineNum = 933;BA.debugLine="If LABEL_LOAD_PERSON1.Text = \"Assign Person\" And";
if ((mostCurrent._label_load_person1.getText()).equals("Assign Person") && (mostCurrent._label_load_person2.getText()).equals("Assign Person") && mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("RECOUNT")) { 
 //BA.debugLineNum = 934;BA.debugLine="Msgbox2Async(\"Please assign atleast one person.\"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please assign atleast one person."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 935;BA.debugLine="SV_MONTHLY.ScrollToNow(9%x)";
mostCurrent._sv_monthly.ScrollToNow(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (9),mostCurrent.activityBA));
 }else {
 //BA.debugLineNum = 937;BA.debugLine="CLEAR_INPUT";
_clear_input();
 //BA.debugLineNum = 939;BA.debugLine="assign1 = LABEL_LOAD_PERSON1.Text";
_assign1 = mostCurrent._label_load_person1.getText();
 //BA.debugLineNum = 940;BA.debugLine="assign2 = LABEL_LOAD_PERSON2.Text";
_assign2 = mostCurrent._label_load_person2.getText();
 //BA.debugLineNum = 942;BA.debugLine="inventory_type = CMB_INVTYPE.cmbBox.SelectedItem";
_inventory_type = mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem();
 //BA.debugLineNum = 944;BA.debugLine="StartActivity(INVENTORY_TABLE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._inventory_table.getObject()));
 };
 //BA.debugLineNum = 947;BA.debugLine="End Sub";
return "";
}
public static String  _calcresult1(String _op) throws Exception{
String _res = "";
 //BA.debugLineNum = 1236;BA.debugLine="Sub CalcResult1(Op As String)";
 //BA.debugLineNum = 1237;BA.debugLine="Select Op";
switch (BA.switchObjectToInt(_op,"a","b","c","d","g","s","x","y")) {
case 0: {
 //BA.debugLineNum = 1239;BA.debugLine="Result1 = Result1 + Val";
_result1 = _result1+_val;
 break; }
case 1: {
 //BA.debugLineNum = 1241;BA.debugLine="Result1 = Result1 - Val";
_result1 = _result1-_val;
 break; }
case 2: {
 //BA.debugLineNum = 1243;BA.debugLine="Result1 = Result1 * Val";
_result1 = _result1*_val;
 break; }
case 3: {
 //BA.debugLineNum = 1245;BA.debugLine="Result1 = Result1 / Val";
_result1 = _result1/(double)_val;
 break; }
case 4: {
 //BA.debugLineNum = 1247;BA.debugLine="Result1 = Result1 * Result1";
_result1 = _result1*_result1;
 break; }
case 5: {
 //BA.debugLineNum = 1249;BA.debugLine="Result1 = Sqrt(Result1)";
_result1 = anywheresoftware.b4a.keywords.Common.Sqrt(_result1);
 break; }
case 6: {
 //BA.debugLineNum = 1251;BA.debugLine="If Result1 <> 0 Then";
if (_result1!=0) { 
 //BA.debugLineNum = 1252;BA.debugLine="Result1 = 1 / Result1";
_result1 = 1/(double)_result1;
 };
 break; }
case 7: {
 //BA.debugLineNum = 1255;BA.debugLine="Result1 = Result1 * Val / 100";
_result1 = _result1*_val/(double)100;
 break; }
}
;
 //BA.debugLineNum = 1257;BA.debugLine="Dim res As String = Result1";
_res = BA.NumberToString(_result1);
 //BA.debugLineNum = 1258;BA.debugLine="LABEL_LOAD_ANSWER.Text = res";
mostCurrent._label_load_answer.setText(BA.ObjectToCharSequence(_res));
 //BA.debugLineNum = 1259;BA.debugLine="End Sub";
return "";
}
public static String  _clear_input() throws Exception{
 //BA.debugLineNum = 907;BA.debugLine="Sub CLEAR_INPUT";
 //BA.debugLineNum = 908;BA.debugLine="LABEL_LOAD_BARCODE.Text = \"\"";
mostCurrent._label_load_barcode.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 909;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = \"\"";
mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 910;BA.debugLine="LABEL_LOAD_VARIANT.Text = \"\"";
mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 911;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = \"\"";
mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 912;BA.debugLine="CMB_UNIT.cmbBox.Clear";
mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 913;BA.debugLine="EDITTEXT_QUANTITY.Text = \"\"";
mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 914;BA.debugLine="PRINCIPAL_SELECT";
_principal_select();
 //BA.debugLineNum = 915;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_invtype_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_INVTYPE_SelectedIndexChanged rsub = new ResumableSub_CMB_INVTYPE_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_INVTYPE_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_INVTYPE_SelectedIndexChanged(wingan.app.montlhy_inventory2_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.montlhy_inventory2_module parent;
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
 //BA.debugLineNum = 275;BA.debugLine="INV_COLOR";
_inv_color();
 //BA.debugLineNum = 276;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 277;BA.debugLine="OpenLabel(LABEL_LOAD_PERSON1)";
_openlabel(parent.mostCurrent._label_load_person1);
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _cmb_position_selectedindexchanged(int _index) throws Exception{
 //BA.debugLineNum = 401;BA.debugLine="Sub CMB_POSITION_SelectedIndexChanged (Index As In";
 //BA.debugLineNum = 402;BA.debugLine="SV_MONTHLY.ScrollToNow(53%x)";
mostCurrent._sv_monthly.ScrollToNow(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (53),mostCurrent.activityBA));
 //BA.debugLineNum = 403;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_unit_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_UNIT_SelectedIndexChanged rsub = new ResumableSub_CMB_UNIT_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_UNIT_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_UNIT_SelectedIndexChanged(wingan.app.montlhy_inventory2_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.montlhy_inventory2_module parent;
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
 //BA.debugLineNum = 727;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 728;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 729;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 730;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _continues() throws Exception{
 //BA.debugLineNum = 615;BA.debugLine="Sub CONTINUES";
 //BA.debugLineNum = 616;BA.debugLine="If SWITCH_CONTINUES.Value = True Then";
if (mostCurrent._switch_continues._getvalue /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 617;BA.debugLine="ToastMessageShow(\"Continues mode : ON\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Continues mode : ON"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 618;BA.debugLine="LABEL_CONTINUES.TextColor = Colors.Green";
mostCurrent._label_continues.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 }else {
 //BA.debugLineNum = 620;BA.debugLine="ToastMessageShow(\"Continues mode : OFF\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Continues mode : OFF"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 621;BA.debugLine="LABEL_CONTINUES.TextColor = Colors.Red";
mostCurrent._label_continues.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 };
 //BA.debugLineNum = 623;BA.debugLine="End Sub";
return "";
}
public static String  _count_transaction() throws Exception{
int _i = 0;
 //BA.debugLineNum = 731;BA.debugLine="Sub COUNT_TRANSACTION";
 //BA.debugLineNum = 732;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT COUNT(tran";
_cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT COUNT(transaction_number) as transaction_count from inventory_disc_table WHERE transaction_id = '"+mostCurrent._monthly_inventory_module._transaction_id /*String*/ +"'")));
 //BA.debugLineNum = 733;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
{
final int step2 = 1;
final int limit2 = (int) (_cursor6.getRowCount()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 734;BA.debugLine="cursor6.Position = i";
_cursor6.setPosition(_i);
 //BA.debugLineNum = 735;BA.debugLine="If cursor6.GetString(\"transaction_count\") = Null";
if (_cursor6.GetString("transaction_count")== null || (_cursor6.GetString("transaction_count")).equals("")) { 
 //BA.debugLineNum = 736;BA.debugLine="UpdateIcon(\"table\", AddBadgeToIcon(tableBitmap,";
_updateicon("table",_addbadgetoicon(_tablebitmap,(int) (0)));
 }else {
 //BA.debugLineNum = 738;BA.debugLine="UpdateIcon(\"table\", AddBadgeToIcon(tableBitmap,";
_updateicon("table",_addbadgetoicon(_tablebitmap,(int)(Double.parseDouble(_cursor6.GetString("transaction_count")))));
 //BA.debugLineNum = 739;BA.debugLine="Log(cursor6.GetString(\"transaction_count\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("766387976",_cursor6.GetString("transaction_count"),0);
 };
 }
};
 //BA.debugLineNum = 742;BA.debugLine="End Sub";
return "";
}
public static String  _edittext_quantity_enterpressed() throws Exception{
 //BA.debugLineNum = 1351;BA.debugLine="Sub EDITTEXT_QUANTITY_EnterPressed";
 //BA.debugLineNum = 1352;BA.debugLine="OpenButton(BUTTON_ADD)";
_openbutton((anywheresoftware.b4a.objects.LabelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.LabelWrapper(), (android.widget.TextView)(mostCurrent._button_add.getObject())));
 //BA.debugLineNum = 1353;BA.debugLine="End Sub";
return "";
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 974;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 975;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 976;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 977;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 978;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 981;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 982;BA.debugLine="End Sub";
return null;
}
public static String  _getvalue(String _op) throws Exception{
 //BA.debugLineNum = 1152;BA.debugLine="Sub GetValue(Op As String)";
 //BA.debugLineNum = 1153;BA.debugLine="If Op0 = \"e\" And (Op = \"s\" Or Op = \"g\" Or Op = \"x";
if ((_op0).equals("e") && ((_op).equals("s") || (_op).equals("g") || (_op).equals("x"))) { 
 //BA.debugLineNum = 1154;BA.debugLine="Val = Result1";
_val = _result1;
 }else {
 //BA.debugLineNum = 1156;BA.debugLine="Val = sVal";
_val = (double)(Double.parseDouble(_sval));
 };
 //BA.debugLineNum = 1159;BA.debugLine="sVal = \"\"";
_sval = "";
 //BA.debugLineNum = 1161;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1162;BA.debugLine="If Op0 = \"a\" Or Op0 = \"b\" Or Op0 = \"c\" Or Op0 =";
if ((_op0).equals("a") || (_op0).equals("b") || (_op0).equals("c") || (_op0).equals("d") || (_op0).equals("y")) { 
 //BA.debugLineNum = 1163;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1164;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1166;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1167;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1168;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1169;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1170;BA.debugLine="Op0 = \"e\"";
_op0 = "e";
 //BA.debugLineNum = 1171;BA.debugLine="Op = \"e\"";
_op = "e";
 //BA.debugLineNum = 1172;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1175;BA.debugLine="If New1 = 1 Then";
if (_new1==1) { 
 //BA.debugLineNum = 1176;BA.debugLine="Result1 = Val";
_result1 = _val;
 //BA.debugLineNum = 1177;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1178;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1179;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1180;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1181;BA.debugLine="Op = \"e\"";
_op = "e";
 };
 //BA.debugLineNum = 1183;BA.debugLine="UpdateTape";
_updatetape();
 //BA.debugLineNum = 1184;BA.debugLine="New1 = 2";
_new1 = (int) (2);
 //BA.debugLineNum = 1185;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1186;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1189;BA.debugLine="If Op = \"e\" Then";
if ((_op).equals("e")) { 
 //BA.debugLineNum = 1190;BA.debugLine="If Op0 = \"e\" Then";
if ((_op0).equals("e")) { 
 //BA.debugLineNum = 1191;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1193;BA.debugLine="Txt = Txt & CRLF & \" =  \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+" =  ";
 //BA.debugLineNum = 1194;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1195;BA.debugLine="Txt = Txt & Result1";
_txt = _txt+BA.NumberToString(_result1);
 }else {
 //BA.debugLineNum = 1197;BA.debugLine="If Op0 = \"g\" Or Op0 = \"s\" Or Op0 = \"x\" Then";
if ((_op0).equals("g") || (_op0).equals("s") || (_op0).equals("x")) { 
 //BA.debugLineNum = 1198;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1199;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1200;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 1203;BA.debugLine="CalcResult1(Op0)";
_calcresult1(_op0);
 //BA.debugLineNum = 1204;BA.debugLine="If Op0<>\"e\" Then";
if ((_op0).equals("e") == false) { 
 //BA.debugLineNum = 1205;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 };
 //BA.debugLineNum = 1207;BA.debugLine="Operation(Op)";
_operation(_op);
 //BA.debugLineNum = 1208;BA.debugLine="If Op = \"g\" Or Op = \"s\" Or Op = \"x\" Then";
if ((_op).equals("g") || (_op).equals("s") || (_op).equals("x")) { 
 //BA.debugLineNum = 1209;BA.debugLine="CalcResult1(Op)";
_calcresult1(_op);
 //BA.debugLineNum = 1210;BA.debugLine="Txt = Txt & \"  = \" & Result1";
_txt = _txt+"  = "+BA.NumberToString(_result1);
 //BA.debugLineNum = 1211;BA.debugLine="Op = \"e\"";
_op = "e";
 };
 };
 //BA.debugLineNum = 1214;BA.debugLine="Op0 = Op";
_op0 = _op;
 //BA.debugLineNum = 1215;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 65;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 70;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 73;BA.debugLine="Dim ScannerMacAddress As String";
mostCurrent._scannermacaddress = "";
 //BA.debugLineNum = 74;BA.debugLine="Dim ScannerOnceConnected As Boolean";
_scanneronceconnected = false;
 //BA.debugLineNum = 77;BA.debugLine="Dim btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn";
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
 //BA.debugLineNum = 78;BA.debugLine="Dim btnBack, btnClr, btnExit As Button";
mostCurrent._btnback = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnclr = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnexit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Dim lblPaperRoll As Label";
mostCurrent._lblpaperroll = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Dim scvPaperRoll As ScrollView";
mostCurrent._scvpaperroll = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Dim pnlKeyboard As Panel";
mostCurrent._pnlkeyboard = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Dim stu As StringUtils";
mostCurrent._stu = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 84;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 85;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 87;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 88;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 90;BA.debugLine="Private SearchTemplate2 As B4XSearchTemplate";
mostCurrent._searchtemplate2 = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 91;BA.debugLine="Private SearchTemplate3 As B4XSearchTemplate";
mostCurrent._searchtemplate3 = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 93;BA.debugLine="Private SV_MONTHLY As ScrollView";
mostCurrent._sv_monthly = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 95;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Private CMB_INVTYPE As B4XComboBox";
mostCurrent._cmb_invtype = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 98;BA.debugLine="Private LABEL_LOAD_PERSON1 As Label";
mostCurrent._label_load_person1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private LABEL_LOAD_PERSON2 As Label";
mostCurrent._label_load_person2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 101;BA.debugLine="Private EDITTEXT_QUANTITY As EditText";
mostCurrent._edittext_quantity = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 103;BA.debugLine="Private LABEL_LOAD_DESCRIPTION As Label";
mostCurrent._label_load_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 104;BA.debugLine="Private LABEL_LOAD_VARIANT As Label";
mostCurrent._label_load_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 105;BA.debugLine="Private LABEL_LOAD_PRINCIPAL As Label";
mostCurrent._label_load_principal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 106;BA.debugLine="Private LABEL_LOAD_BARCODE As Label";
mostCurrent._label_load_barcode = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Private CMB_POSITION As B4XComboBox";
mostCurrent._cmb_position = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 108;BA.debugLine="Private PANEL_INPUT As Panel";
mostCurrent._panel_input = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Private SWITCH_CONTINUES As B4XSwitch";
mostCurrent._switch_continues = new wingan.app.b4xswitch();
 //BA.debugLineNum = 110;BA.debugLine="Private LABEL_CONTINUES As Label";
mostCurrent._label_continues = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 111;BA.debugLine="Private LABEL_LOAD_ANSWER As Label";
mostCurrent._label_load_answer = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 112;BA.debugLine="Private PANEL_BG_CALCU As Panel";
mostCurrent._panel_bg_calcu = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 113;BA.debugLine="Private BUTTON_ADD As Button";
mostCurrent._button_add = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return "";
}
public static void  _input_manual() throws Exception{
ResumableSub_INPUT_MANUAL rsub = new ResumableSub_INPUT_MANUAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_MANUAL extends BA.ResumableSub {
public ResumableSub_INPUT_MANUAL(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
String _principal_query = "";
anywheresoftware.b4a.objects.collections.List _items = null;
int _i = 0;
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
 //BA.debugLineNum = 699;BA.debugLine="Dim principal_query As String";
_principal_query = "";
 //BA.debugLineNum = 700;BA.debugLine="If MONTHLY_INVENTORY_MODULE.principal_name = \"ALL";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._monthly_inventory_module._principal_name /*String*/ ).equals("ALL")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 701;BA.debugLine="principal_query = \"\"";
_principal_query = "";
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 703;BA.debugLine="principal_query = \"AND principal_id = '\"&MONTHLY";
_principal_query = "AND principal_id = '"+parent.mostCurrent._monthly_inventory_module._principal_id /*String*/ +"'";
 if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 705;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 11;
return;
case 11:
//C
this.state = 7;
;
 //BA.debugLineNum = 706;BA.debugLine="SearchTemplate2.CustomListView1.Clear";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 707;BA.debugLine="Dialog.Title = \"Find Product\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Find Product");
 //BA.debugLineNum = 708;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 709;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 710;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 711;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' "+_principal_query+" ORDER BY product_desc ASC")));
 //BA.debugLineNum = 712;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 10;
step14 = 1;
limit14 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 12;
if (true) break;

case 12:
//C
this.state = 10;
if ((step14 > 0 && _i <= limit14) || (step14 < 0 && _i >= limit14)) this.state = 9;
if (true) break;

case 13:
//C
this.state = 12;
_i = ((int)(0 + _i + step14)) ;
if (true) break;

case 9:
//C
this.state = 13;
 //BA.debugLineNum = 713;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 14;
return;
case 14:
//C
this.state = 13;
;
 //BA.debugLineNum = 714;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 715;BA.debugLine="Items.Add(cursor2.GetString(\"product_desc\"))";
_items.Add((Object)(parent._cursor2.GetString("product_desc")));
 if (true) break;
if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 717;BA.debugLine="SearchTemplate2.SetItems(Items)";
parent.mostCurrent._searchtemplate2._setitems /*Object*/ (_items);
 //BA.debugLineNum = 718;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _input_person1() throws Exception{
ResumableSub_INPUT_PERSON1 rsub = new ResumableSub_INPUT_PERSON1(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_PERSON1 extends BA.ResumableSub {
public ResumableSub_INPUT_PERSON1(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
anywheresoftware.b4a.objects.collections.List _items = null;
int _i = 0;
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
 //BA.debugLineNum = 367;BA.debugLine="SearchTemplate.CustomListView1.Clear";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 368;BA.debugLine="Dialog.Title = \"Assign Person 1\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Assign Person 1");
 //BA.debugLineNum = 369;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 370;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 371;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 372;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT User FROM";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT User FROM users_table WHERE (user <> 'ADMIN' and user <> '"+parent.mostCurrent._label_load_person2.getText()+"') ORDER BY user ASC")));
 //BA.debugLineNum = 373;BA.debugLine="If cursor1.RowCount > 0 Then";
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
 //BA.debugLineNum = 374;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step8 = 1;
limit8 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step8)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 375;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 376;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 377;BA.debugLine="Items.Add(cursor1.GetString(\"User\"))";
_items.Add((Object)(parent._cursor1.GetString("User")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 379;BA.debugLine="SearchTemplate.SetItems(Items)";
parent.mostCurrent._searchtemplate._setitems /*Object*/ (_items);
 if (true) break;

case 9:
//C
this.state = 10;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 382;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _input_person2() throws Exception{
ResumableSub_INPUT_PERSON2 rsub = new ResumableSub_INPUT_PERSON2(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_PERSON2 extends BA.ResumableSub {
public ResumableSub_INPUT_PERSON2(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
anywheresoftware.b4a.objects.collections.List _items2 = null;
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
 //BA.debugLineNum = 384;BA.debugLine="SearchTemplate3.CustomListView1.Refresh";
parent.mostCurrent._searchtemplate3._customlistview1 /*b4a.example3.customlistview*/ ._refresh();
 //BA.debugLineNum = 385;BA.debugLine="SearchTemplate3.CustomListView1.Clear";
parent.mostCurrent._searchtemplate3._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 386;BA.debugLine="Dialog.Title = \"Assign Person 2\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Assign Person 2");
 //BA.debugLineNum = 387;BA.debugLine="Dim Items2 As List";
_items2 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 388;BA.debugLine="Items2.Initialize";
_items2.Initialize();
 //BA.debugLineNum = 389;BA.debugLine="Items2.Clear";
_items2.Clear();
 //BA.debugLineNum = 390;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT User FROM";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT User FROM users_table WHERE (user <> 'ADMIN' and user <> '"+parent.mostCurrent._label_load_person1.getText()+"') ORDER BY user ASC")));
 //BA.debugLineNum = 391;BA.debugLine="If cursor2.RowCount > 0 Then";
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
 //BA.debugLineNum = 392;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step9 = 1;
limit9 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step9)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 393;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 394;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 395;BA.debugLine="Items2.Add(cursor2.GetString(\"User\"))";
_items2.Add((Object)(parent._cursor2.GetString("User")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 397;BA.debugLine="SearchTemplate3.SetItems(Items2)";
parent.mostCurrent._searchtemplate3._setitems /*Object*/ (_items2);
 if (true) break;

case 9:
//C
this.state = 10;
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 400;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _inv_color() throws Exception{
 //BA.debugLineNum = 279;BA.debugLine="Sub INV_COLOR";
 //BA.debugLineNum = 280;BA.debugLine="If CMB_INVTYPE.SelectedIndex = 0 Then";
if (mostCurrent._cmb_invtype._getselectedindex /*int*/ ()==0) { 
 //BA.debugLineNum = 281;BA.debugLine="CMB_INVTYPE.mBase.Color = xui.Color_Red";
mostCurrent._cmb_invtype._mbase /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(mostCurrent._xui.Color_Red);
 }else {
 //BA.debugLineNum = 283;BA.debugLine="CMB_INVTYPE.mBase.Color = xui.Color_green";
mostCurrent._cmb_invtype._mbase /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setColor(mostCurrent._xui.Color_Green);
 };
 //BA.debugLineNum = 285;BA.debugLine="End Sub";
return "";
}
public static void  _label_load_person1_click() throws Exception{
ResumableSub_LABEL_LOAD_PERSON1_Click rsub = new ResumableSub_LABEL_LOAD_PERSON1_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_PERSON1_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_PERSON1_Click(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
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
 //BA.debugLineNum = 306;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 307;BA.debugLine="INPUT_PERSON1";
_input_person1();
 //BA.debugLineNum = 308;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 27;
return;
case 27:
//C
this.state = 1;
;
 //BA.debugLineNum = 309;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 310;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sear";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate),(Object)(""),(Object)("ENTER"),(Object)("CANCEL"));
 //BA.debugLineNum = 311;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 312;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 28;
return;
case 28:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 313;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 26;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}else if(_result==parent.mostCurrent._xui.DialogResponse_Negative) { 
this.state = 11;
}else {
this.state = 25;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 314;BA.debugLine="LABEL_LOAD_PERSON1.Text = SearchTemplate.Selecte";
parent.mostCurrent._label_load_person1.setText(BA.ObjectToCharSequence(parent.mostCurrent._searchtemplate._selecteditem /*String*/ ));
 //BA.debugLineNum = 315;BA.debugLine="Msgbox2Async(\"Do you want to assign second perso";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Do you want to assign second person?"),BA.ObjectToCharSequence("Second Person"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 316;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 29;
return;
case 29:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 317;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 318;BA.debugLine="OpenLabel(LABEL_LOAD_PERSON2)";
_openlabel(parent.mostCurrent._label_load_person2);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 320;BA.debugLine="OpenSpinner(CMB_POSITION.cmbBox)";
_openspinner(parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 if (true) break;

case 9:
//C
this.state = 26;
;
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 323;BA.debugLine="If SearchTemplate.SearchField.Text = \"\" Then";
if (true) break;

case 12:
//if
this.state = 23;
if ((parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettext /*String*/ ()).equals("")) { 
this.state = 14;
}else {
this.state = 16;
}if (true) break;

case 14:
//C
this.state = 23;
 //BA.debugLineNum = 324;BA.debugLine="LABEL_LOAD_PERSON1.Text = \"Assign Person\"";
parent.mostCurrent._label_load_person1.setText(BA.ObjectToCharSequence("Assign Person"));
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 326;BA.debugLine="LABEL_LOAD_PERSON1.Text = SearchTemplate.Search";
parent.mostCurrent._label_load_person1.setText(BA.ObjectToCharSequence(parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettext /*String*/ ().toUpperCase()));
 //BA.debugLineNum = 327;BA.debugLine="Msgbox2Async(\"Do you want to assign second pers";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Do you want to assign second person?"),BA.ObjectToCharSequence("Second Person"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 328;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 17;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 329;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 17:
//if
this.state = 22;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 19;
}else {
this.state = 21;
}if (true) break;

case 19:
//C
this.state = 22;
 //BA.debugLineNum = 330;BA.debugLine="OpenLabel(LABEL_LOAD_PERSON2)";
_openlabel(parent.mostCurrent._label_load_person2);
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 332;BA.debugLine="OpenSpinner(CMB_POSITION.cmbBox)";
_openspinner(parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
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
 //BA.debugLineNum = 335;BA.debugLine="OpenSpinner(CMB_POSITION.cmbBox)";
_openspinner(parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 if (true) break;

case 25:
//C
this.state = 26;
 //BA.debugLineNum = 337;BA.debugLine="LABEL_LOAD_PERSON1.Text = \"Assign Person\"";
parent.mostCurrent._label_load_person1.setText(BA.ObjectToCharSequence("Assign Person"));
 //BA.debugLineNum = 338;BA.debugLine="OpenSpinner(CMB_POSITION.cmbBox)";
_openspinner(parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 if (true) break;

case 26:
//C
this.state = -1;
;
 //BA.debugLineNum = 340;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _label_load_person2_click() throws Exception{
ResumableSub_LABEL_LOAD_PERSON2_Click rsub = new ResumableSub_LABEL_LOAD_PERSON2_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_PERSON2_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_PERSON2_Click(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
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
 //BA.debugLineNum = 342;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 343;BA.debugLine="INPUT_PERSON2";
_input_person2();
 //BA.debugLineNum = 344;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 15;
return;
case 15:
//C
this.state = 1;
;
 //BA.debugLineNum = 345;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 346;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sear";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate3),(Object)(""),(Object)("ENTER"),(Object)("CANCEL"));
 //BA.debugLineNum = 347;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 348;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 16;
return;
case 16:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 349;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 14;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}else if(_result==parent.mostCurrent._xui.DialogResponse_Negative) { 
this.state = 5;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 14;
 //BA.debugLineNum = 350;BA.debugLine="LABEL_LOAD_PERSON2.Text = SearchTemplate3.Select";
parent.mostCurrent._label_load_person2.setText(BA.ObjectToCharSequence(parent.mostCurrent._searchtemplate3._selecteditem /*String*/ ));
 //BA.debugLineNum = 351;BA.debugLine="OpenSpinner(CMB_POSITION.cmbBox)";
_openspinner(parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 353;BA.debugLine="If SearchTemplate3.SearchField.Text = \"\" Then";
if (true) break;

case 6:
//if
this.state = 11;
if ((parent.mostCurrent._searchtemplate3._searchfield /*wingan.app.b4xfloattextfield*/ ._gettext /*String*/ ()).equals("")) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 354;BA.debugLine="LABEL_LOAD_PERSON2.Text = \"Assign Person\"";
parent.mostCurrent._label_load_person2.setText(BA.ObjectToCharSequence("Assign Person"));
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 356;BA.debugLine="LABEL_LOAD_PERSON2.Text = SearchTemplate3.Searc";
parent.mostCurrent._label_load_person2.setText(BA.ObjectToCharSequence(parent.mostCurrent._searchtemplate3._searchfield /*wingan.app.b4xfloattextfield*/ ._gettext /*String*/ ().toUpperCase()));
 if (true) break;

case 11:
//C
this.state = 14;
;
 //BA.debugLineNum = 358;BA.debugLine="CMB_POSITION.SelectedIndex = -1";
parent.mostCurrent._cmb_position._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 359;BA.debugLine="OpenSpinner(CMB_POSITION.cmbBox)";
_openspinner(parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 361;BA.debugLine="LABEL_LOAD_PERSON2.Text = \"Assign Person\"";
parent.mostCurrent._label_load_person2.setText(BA.ObjectToCharSequence("Assign Person"));
 //BA.debugLineNum = 362;BA.debugLine="CMB_POSITION.SelectedIndex = -1";
parent.mostCurrent._cmb_position._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 363;BA.debugLine="OpenSpinner(CMB_POSITION.cmbBox)";
_openspinner(parent.mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 365;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _loadtexts() throws Exception{
String _filename = "";
int _iq = 0;
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _reader = null;
 //BA.debugLineNum = 1320;BA.debugLine="Sub LoadTexts";
 //BA.debugLineNum = 1321;BA.debugLine="Dim FileName As String";
_filename = "";
 //BA.debugLineNum = 1322;BA.debugLine="Dim iq As Int";
_iq = 0;
 //BA.debugLineNum = 1326;BA.debugLine="FileName = \"tapecalc_\" & LanguageID & \".txt\"";
_filename = "tapecalc_"+_languageid+".txt";
 //BA.debugLineNum = 1327;BA.debugLine="If File.Exists(ProgPath, FileName) = False Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(_progpath,_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 1328;BA.debugLine="FileName = \"tapecalc_en.txt\"";
_filename = "tapecalc_en.txt";
 };
 //BA.debugLineNum = 1331;BA.debugLine="Dim Reader As TextReader";
_reader = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1332;BA.debugLine="Reader.Initialize(File.OpenInput(ProgPath, FileNa";
_reader.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_progpath,_filename).getObject()));
 //BA.debugLineNum = 1333;BA.debugLine="iq = 0";
_iq = (int) (0);
 //BA.debugLineNum = 1334;BA.debugLine="Texts(iq) = Reader.ReadLine";
_texts[_iq] = _reader.ReadLine();
 //BA.debugLineNum = 1335;BA.debugLine="Do While Texts(iq) <> Null";
while (_texts[_iq]!= null) {
 //BA.debugLineNum = 1336;BA.debugLine="iq = iq + 1";
_iq = (int) (_iq+1);
 //BA.debugLineNum = 1337;BA.debugLine="Texts(iq) = Reader.ReadLine";
_texts[_iq] = _reader.ReadLine();
 }
;
 //BA.debugLineNum = 1339;BA.debugLine="Reader.Close";
_reader.Close();
 //BA.debugLineNum = 1340;BA.debugLine="End Sub";
return "";
}
public static String  _mainform_resize(double _width,double _height) throws Exception{
 //BA.debugLineNum = 262;BA.debugLine="Sub MainForm_Resize (Width As Double, Height As Do";
 //BA.debugLineNum = 263;BA.debugLine="If Dialog.Visible Then Dialog.Resize(Width, Heigh";
if (mostCurrent._dialog._getvisible /*boolean*/ ()) { 
mostCurrent._dialog._resize /*String*/ ((int) (_width),(int) (_height));};
 //BA.debugLineNum = 264;BA.debugLine="End Sub";
return "";
}
public static String  _oncal() throws Exception{
 //BA.debugLineNum = 1037;BA.debugLine="Sub oncal";
 //BA.debugLineNum = 1038;BA.debugLine="LoadTexts";
_loadtexts();
 //BA.debugLineNum = 1040;BA.debugLine="Scale_Calc.SetRate(0.6)";
mostCurrent._scale_calc._setrate /*String*/ (mostCurrent.activityBA,0.6);
 //BA.debugLineNum = 1041;BA.debugLine="ScaleAuto = Scale_Calc.GetScaleDS";
_scaleauto = mostCurrent._scale_calc._getscaleds /*double*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 1058;BA.debugLine="lblPaperRoll.Initialize(\"lblPaperRoll\")";
mostCurrent._lblpaperroll.Initialize(mostCurrent.activityBA,"lblPaperRoll");
 //BA.debugLineNum = 1059;BA.debugLine="scvPaperRoll.Panel.AddView(lblPaperRoll, 0, 0, 10";
mostCurrent._scvpaperroll.getPanel().AddView((android.view.View)(mostCurrent._lblpaperroll.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1060;BA.debugLine="scvPaperRoll.Panel.Height = scvPaperRoll.Height";
mostCurrent._scvpaperroll.getPanel().setHeight(mostCurrent._scvpaperroll.getHeight());
 //BA.debugLineNum = 1061;BA.debugLine="lblPaperRoll.TextSize = 22 * ScaleAuto";
mostCurrent._lblpaperroll.setTextSize((float) (22*_scaleauto));
 //BA.debugLineNum = 1062;BA.debugLine="lblPaperRoll.Color = Colors.White";
mostCurrent._lblpaperroll.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1063;BA.debugLine="lblPaperRoll.TextColor = Colors.Black";
mostCurrent._lblpaperroll.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 1079;BA.debugLine="End Sub";
return "";
}
public static String  _openbutton(anywheresoftware.b4a.objects.LabelWrapper _bt) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 1355;BA.debugLine="Sub OpenButton(bt As Label)";
 //BA.debugLineNum = 1356;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 1357;BA.debugLine="reflect.Target = bt";
_reflect.Target = (Object)(_bt.getObject());
 //BA.debugLineNum = 1358;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 1359;BA.debugLine="End Sub";
return "";
}
public static String  _openlabel(anywheresoftware.b4a.objects.LabelWrapper _bt) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 256;BA.debugLine="Sub OpenLabel(bt As Label)";
 //BA.debugLineNum = 257;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 258;BA.debugLine="reflect.Target = bt";
_reflect.Target = (Object)(_bt.getObject());
 //BA.debugLineNum = 259;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 260;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 251;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 252;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 253;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 254;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
return "";
}
public static String  _operation(String _op) throws Exception{
 //BA.debugLineNum = 1216;BA.debugLine="Sub Operation(Op As String)";
 //BA.debugLineNum = 1217;BA.debugLine="Select Op";
switch (BA.switchObjectToInt(_op,"a","b","c","d","g","s","x","y")) {
case 0: {
 //BA.debugLineNum = 1219;BA.debugLine="Txt = Txt & CRLF & \"+ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"+ ";
 break; }
case 1: {
 //BA.debugLineNum = 1221;BA.debugLine="Txt = Txt & CRLF & \"- \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"- ";
 break; }
case 2: {
 //BA.debugLineNum = 1223;BA.debugLine="Txt = Txt & CRLF & \"× \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"× ";
 break; }
case 3: {
 //BA.debugLineNum = 1225;BA.debugLine="Txt = Txt & CRLF & \"/ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"/ ";
 break; }
case 4: {
 //BA.debugLineNum = 1227;BA.debugLine="Txt = Txt & CRLF & \"x2 \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"x2 ";
 break; }
case 5: {
 //BA.debugLineNum = 1229;BA.debugLine="Txt = Txt & CRLF & \"√ \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"√ ";
 break; }
case 6: {
 //BA.debugLineNum = 1231;BA.debugLine="Txt = Txt & CRLF & \"1/x \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"1/x ";
 break; }
case 7: {
 //BA.debugLineNum = 1233;BA.debugLine="Txt = Txt & CRLF & \"% \"";
_txt = _txt+anywheresoftware.b4a.keywords.Common.CRLF+"% ";
 break; }
}
;
 //BA.debugLineNum = 1235;BA.debugLine="End Sub";
return "";
}
public static String  _panel_bg_calcu_click() throws Exception{
 //BA.debugLineNum = 1347;BA.debugLine="Sub PANEL_BG_CALCU_Click";
 //BA.debugLineNum = 1348;BA.debugLine="Return True";
if (true) return BA.ObjectToString(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 1349;BA.debugLine="End Sub";
return "";
}
public static String  _populate_invposition() throws Exception{
 //BA.debugLineNum = 286;BA.debugLine="Sub POPULATE_INVPOSITION";
 //BA.debugLineNum = 287;BA.debugLine="CMB_POSITION.cmbBox.Clear";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 288;BA.debugLine="CMB_POSITION.cmbBox.DropdownTextColor = Colors.Bl";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 289;BA.debugLine="CMB_POSITION.cmbBox.TextColor = Colors.Black";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 290;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Isle 1\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Isle 1");
 //BA.debugLineNum = 291;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Isle 2\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Isle 2");
 //BA.debugLineNum = 292;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Isle 3\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Isle 3");
 //BA.debugLineNum = 293;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Isle 4\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Isle 4");
 //BA.debugLineNum = 294;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Isle 5\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Isle 5");
 //BA.debugLineNum = 295;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Isle 6\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Isle 6");
 //BA.debugLineNum = 296;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Isle 7\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Isle 7");
 //BA.debugLineNum = 297;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Isle 8\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Isle 8");
 //BA.debugLineNum = 298;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Room 1\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Room 1");
 //BA.debugLineNum = 299;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Room 2\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Room 2");
 //BA.debugLineNum = 300;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Room 3\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Room 3");
 //BA.debugLineNum = 301;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Room 4\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Room 4");
 //BA.debugLineNum = 302;BA.debugLine="CMB_POSITION.cmbBox.Add(\"Room 5\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("Room 5");
 //BA.debugLineNum = 303;BA.debugLine="CMB_POSITION.cmbBox.Add(\"No specific position\")";
mostCurrent._cmb_position._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("No specific position");
 //BA.debugLineNum = 304;BA.debugLine="End Sub";
return "";
}
public static String  _populate_invtype() throws Exception{
 //BA.debugLineNum = 267;BA.debugLine="Sub POPULATE_INVTYPE";
 //BA.debugLineNum = 268;BA.debugLine="CMB_INVTYPE.cmbBox.Clear";
mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 269;BA.debugLine="CMB_INVTYPE.cmbBox.DropdownTextColor = Colors.Bla";
mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 270;BA.debugLine="CMB_INVTYPE.cmbBox.TextColor = Colors.Black";
mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 271;BA.debugLine="CMB_INVTYPE.cmbBox.Add(\"COUNT\")";
mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("COUNT");
 //BA.debugLineNum = 272;BA.debugLine="CMB_INVTYPE.cmbBox.Add(\"RECOUNT\")";
mostCurrent._cmb_invtype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("RECOUNT");
 //BA.debugLineNum = 273;BA.debugLine="End Sub";
return "";
}
public static String  _principal_select() throws Exception{
 //BA.debugLineNum = 719;BA.debugLine="Sub PRINCIPAL_SELECT";
 //BA.debugLineNum = 720;BA.debugLine="If MONTHLY_INVENTORY_MODULE.principal_name = \"ALL";
if ((mostCurrent._monthly_inventory_module._principal_name /*String*/ ).equals("ALL")) { 
 //BA.debugLineNum = 721;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = \"\"";
mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(""));
 }else {
 //BA.debugLineNum = 723;BA.debugLine="LABEL_LOAD_PRINCIPAL.Text = MONTHLY_INVENTORY_MO";
mostCurrent._label_load_principal.setText(BA.ObjectToCharSequence(mostCurrent._monthly_inventory_module._principal_name /*String*/ ));
 };
 //BA.debugLineNum = 725;BA.debugLine="End Sub";
return "";
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
 //BA.debugLineNum = 21;BA.debugLine="Dim TTS1 As TTS";
_tts1 = new anywheresoftware.b4a.obejcts.TTS();
 //BA.debugLineNum = 24;BA.debugLine="Public principal_id As String";
_principal_id = "";
 //BA.debugLineNum = 25;BA.debugLine="Public product_id As String";
_product_id = "";
 //BA.debugLineNum = 26;BA.debugLine="Public assign1 As String";
_assign1 = "";
 //BA.debugLineNum = 27;BA.debugLine="Public assign2 As String";
_assign2 = "";
 //BA.debugLineNum = 28;BA.debugLine="Public inventory_type As String";
_inventory_type = "";
 //BA.debugLineNum = 29;BA.debugLine="Dim reason As String";
_reason = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim input_type As String";
_input_type = "";
 //BA.debugLineNum = 31;BA.debugLine="Dim caseper As String";
_caseper = "";
 //BA.debugLineNum = 32;BA.debugLine="Dim pcsper As String";
_pcsper = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim dozper As String";
_dozper = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim boxper As String";
_boxper = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim bagper As String";
_bagper = "";
 //BA.debugLineNum = 36;BA.debugLine="Dim packper As String";
_packper = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim total_pieces As String";
_total_pieces = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim scan_code As String";
_scan_code = "";
 //BA.debugLineNum = 41;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 42;BA.debugLine="Dim AStream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 43;BA.debugLine="Dim Ts As Timer";
_ts = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 45;BA.debugLine="Dim cmb_trigger As Int";
_cmb_trigger = 0;
 //BA.debugLineNum = 47;BA.debugLine="Dim tableBitmap As Bitmap";
_tablebitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Dim cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Dim calcBitmap As Bitmap";
_calcbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Dim ProgPath = File.DirRootExternal & \"/TapeCalc\"";
_progpath = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/TapeCalc";
 //BA.debugLineNum = 53;BA.debugLine="Dim ScaleAuto As Double";
_scaleauto = 0;
 //BA.debugLineNum = 54;BA.debugLine="Dim Texts(8) As String";
_texts = new String[(int) (8)];
java.util.Arrays.fill(_texts,"");
 //BA.debugLineNum = 55;BA.debugLine="Dim LanguageID As String";
_languageid = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim sVal = \"\" As String";
_sval = "";
 //BA.debugLineNum = 58;BA.debugLine="Dim Val = 0 As Double";
_val = 0;
 //BA.debugLineNum = 59;BA.debugLine="Dim Op0 = \"\" As String";
_op0 = "";
 //BA.debugLineNum = 60;BA.debugLine="Dim Result1 = 0 As Double";
_result1 = 0;
 //BA.debugLineNum = 61;BA.debugLine="Dim Txt = \"\" As String";
_txt = "";
 //BA.debugLineNum = 62;BA.debugLine="Dim New1 = 0 As Int";
_new1 = (int) (0);
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _serial_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 435;BA.debugLine="Sub Serial_Connected (success As Boolean)";
 //BA.debugLineNum = 436;BA.debugLine="If success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 437;BA.debugLine="Log(\"Scanner is now connected. Waiting for data.";
anywheresoftware.b4a.keywords.Common.LogImpl("765667074","Scanner is now connected. Waiting for data...",0);
 //BA.debugLineNum = 438;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 439;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 440;BA.debugLine="ToastMessageShow(\"Scanner Connected\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner Connected"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 441;BA.debugLine="AStream.Initialize(serial1.InputStream, serial1.";
_astream.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"AStream");
 //BA.debugLineNum = 442;BA.debugLine="ScannerOnceConnected=True";
_scanneronceconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 443;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 445;BA.debugLine="If ScannerOnceConnected=False Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 446;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 447;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 448;BA.debugLine="ToastMessageShow(\"Scanner is off, please turn o";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner is off, please turn on"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 449;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 }else {
 //BA.debugLineNum = 451;BA.debugLine="Log(\"Still waiting for the scanner to reconnect";
anywheresoftware.b4a.keywords.Common.LogImpl("765667088","Still waiting for the scanner to reconnect: "+mostCurrent._scannermacaddress,0);
 //BA.debugLineNum = 452;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 453;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 454;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 };
 //BA.debugLineNum = 457;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 949;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 950;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 951;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 952;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 953;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 954;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 955;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 956;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 957;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 958;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 959;BA.debugLine="End Sub";
return "";
}
public static void  _showpaireddevices() throws Exception{
ResumableSub_ShowPairedDevices rsub = new ResumableSub_ShowPairedDevices(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowPairedDevices extends BA.ResumableSub {
public ResumableSub_ShowPairedDevices(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
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
 //BA.debugLineNum = 408;BA.debugLine="Dim mac As String";
_mac = "";
 //BA.debugLineNum = 409;BA.debugLine="Dim PairedDevices As Map";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 410;BA.debugLine="PairedDevices = serial1.GetPairedDevices";
_paireddevices = parent._serial1.GetPairedDevices();
 //BA.debugLineNum = 411;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 412;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 413;BA.debugLine="For Iq = 0 To PairedDevices.Size - 1";
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
 //BA.debugLineNum = 414;BA.debugLine="mac = PairedDevices.Get(PairedDevices.GetKeyAt(I";
_mac = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_iq)));
 //BA.debugLineNum = 415;BA.debugLine="ls.add(\"Scanner \" & mac.SubString2 (mac.Length -";
_ls.Add((Object)("Scanner "+_mac.substring((int) (_mac.length()-4),_mac.length())));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 417;BA.debugLine="If ls.Size=0 Then";

case 4:
//if
this.state = 7;
if (_ls.getSize()==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 418;BA.debugLine="ls.Add(\"No device(s) found...\")";
_ls.Add((Object)("No device(s) found..."));
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 420;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 421;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 20;
return;
case 20:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 422;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
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
 //BA.debugLineNum = 423;BA.debugLine="If ls.Get(Result)=\"No device(s) found...\" Then";
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
 //BA.debugLineNum = 424;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 426;BA.debugLine="ScannerMacAddress=PairedDevices.Get(PairedDevic";
parent.mostCurrent._scannermacaddress = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))));
 //BA.debugLineNum = 428;BA.debugLine="Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (R";
anywheresoftware.b4a.keywords.Common.LogImpl("765601557",BA.ObjectToString(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))),0);
 //BA.debugLineNum = 429;BA.debugLine="serial1.Connect(ScannerMacAddress)";
parent._serial1.Connect(processBA,parent.mostCurrent._scannermacaddress);
 //BA.debugLineNum = 430;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 431;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
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
 //BA.debugLineNum = 434;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _switch_continues_valuechanged(boolean _value) throws Exception{
 //BA.debugLineNum = 612;BA.debugLine="Sub SWITCH_CONTINUES_ValueChanged (Value As Boolea";
 //BA.debugLineNum = 613;BA.debugLine="CONTINUES";
_continues();
 //BA.debugLineNum = 614;BA.debugLine="End Sub";
return "";
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 602;BA.debugLine="Sub Timer_Tick";
 //BA.debugLineNum = 603;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 604;BA.debugLine="serial1.Connect(ScannerMacAddress)";
_serial1.Connect(processBA,mostCurrent._scannermacaddress);
 //BA.debugLineNum = 605;BA.debugLine="Log (\"Trying to connect...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("765929219","Trying to connect...",0);
 //BA.debugLineNum = 606;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 607;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 608;BA.debugLine="End Sub";
return "";
}
public static String  _tts1_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 921;BA.debugLine="Sub TTS1_Ready (Success As Boolean)";
 //BA.debugLineNum = 922;BA.debugLine="If Success Then";
if (_success) { 
 }else {
 //BA.debugLineNum = 926;BA.debugLine="ToastMessageShow(\"Error initializing TTS engine.";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error initializing TTS engine."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 928;BA.debugLine="End Sub";
return "";
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 970;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 971;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 972;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 973;BA.debugLine="End Sub";
return "";
}
public static void  _updatetape() throws Exception{
ResumableSub_UpdateTape rsub = new ResumableSub_UpdateTape(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_UpdateTape extends BA.ResumableSub {
public ResumableSub_UpdateTape(wingan.app.montlhy_inventory2_module parent) {
this.parent = parent;
}
wingan.app.montlhy_inventory2_module parent;
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
 //BA.debugLineNum = 1261;BA.debugLine="Dim hr As Float";
_hr = 0f;
 //BA.debugLineNum = 1263;BA.debugLine="lblPaperRoll.Text = Txt";
parent.mostCurrent._lblpaperroll.setText(BA.ObjectToCharSequence(parent._txt));
 //BA.debugLineNum = 1265;BA.debugLine="hr = stu.MeasureMultilineTextHeight(lblPaperRoll,";
_hr = (float) (parent.mostCurrent._stu.MeasureMultilineTextHeight((android.widget.TextView)(parent.mostCurrent._lblpaperroll.getObject()),BA.ObjectToCharSequence(parent._txt)));
 //BA.debugLineNum = 1266;BA.debugLine="If hr > scvPaperRoll.Height Then";
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
 //BA.debugLineNum = 1267;BA.debugLine="lblPaperRoll.Height = hr";
parent.mostCurrent._lblpaperroll.setHeight((int) (_hr));
 //BA.debugLineNum = 1268;BA.debugLine="scvPaperRoll.Panel.Height = hr";
parent.mostCurrent._scvpaperroll.getPanel().setHeight((int) (_hr));
 //BA.debugLineNum = 1269;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 4;
;
 //BA.debugLineNum = 1270;BA.debugLine="scvPaperRoll.ScrollPosition = hr";
parent.mostCurrent._scvpaperroll.setScrollPosition((int) (_hr));
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 1272;BA.debugLine="End Sub";
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
