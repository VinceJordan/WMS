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

public class price_checking_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static price_checking_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.price_checking_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (price_checking_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.price_checking_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.price_checking_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (price_checking_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (price_checking_module) Resume **");
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
		return price_checking_module.class;
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
            BA.LogInfo("** Activity (price_checking_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (price_checking_module) Pause event (activity is not paused). **");
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
            price_checking_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (price_checking_module) Resume **");
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
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astream = null;
public static anywheresoftware.b4a.objects.Timer _ts = null;
public static String _booking_pcs = "";
public static String _booking_case = "";
public static String _booking_doz = "";
public static String _booking_box = "";
public static String _booking_pack = "";
public static String _booking_bag = "";
public static String _extruck_pcs = "";
public static String _extruck_case = "";
public static String _extruck_doz = "";
public static String _extruck_box = "";
public static String _extruck_pack = "";
public static String _extruck_bag = "";
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _addbitmap = null;
public anywheresoftware.b4a.objects.IME _ctrl = null;
public static String _scannermacaddress = "";
public static boolean _scanneronceconnected = false;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xsearchtemplate _searchtemplate = null;
public wingan.app.b4xsearchtemplate _searchtemplate3 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public wingan.app.b4xcombobox _cmb_pricetype = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_variant = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_description = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_per_pcs = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_per_case = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_per_doz = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_per_box = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_per_pack = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_per_bag = null;
public wingan.app.b4xcombobox _cmb_unit = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext_quantity = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_totprice = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _img_product = null;
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
public ResumableSub_Activity_Create(wingan.app.price_checking_module parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.price_checking_module parent;
boolean _firsttime;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.agraham.reflection.Reflection _ref = null;
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
 //BA.debugLineNum = 93;BA.debugLine="Activity.LoadLayout(\"price_check\")";
parent.mostCurrent._activity.LoadLayout("price_check",mostCurrent.activityBA);
 //BA.debugLineNum = 95;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 96;BA.debugLine="addBitmap = LoadBitmap(File.DirAssets, \"pencil.pn";
parent._addbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"pencil.png");
 //BA.debugLineNum = 98;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 99;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 100;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 101;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 102;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 103;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 104;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 106;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 107;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 108;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 110;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 111;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 115;BA.debugLine="serial1.Initialize(\"Serial\")";
parent._serial1.Initialize("Serial");
 //BA.debugLineNum = 116;BA.debugLine="Ts.Initialize(\"Timer\", 2000)";
parent._ts.Initialize(processBA,"Timer",(long) (2000));
 //BA.debugLineNum = 122;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 123;BA.debugLine="Dialog.Initialize(Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 124;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 125;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 126;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 127;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 128;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 129;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 130;BA.debugLine="Dialog.ButtonsTextColor = Colors.Black";
parent.mostCurrent._dialog._buttonstextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 132;BA.debugLine="SearchTemplate.Initialize";
parent.mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 133;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 134;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 135;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 136;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
parent.mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 137;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
parent.mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 140;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 141;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 144;BA.debugLine="CMB_PRICETYPE.cmbBox.Add(\"BOOKING\")";
parent.mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOOKING");
 //BA.debugLineNum = 145;BA.debugLine="CMB_PRICETYPE.cmbBox.Add(\"EXTRUCK\")";
parent.mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("EXTRUCK");
 //BA.debugLineNum = 147;BA.debugLine="CLEAR_ALL";
_clear_all();
 //BA.debugLineNum = 149;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 150;BA.debugLine="Dim Ref As Reflector";
_ref = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 151;BA.debugLine="Ref.Target = EDITTEXT_QUANTITY ' The text field b";
_ref.Target = (Object)(parent.mostCurrent._edittext_quantity.getObject());
 //BA.debugLineNum = 152;BA.debugLine="Ref.RunMethod2(\"setImeOptions\", 268435456, \"java.";
_ref.RunMethod2("setImeOptions",BA.NumberToString(268435456),"java.lang.int");
 //BA.debugLineNum = 154;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 155;BA.debugLine="bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 156;BA.debugLine="EDITTEXT_QUANTITY.Background = bg";
parent.mostCurrent._edittext_quantity.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 157;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 158;BA.debugLine="INPUT_MANUAL";
_input_manual();
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
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
public ResumableSub_Activity_CreateMenu(wingan.app.price_checking_module parent,de.amberhome.objects.appcompat.ACMenuWrapper _menu) {
this.parent = parent;
this._menu = _menu;
}
wingan.app.price_checking_module parent;
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
 //BA.debugLineNum = 161;BA.debugLine="Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item1 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item1 = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("cart"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 162;BA.debugLine="item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS";
_item1.setShowAsAction(_item1.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 163;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",parent._cartbitmap);
 //BA.debugLineNum = 164;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = parent.mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("type product"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 165;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 166;BA.debugLine="UpdateIcon(\"type product\", addBitmap)";
_updateicon("type product",parent._addbitmap);
 //BA.debugLineNum = 167;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 168;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 176;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 177;BA.debugLine="Log (\"Activity paused. Disconnecting...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("768616193","Activity paused. Disconnecting...",0);
 //BA.debugLineNum = 178;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 179;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 180;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 181;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 170;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 171;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 172;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 173;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_menuitemclick(de.amberhome.objects.appcompat.ACMenuItemWrapper _item) throws Exception{
 //BA.debugLineNum = 231;BA.debugLine="Sub ACToolBarLight1_MenuItemClick (Item As ACMenuI";
 //BA.debugLineNum = 232;BA.debugLine="If Item.Title = \"cart\" Then";
if ((_item.getTitle()).equals("cart")) { 
 //BA.debugLineNum = 233;BA.debugLine="Log(\"Resuming...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("769074946","Resuming...",0);
 //BA.debugLineNum = 234;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 235;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 236;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 //BA.debugLineNum = 237;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 238;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 239;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 240;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 242;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 243;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 }else if((_item.getTitle()).equals("type product")) { 
 //BA.debugLineNum = 246;BA.debugLine="MANUAL_SEARCH";
_manual_search();
 };
 //BA.debugLineNum = 248;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 199;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 200;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 201;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 202;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _addbadgetoicon(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp,int _number1) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper _cvs1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _mbmp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _target = null;
 //BA.debugLineNum = 217;BA.debugLine="Sub AddBadgeToIcon(bmp As Bitmap, Number1 As Int)";
 //BA.debugLineNum = 218;BA.debugLine="Dim cvs1 As Canvas";
_cvs1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 219;BA.debugLine="Dim mbmp As Bitmap";
_mbmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 220;BA.debugLine="mbmp.InitializeMutable(32dip, 32dip)";
_mbmp.InitializeMutable(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32)));
 //BA.debugLineNum = 221;BA.debugLine="cvs1.Initialize2(mbmp)";
_cvs1.Initialize2((android.graphics.Bitmap)(_mbmp.getObject()));
 //BA.debugLineNum = 222;BA.debugLine="Dim target As Rect";
_target = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 223;BA.debugLine="target.Initialize(0, 0, mbmp.Width, mbmp.Height)";
_target.Initialize((int) (0),(int) (0),_mbmp.getWidth(),_mbmp.getHeight());
 //BA.debugLineNum = 224;BA.debugLine="cvs1.DrawBitmap(bmp, Null, target)";
_cvs1.DrawBitmap((android.graphics.Bitmap)(_bmp.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_target.getObject()));
 //BA.debugLineNum = 225;BA.debugLine="If Number1 > 0 Then";
if (_number1>0) { 
 //BA.debugLineNum = 226;BA.debugLine="cvs1.DrawCircle(mbmp.Width - 8dip, 8dip, 8dip, C";
_cvs1.DrawCircle((float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),anywheresoftware.b4a.keywords.Common.Colors.Red,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 227;BA.debugLine="cvs1.DrawText(Min(Number1, 1000), mbmp.Width - 8";
_cvs1.DrawText(mostCurrent.activityBA,BA.NumberToString(anywheresoftware.b4a.keywords.Common.Min(_number1,1000)),(float) (_mbmp.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (11))),anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD,(float) (9),anywheresoftware.b4a.keywords.Common.Colors.White,BA.getEnumFromString(android.graphics.Paint.Align.class,"CENTER"));
 };
 //BA.debugLineNum = 229;BA.debugLine="Return mbmp";
if (true) return _mbmp;
 //BA.debugLineNum = 230;BA.debugLine="End Sub";
return null;
}
public static String  _astream_error() throws Exception{
 //BA.debugLineNum = 417;BA.debugLine="Sub AStream_Error";
 //BA.debugLineNum = 418;BA.debugLine="Log(\"Connection broken...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("769337089","Connection broken...",0);
 //BA.debugLineNum = 419;BA.debugLine="AStream.Close";
_astream.Close();
 //BA.debugLineNum = 420;BA.debugLine="serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 421;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 422;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 423;BA.debugLine="If ScannerOnceConnected=True Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 424;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 425;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 426;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 }else {
 //BA.debugLineNum = 428;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 };
 //BA.debugLineNum = 430;BA.debugLine="End Sub";
return "";
}
public static void  _astream_newdata(byte[] _buffer) throws Exception{
ResumableSub_AStream_NewData rsub = new ResumableSub_AStream_NewData(null,_buffer);
rsub.resume(processBA, null);
}
public static class ResumableSub_AStream_NewData extends BA.ResumableSub {
public ResumableSub_AStream_NewData(wingan.app.price_checking_module parent,byte[] _buffer) {
this.parent = parent;
this._buffer = _buffer;
}
wingan.app.price_checking_module parent;
byte[] _buffer;
int _trigger = 0;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _row = 0;
int _result = 0;
int _qrow = 0;
int step7;
int limit7;
int step21;
int limit21;
int step35;
int limit35;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 303;BA.debugLine="Log(\"Received: \" & BytesToString(Buffer, 0, Buffe";
anywheresoftware.b4a.keywords.Common.LogImpl("769271553","Received: "+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8"),0);
 //BA.debugLineNum = 305;BA.debugLine="Dim trigger As Int = 0";
_trigger = (int) (0);
 //BA.debugLineNum = 306;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or case_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or box_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or bag_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER) or pack_bar_code = CAST ('"+anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8")+"' as INTEGER)) and prod_status = '0' ORDER BY product_id")));
 //BA.debugLineNum = 307;BA.debugLine="If cursor2.RowCount >= 2 Then";
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
 //BA.debugLineNum = 308;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 309;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 310;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step7 = 1;
limit7 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 55;
if (true) break;

case 55:
//C
this.state = 7;
if ((step7 > 0 && _row <= limit7) || (step7 < 0 && _row >= limit7)) this.state = 6;
if (true) break;

case 56:
//C
this.state = 55;
_row = ((int)(0 + _row + step7)) ;
if (true) break;

case 6:
//C
this.state = 56;
 //BA.debugLineNum = 311;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 312;BA.debugLine="ls.Add(cursor2.GetString(\"product_desc\"))";
_ls.Add((Object)(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 313;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 if (true) break;
if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 315;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) '";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 316;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 57;
return;
case 57:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 317;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
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
 //BA.debugLineNum = 318;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(_ls.Get(_result)));
 //BA.debugLineNum = 319;BA.debugLine="trigger = 0";
_trigger = (int) (0);
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 321;BA.debugLine="trigger = 1";
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
 //BA.debugLineNum = 326;BA.debugLine="For row = 0 To cursor2.RowCount - 1";
if (true) break;

case 16:
//for
this.state = 19;
step21 = 1;
limit21 = (int) (parent._cursor2.getRowCount()-1);
_row = (int) (0) ;
this.state = 58;
if (true) break;

case 58:
//C
this.state = 19;
if ((step21 > 0 && _row <= limit21) || (step21 < 0 && _row >= limit21)) this.state = 18;
if (true) break;

case 59:
//C
this.state = 58;
_row = ((int)(0 + _row + step21)) ;
if (true) break;

case 18:
//C
this.state = 59;
 //BA.debugLineNum = 327;BA.debugLine="cursor2.Position = row";
parent._cursor2.setPosition(_row);
 //BA.debugLineNum = 328;BA.debugLine="Log(1)";
anywheresoftware.b4a.keywords.Common.LogImpl("769271578",BA.NumberToString(1),0);
 //BA.debugLineNum = 329;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor2.GetString";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor2.GetString("product_desc")));
 //BA.debugLineNum = 330;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 331;BA.debugLine="trigger = 0";
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
 //BA.debugLineNum = 334;BA.debugLine="trigger = 1";
_trigger = (int) (1);
 //BA.debugLineNum = 335;BA.debugLine="Msgbox2Async(\"The barcode you scanned is not REG";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The barcode you scanned is not REGISTERED IN THE SYSTEM."),BA.ObjectToCharSequence("Warning"),"Ok","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 336;BA.debugLine="CLEAR_ALL";
_clear_all();
 if (true) break;
;
 //BA.debugLineNum = 338;BA.debugLine="If trigger = 0 Then";

case 22:
//if
this.state = 54;
if (_trigger==0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 339;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._label_load_description.getText()+"'")));
 //BA.debugLineNum = 340;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 25:
//for
this.state = 53;
step35 = 1;
limit35 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 60;
if (true) break;

case 60:
//C
this.state = 53;
if ((step35 > 0 && _qrow <= limit35) || (step35 < 0 && _qrow >= limit35)) this.state = 27;
if (true) break;

case 61:
//C
this.state = 60;
_qrow = ((int)(0 + _qrow + step35)) ;
if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 341;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 342;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"pr";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 354;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 355;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0 T";
if (true) break;

case 28:
//if
this.state = 31;
if ((double)(Double.parseDouble(parent._cursor3.GetString("CASE_UNIT_PER_PCS")))>0) { 
this.state = 30;
}if (true) break;

case 30:
//C
this.state = 31;
 //BA.debugLineNum = 356;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 358;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 Th";

case 31:
//if
this.state = 34;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 33;
}if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 359;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 361;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 Th";

case 34:
//if
this.state = 37;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 36;
}if (true) break;

case 36:
//C
this.state = 37;
 //BA.debugLineNum = 362;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 364;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 Th";

case 37:
//if
this.state = 40;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 39;
}if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 365;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 367;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 Th";

case 40:
//if
this.state = 43;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 42;
}if (true) break;

case 42:
//C
this.state = 43;
 //BA.debugLineNum = 368;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 370;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0 T";

case 43:
//if
this.state = 46;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 45;
}if (true) break;

case 45:
//C
this.state = 46;
 //BA.debugLineNum = 371;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 46:
//C
this.state = 47;
;
 //BA.debugLineNum = 374;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 62;
return;
case 62:
//C
this.state = 47;
;
 //BA.debugLineNum = 375;BA.debugLine="CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE"));
 //BA.debugLineNum = 376;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 63;
return;
case 63:
//C
this.state = 47;
;
 //BA.debugLineNum = 378;BA.debugLine="BOOKING_PCS = Number.Format3((cursor3.GetString";
parent._booking_pcs = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("PCS_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 379;BA.debugLine="BOOKING_CASE = Number.Format3((cursor3.GetStrin";
parent._booking_case = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("CASE_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 380;BA.debugLine="BOOKING_DOZ = Number.Format3((cursor3.GetString";
parent._booking_doz = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("DOZ_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 381;BA.debugLine="BOOKING_BOX = Number.Format3((cursor3.GetString";
parent._booking_box = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("BOX_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 382;BA.debugLine="BOOKING_PACK = Number.Format3((cursor3.GetStrin";
parent._booking_pack = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("PACK_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 383;BA.debugLine="BOOKING_BAG = Number.Format3((cursor3.GetString";
parent._booking_bag = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("BAG_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 384;BA.debugLine="EXTRUCK_PCS = Number.Format3((cursor3.GetString";
parent._extruck_pcs = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("PCS_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 385;BA.debugLine="EXTRUCK_CASE = Number.Format3((cursor3.GetStrin";
parent._extruck_case = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("CASE_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 386;BA.debugLine="EXTRUCK_DOZ = Number.Format3((cursor3.GetString";
parent._extruck_doz = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("DOZ_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 387;BA.debugLine="EXTRUCK_BOX = Number.Format3((cursor3.GetString";
parent._extruck_box = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("BOX_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 388;BA.debugLine="EXTRUCK_PACK = Number.Format3((cursor3.GetStrin";
parent._extruck_pack = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("PACK_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 389;BA.debugLine="EXTRUCK_BAG = Number.Format3((cursor3.GetString";
parent._extruck_bag = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("BAG_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 391;BA.debugLine="If CMB_PRICETYPE.cmbBox.SelectedIndex = CMB_PRI";
if (true) break;

case 47:
//if
this.state = 52;
if (parent.mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOOKING")) { 
this.state = 49;
}else {
this.state = 51;
}if (true) break;

case 49:
//C
this.state = 52;
 //BA.debugLineNum = 392;BA.debugLine="LABEL_LOAD_PER_PCS.Text = \"₱\"&Number.Format3((";
parent.mostCurrent._label_load_per_pcs.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_pcs))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 393;BA.debugLine="LABEL_LOAD_PER_CASE.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_case.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_case))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 394;BA.debugLine="LABEL_LOAD_PER_DOZ.Text = \"₱\"&Number.Format3((";
parent.mostCurrent._label_load_per_doz.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_doz))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 395;BA.debugLine="LABEL_LOAD_PER_BOX.Text = \"₱\"&Number.Format3((";
parent.mostCurrent._label_load_per_box.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_box))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 396;BA.debugLine="LABEL_LOAD_PER_PACK.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_pack.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_pack))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 397;BA.debugLine="LABEL_LOAD_PER_BAG.Text = \"₱\"&Number.Format3((";
parent.mostCurrent._label_load_per_bag.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_bag))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 399;BA.debugLine="LABEL_LOAD_PER_PCS.Text = \"₱\"& Number.Format3(";
parent.mostCurrent._label_load_per_pcs.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_pcs))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 400;BA.debugLine="LABEL_LOAD_PER_CASE.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_case.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_case))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 401;BA.debugLine="LABEL_LOAD_PER_DOZ.Text = \"₱\"&Number.Format3((";
parent.mostCurrent._label_load_per_doz.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_doz))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 402;BA.debugLine="LABEL_LOAD_PER_BOX.Text = \"₱\"&Number.Format3((";
parent.mostCurrent._label_load_per_box.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_box))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 403;BA.debugLine="LABEL_LOAD_PER_PACK.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_pack.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_pack))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 404;BA.debugLine="LABEL_LOAD_PER_BAG.Text = \"₱\"&Number.Format3((";
parent.mostCurrent._label_load_per_bag.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_bag))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 if (true) break;

case 52:
//C
this.state = 61;
;
 //BA.debugLineNum = 406;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 64;
return;
case 64:
//C
this.state = 61;
;
 //BA.debugLineNum = 407;BA.debugLine="EDITTEXT_QUANTITY.Text = \"0\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 408;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 409;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 65;
return;
case 65:
//C
this.state = 61;
;
 //BA.debugLineNum = 410;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 411;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 412;BA.debugLine="FORMULA";
_formula();
 if (true) break;
if (true) break;

case 53:
//C
this.state = 54;
;
 if (true) break;

case 54:
//C
this.state = -1;
;
 //BA.debugLineNum = 416;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _inputlist_result(int _result) throws Exception{
}
public static String  _astream_terminated() throws Exception{
 //BA.debugLineNum = 431;BA.debugLine="Sub AStream_Terminated";
 //BA.debugLineNum = 432;BA.debugLine="Log(\"Connection terminated...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("769402625","Connection terminated...",0);
 //BA.debugLineNum = 433;BA.debugLine="AStream_Error";
_astream_error();
 //BA.debugLineNum = 434;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 194;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 195;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 196;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 197;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return null;
}
public static String  _clear_all() throws Exception{
 //BA.debugLineNum = 464;BA.debugLine="Sub CLEAR_ALL";
 //BA.debugLineNum = 465;BA.debugLine="LABEL_LOAD_VARIANT.Text = \"-\"";
mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 466;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = \"-\"";
mostCurrent._label_load_description.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 467;BA.debugLine="LABEL_LOAD_PER_PCS.Text = \"-\"";
mostCurrent._label_load_per_pcs.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 468;BA.debugLine="LABEL_LOAD_PER_CASE.Text = \"-\"";
mostCurrent._label_load_per_case.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 469;BA.debugLine="LABEL_LOAD_PER_DOZ.Text = \"-\"";
mostCurrent._label_load_per_doz.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 470;BA.debugLine="LABEL_LOAD_PER_BOX.Text = \"-\"";
mostCurrent._label_load_per_box.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 471;BA.debugLine="LABEL_LOAD_PER_PACK.Text = \"-\"";
mostCurrent._label_load_per_pack.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 472;BA.debugLine="LABEL_LOAD_PER_BAG.Text = \"-\"";
mostCurrent._label_load_per_bag.setText(BA.ObjectToCharSequence("-"));
 //BA.debugLineNum = 473;BA.debugLine="End Sub";
return "";
}
public static String  _cmb_pricetype_selectedindexchanged(int _index) throws Exception{
 //BA.debugLineNum = 444;BA.debugLine="Sub CMB_PRICETYPE_SelectedIndexChanged (Index As I";
 //BA.debugLineNum = 445;BA.debugLine="If LABEL_LOAD_VARIANT.text <> \"-\" Then";
if ((mostCurrent._label_load_variant.getText()).equals("-") == false) { 
 //BA.debugLineNum = 446;BA.debugLine="If CMB_PRICETYPE.cmbBox.SelectedIndex = CMB_PRIC";
if (mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOOKING")) { 
 //BA.debugLineNum = 447;BA.debugLine="LABEL_LOAD_PER_PCS.Text = \"₱\"&Number.Format3((B";
mostCurrent._label_load_per_pcs.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_booking_pcs))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 448;BA.debugLine="LABEL_LOAD_PER_CASE.Text = \"₱\"&Number.Format3((";
mostCurrent._label_load_per_case.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_booking_case))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 449;BA.debugLine="LABEL_LOAD_PER_DOZ.Text = \"₱\"&Number.Format3((B";
mostCurrent._label_load_per_doz.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_booking_doz))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 450;BA.debugLine="LABEL_LOAD_PER_BOX.Text = \"₱\"&Number.Format3((B";
mostCurrent._label_load_per_box.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_booking_box))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 451;BA.debugLine="LABEL_LOAD_PER_PACK.Text = \"₱\"&Number.Format3((";
mostCurrent._label_load_per_pack.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_booking_pack))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 452;BA.debugLine="LABEL_LOAD_PER_BAG.Text = \"₱\"&Number.Format3((B";
mostCurrent._label_load_per_bag.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_booking_bag))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else {
 //BA.debugLineNum = 454;BA.debugLine="LABEL_LOAD_PER_PCS.Text = \"₱\"& Number.Format3((";
mostCurrent._label_load_per_pcs.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_extruck_pcs))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 455;BA.debugLine="LABEL_LOAD_PER_CASE.Text = \"₱\"&Number.Format3((";
mostCurrent._label_load_per_case.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_extruck_case))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 456;BA.debugLine="LABEL_LOAD_PER_DOZ.Text = \"₱\"&Number.Format3((E";
mostCurrent._label_load_per_doz.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_extruck_doz))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 457;BA.debugLine="LABEL_LOAD_PER_BOX.Text = \"₱\"&Number.Format3((E";
mostCurrent._label_load_per_box.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_extruck_box))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 458;BA.debugLine="LABEL_LOAD_PER_PACK.Text = \"₱\"&Number.Format3((";
mostCurrent._label_load_per_pack.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_extruck_pack))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 459;BA.debugLine="LABEL_LOAD_PER_BAG.Text = \"₱\"&Number.Format3((E";
mostCurrent._label_load_per_bag.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((_extruck_bag))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 };
 };
 //BA.debugLineNum = 462;BA.debugLine="End Sub";
return "";
}
public static String  _edittext_quantity_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 515;BA.debugLine="Sub EDITTEXT_QUANTITY_TextChanged (Old As String,";
 //BA.debugLineNum = 516;BA.debugLine="FORMULA";
_formula();
 //BA.debugLineNum = 517;BA.debugLine="End Sub";
return "";
}
public static String  _formula() throws Exception{
 //BA.debugLineNum = 475;BA.debugLine="Sub FORMULA";
 //BA.debugLineNum = 476;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBo";
if (mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("")) { 
 }else {
 //BA.debugLineNum = 479;BA.debugLine="If EDITTEXT_QUANTITY.Text = \"\" Then";
if ((mostCurrent._edittext_quantity.getText()).equals("")) { 
 //BA.debugLineNum = 480;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱.00\"";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱.00"));
 }else {
 //BA.debugLineNum = 482;BA.debugLine="If CMB_PRICETYPE.cmbBox.SelectedIndex = CMB_PRI";
if (mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOOKING")) { 
 //BA.debugLineNum = 483;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cm";
if (mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
 //BA.debugLineNum = 484;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_booking_pcs))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
 //BA.debugLineNum = 486;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_booking_case))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
 //BA.debugLineNum = 488;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_booking_box))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
 //BA.debugLineNum = 490;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_booking_doz))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
 //BA.debugLineNum = 492;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_booking_pack))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
 //BA.debugLineNum = 494;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_booking_bag))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 };
 }else {
 //BA.debugLineNum = 497;BA.debugLine="If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cm";
if (mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PCS")) { 
 //BA.debugLineNum = 498;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_extruck_pcs))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE")) { 
 //BA.debugLineNum = 500;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_extruck_case))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOX")) { 
 //BA.debugLineNum = 502;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_extruck_box))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("DOZ")) { 
 //BA.debugLineNum = 504;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_extruck_doz))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("PACK")) { 
 //BA.debugLineNum = 506;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_extruck_pack))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 }else if(mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BAG")) { 
 //BA.debugLineNum = 508;BA.debugLine="LABEL_LOAD_TOTPRICE.Text = \"₱\"&Number.Format3";
mostCurrent._label_load_totprice.setText(BA.ObjectToCharSequence("₱"+mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,((double)(Double.parseDouble(_extruck_bag))*(double)(Double.parseDouble(mostCurrent._edittext_quantity.getText()))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 };
 };
 };
 };
 //BA.debugLineNum = 513;BA.debugLine="End Sub";
return "";
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 208;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 209;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 210;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 211;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 212;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 215;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 216;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 43;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 48;BA.debugLine="Dim CTRL As IME";
mostCurrent._ctrl = new anywheresoftware.b4a.objects.IME();
 //BA.debugLineNum = 51;BA.debugLine="Dim ScannerMacAddress As String";
mostCurrent._scannermacaddress = "";
 //BA.debugLineNum = 52;BA.debugLine="Dim ScannerOnceConnected As Boolean";
_scanneronceconnected = false;
 //BA.debugLineNum = 54;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 55;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 57;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 58;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 60;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 61;BA.debugLine="Private SearchTemplate3 As B4XSearchTemplate";
mostCurrent._searchtemplate3 = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 63;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 64;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private CMB_PRICETYPE As B4XComboBox";
mostCurrent._cmb_pricetype = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 66;BA.debugLine="Private LABEL_LOAD_VARIANT As Label";
mostCurrent._label_load_variant = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private LABEL_LOAD_DESCRIPTION As Label";
mostCurrent._label_load_description = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private LABEL_LOAD_PER_PCS As Label";
mostCurrent._label_load_per_pcs = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private LABEL_LOAD_PER_CASE As Label";
mostCurrent._label_load_per_case = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Private LABEL_LOAD_PER_DOZ As Label";
mostCurrent._label_load_per_doz = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private LABEL_LOAD_PER_BOX As Label";
mostCurrent._label_load_per_box = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private LABEL_LOAD_PER_PACK As Label";
mostCurrent._label_load_per_pack = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private LABEL_LOAD_PER_BAG As Label";
mostCurrent._label_load_per_bag = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private CMB_UNIT As B4XComboBox";
mostCurrent._cmb_unit = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 75;BA.debugLine="Private EDITTEXT_QUANTITY As EditText";
mostCurrent._edittext_quantity = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Private LABEL_LOAD_TOTPRICE As Label";
mostCurrent._label_load_totprice = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Private IMG_PRODUCT As ImageView";
mostCurrent._img_product = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static void  _input_manual() throws Exception{
ResumableSub_INPUT_MANUAL rsub = new ResumableSub_INPUT_MANUAL(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_INPUT_MANUAL extends BA.ResumableSub {
public ResumableSub_INPUT_MANUAL(wingan.app.price_checking_module parent) {
this.parent = parent;
}
wingan.app.price_checking_module parent;
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
 //BA.debugLineNum = 607;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = 1;
;
 //BA.debugLineNum = 608;BA.debugLine="SearchTemplate.CustomListView1.Clear";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 609;BA.debugLine="Dialog.Title = \"Find Product\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Find Product");
 //BA.debugLineNum = 610;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 611;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 612;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 613;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' ORDER BY product_desc ASC")));
 //BA.debugLineNum = 614;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step8 = 1;
limit8 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 4;
if ((step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8)) this.state = 3;
if (true) break;

case 7:
//C
this.state = 6;
_i = ((int)(0 + _i + step8)) ;
if (true) break;

case 3:
//C
this.state = 7;
 //BA.debugLineNum = 615;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 8;
return;
case 8:
//C
this.state = 7;
;
 //BA.debugLineNum = 616;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 617;BA.debugLine="Items.Add(cursor2.GetString(\"product_desc\"))";
_items.Add((Object)(parent._cursor2.GetString("product_desc")));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 619;BA.debugLine="SearchTemplate.SetItems(Items)";
parent.mostCurrent._searchtemplate._setitems /*Object*/ (_items);
 //BA.debugLineNum = 620;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _manual_search() throws Exception{
ResumableSub_MANUAL_SEARCH rsub = new ResumableSub_MANUAL_SEARCH(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_MANUAL_SEARCH extends BA.ResumableSub {
public ResumableSub_MANUAL_SEARCH(wingan.app.price_checking_module parent) {
this.parent = parent;
}
wingan.app.price_checking_module parent;
anywheresoftware.b4a.objects.collections.List _ls = null;
int _result2 = 0;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
int _result = 0;
int _qrow = 0;
int step16;
int limit16;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 520;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 521;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 522;BA.debugLine="ls.Add(\"BARCODE NOT REGISTERED\")";
_ls.Add((Object)("BARCODE NOT REGISTERED"));
 //BA.debugLineNum = 523;BA.debugLine="ls.Add(\"NO ACTUAL BARCODE\")";
_ls.Add((Object)("NO ACTUAL BARCODE"));
 //BA.debugLineNum = 524;BA.debugLine="ls.Add(\"NO SCANNER\")";
_ls.Add((Object)("NO SCANNER"));
 //BA.debugLineNum = 525;BA.debugLine="ls.Add(\"DAMAGE BARCODE\")";
_ls.Add((Object)("DAMAGE BARCODE"));
 //BA.debugLineNum = 526;BA.debugLine="ls.Add(\"SCANNER CAN'T READ BARCODE\")";
_ls.Add((Object)("SCANNER CAN'T READ BARCODE"));
 //BA.debugLineNum = 527;BA.debugLine="InputListAsync(ls, \"Choose reason\", -1, True) 'sh";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose reason"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 528;BA.debugLine="Wait For InputList_Result (Result2 As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 38;
return;
case 38:
//C
this.state = 1;
_result2 = (Integer) result[0];
;
 //BA.debugLineNum = 529;BA.debugLine="If Result2 <> DialogResponse.CANCEL Then";
if (true) break;

case 1:
//if
this.state = 37;
if (_result2!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 530;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sea";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 531;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 532;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 39;
return;
case 39:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 533;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 4:
//if
this.state = 36;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 535;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM p";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._searchtemplate._selecteditem /*String*/ +"'")));
 //BA.debugLineNum = 536;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 7:
//for
this.state = 35;
step16 = 1;
limit16 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 40;
if (true) break;

case 40:
//C
this.state = 35;
if ((step16 > 0 && _qrow <= limit16) || (step16 < 0 && _qrow >= limit16)) this.state = 9;
if (true) break;

case 41:
//C
this.state = 40;
_qrow = ((int)(0 + _qrow + step16)) ;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 537;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 538;BA.debugLine="LABEL_LOAD_VARIANT.Text = cursor3.GetString(\"p";
parent.mostCurrent._label_load_variant.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_variant")));
 //BA.debugLineNum = 539;BA.debugLine="LABEL_LOAD_DESCRIPTION.Text = cursor3.GetStrin";
parent.mostCurrent._label_load_description.setText(BA.ObjectToCharSequence(parent._cursor3.GetString("product_desc")));
 //BA.debugLineNum = 541;BA.debugLine="CMB_UNIT.cmbBox.Clear";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 542;BA.debugLine="If cursor3.GetString(\"CASE_UNIT_PER_PCS\") > 0";
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
 //BA.debugLineNum = 543;BA.debugLine="CMB_UNIT.cmbBox.Add(\"CASE\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("CASE");
 if (true) break;
;
 //BA.debugLineNum = 545;BA.debugLine="If cursor3.GetString(\"PCS_UNIT_PER_PCS\") > 0 T";

case 13:
//if
this.state = 16;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PCS_UNIT_PER_PCS")))>0) { 
this.state = 15;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 546;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PCS\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PCS");
 if (true) break;
;
 //BA.debugLineNum = 548;BA.debugLine="If cursor3.GetString(\"DOZ_UNIT_PER_PCS\") > 0 T";

case 16:
//if
this.state = 19;
if ((double)(Double.parseDouble(parent._cursor3.GetString("DOZ_UNIT_PER_PCS")))>0) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 549;BA.debugLine="CMB_UNIT.cmbBox.Add(\"DOZ\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("DOZ");
 if (true) break;
;
 //BA.debugLineNum = 551;BA.debugLine="If cursor3.GetString(\"BOX_UNIT_PER_PCS\") > 0 T";

case 19:
//if
this.state = 22;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BOX_UNIT_PER_PCS")))>0) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 552;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BOX\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BOX");
 if (true) break;
;
 //BA.debugLineNum = 554;BA.debugLine="If cursor3.GetString(\"BAG_UNIT_PER_PCS\") > 0 T";

case 22:
//if
this.state = 25;
if ((double)(Double.parseDouble(parent._cursor3.GetString("BAG_UNIT_PER_PCS")))>0) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 555;BA.debugLine="CMB_UNIT.cmbBox.Add(\"BAG\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("BAG");
 if (true) break;
;
 //BA.debugLineNum = 557;BA.debugLine="If cursor3.GetString(\"PACK_UNIT_PER_PCS\") > 0";

case 25:
//if
this.state = 28;
if ((double)(Double.parseDouble(parent._cursor3.GetString("PACK_UNIT_PER_PCS")))>0) { 
this.state = 27;
}if (true) break;

case 27:
//C
this.state = 28;
 //BA.debugLineNum = 558;BA.debugLine="CMB_UNIT.cmbBox.Add(\"PACK\")";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("PACK");
 if (true) break;

case 28:
//C
this.state = 29;
;
 //BA.debugLineNum = 561;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 42;
return;
case 42:
//C
this.state = 29;
;
 //BA.debugLineNum = 562;BA.debugLine="CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBo";
parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setSelectedIndex(parent.mostCurrent._cmb_unit._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("CASE"));
 //BA.debugLineNum = 563;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 43;
return;
case 43:
//C
this.state = 29;
;
 //BA.debugLineNum = 565;BA.debugLine="BOOKING_PCS = Number.Format3((cursor3.GetStrin";
parent._booking_pcs = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("PCS_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 566;BA.debugLine="BOOKING_CASE = Number.Format3((cursor3.GetStri";
parent._booking_case = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("CASE_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 567;BA.debugLine="BOOKING_DOZ = Number.Format3((cursor3.GetStrin";
parent._booking_doz = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("DOZ_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 568;BA.debugLine="BOOKING_BOX = Number.Format3((cursor3.GetStrin";
parent._booking_box = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("BOX_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 569;BA.debugLine="BOOKING_PACK = Number.Format3((cursor3.GetStri";
parent._booking_pack = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("PACK_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 570;BA.debugLine="BOOKING_BAG = Number.Format3((cursor3.GetStrin";
parent._booking_bag = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("BAG_BOOKING")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 571;BA.debugLine="EXTRUCK_PCS = Number.Format3((cursor3.GetStrin";
parent._extruck_pcs = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("PCS_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 572;BA.debugLine="EXTRUCK_CASE = Number.Format3((cursor3.GetStri";
parent._extruck_case = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("CASE_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 573;BA.debugLine="EXTRUCK_DOZ = Number.Format3((cursor3.GetStrin";
parent._extruck_doz = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("DOZ_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 574;BA.debugLine="EXTRUCK_BOX = Number.Format3((cursor3.GetStrin";
parent._extruck_box = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("BOX_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 575;BA.debugLine="EXTRUCK_PACK = Number.Format3((cursor3.GetStri";
parent._extruck_pack = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("PACK_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 576;BA.debugLine="EXTRUCK_BAG = Number.Format3((cursor3.GetStrin";
parent._extruck_bag = parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._cursor3.GetString("BAG_EXTRUCK")))),(int) (0),(int) (2),(int) (2),".","","",(int) (0),(int) (15));
 //BA.debugLineNum = 578;BA.debugLine="If CMB_PRICETYPE.cmbBox.SelectedIndex = CMB_PR";
if (true) break;

case 29:
//if
this.state = 34;
if (parent.mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_pricetype._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("BOOKING")) { 
this.state = 31;
}else {
this.state = 33;
}if (true) break;

case 31:
//C
this.state = 34;
 //BA.debugLineNum = 579;BA.debugLine="LABEL_LOAD_PER_PCS.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_pcs.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_pcs))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 580;BA.debugLine="LABEL_LOAD_PER_CASE.Text = \"₱\"&Number.Format3";
parent.mostCurrent._label_load_per_case.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_case))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 581;BA.debugLine="LABEL_LOAD_PER_DOZ.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_doz.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_doz))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 582;BA.debugLine="LABEL_LOAD_PER_BOX.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_box.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_box))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 583;BA.debugLine="LABEL_LOAD_PER_PACK.Text = \"₱\"&Number.Format3";
parent.mostCurrent._label_load_per_pack.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_pack))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 584;BA.debugLine="LABEL_LOAD_PER_BAG.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_bag.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._booking_bag))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 if (true) break;

case 33:
//C
this.state = 34;
 //BA.debugLineNum = 586;BA.debugLine="LABEL_LOAD_PER_PCS.Text = \"₱\"& Number.Format3";
parent.mostCurrent._label_load_per_pcs.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_pcs))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 587;BA.debugLine="LABEL_LOAD_PER_CASE.Text = \"₱\"&Number.Format3";
parent.mostCurrent._label_load_per_case.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_case))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 588;BA.debugLine="LABEL_LOAD_PER_DOZ.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_doz.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_doz))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 589;BA.debugLine="LABEL_LOAD_PER_BOX.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_box.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_box))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 590;BA.debugLine="LABEL_LOAD_PER_PACK.Text = \"₱\"&Number.Format3";
parent.mostCurrent._label_load_per_pack.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_pack))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 //BA.debugLineNum = 591;BA.debugLine="LABEL_LOAD_PER_BAG.Text = \"₱\"&Number.Format3(";
parent.mostCurrent._label_load_per_bag.setText(BA.ObjectToCharSequence("₱"+parent.mostCurrent._number._format3 /*String*/ (mostCurrent.activityBA,(double)(Double.parseDouble((parent._extruck_bag))),(int) (0),(int) (2),(int) (2),".",",","",(int) (0),(int) (15))));
 if (true) break;

case 34:
//C
this.state = 41;
;
 if (true) break;
if (true) break;

case 35:
//C
this.state = 36;
;
 //BA.debugLineNum = 595;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 44;
return;
case 44:
//C
this.state = 36;
;
 //BA.debugLineNum = 596;BA.debugLine="EDITTEXT_QUANTITY.Text = \"0\"";
parent.mostCurrent._edittext_quantity.setText(BA.ObjectToCharSequence("0"));
 //BA.debugLineNum = 597;BA.debugLine="EDITTEXT_QUANTITY.RequestFocus";
parent.mostCurrent._edittext_quantity.RequestFocus();
 //BA.debugLineNum = 598;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 45;
return;
case 45:
//C
this.state = 36;
;
 //BA.debugLineNum = 599;BA.debugLine="CTRL.ShowKeyboard(EDITTEXT_QUANTITY)";
parent.mostCurrent._ctrl.ShowKeyboard((android.view.View)(parent.mostCurrent._edittext_quantity.getObject()));
 //BA.debugLineNum = 600;BA.debugLine="EDITTEXT_QUANTITY.SelectAll";
parent.mostCurrent._edittext_quantity.SelectAll();
 //BA.debugLineNum = 601;BA.debugLine="FORMULA";
_formula();
 if (true) break;

case 36:
//C
this.state = 37;
;
 if (true) break;

case 37:
//C
this.state = -1;
;
 //BA.debugLineNum = 605;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
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
 //BA.debugLineNum = 21;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 22;BA.debugLine="Dim AStream As AsyncStreams";
_astream = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 23;BA.debugLine="Dim Ts As Timer";
_ts = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 26;BA.debugLine="Dim BOOKING_PCS As String";
_booking_pcs = "";
 //BA.debugLineNum = 27;BA.debugLine="Dim BOOKING_CASE As String";
_booking_case = "";
 //BA.debugLineNum = 28;BA.debugLine="Dim BOOKING_DOZ As String";
_booking_doz = "";
 //BA.debugLineNum = 29;BA.debugLine="Dim BOOKING_BOX As String";
_booking_box = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim BOOKING_PACK As String";
_booking_pack = "";
 //BA.debugLineNum = 31;BA.debugLine="Dim BOOKING_BAG As String";
_booking_bag = "";
 //BA.debugLineNum = 32;BA.debugLine="Dim EXTRUCK_PCS As String";
_extruck_pcs = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim EXTRUCK_CASE As String";
_extruck_case = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim EXTRUCK_DOZ As String";
_extruck_doz = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim EXTRUCK_BOX As String";
_extruck_box = "";
 //BA.debugLineNum = 36;BA.debugLine="Dim EXTRUCK_PACK As String";
_extruck_pack = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim EXTRUCK_BAG As String";
_extruck_bag = "";
 //BA.debugLineNum = 39;BA.debugLine="Dim cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim addBitmap As Bitmap";
_addbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _serial_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 279;BA.debugLine="Sub Serial_Connected (success As Boolean)";
 //BA.debugLineNum = 280;BA.debugLine="If success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 281;BA.debugLine="Log(\"Scanner is now connected. Waiting for data.";
anywheresoftware.b4a.keywords.Common.LogImpl("769206018","Scanner is now connected. Waiting for data...",0);
 //BA.debugLineNum = 282;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoo";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connected.png");
 //BA.debugLineNum = 283;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 284;BA.debugLine="ToastMessageShow(\"Scanner Connected\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner Connected"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 285;BA.debugLine="AStream.Initialize(serial1.InputStream, serial1.";
_astream.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"AStream");
 //BA.debugLineNum = 286;BA.debugLine="ScannerOnceConnected=True";
_scanneronceconnected = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 287;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 289;BA.debugLine="If ScannerOnceConnected=False Then";
if (_scanneronceconnected==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 290;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_notconnected.png");
 //BA.debugLineNum = 291;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 292;BA.debugLine="ToastMessageShow(\"Scanner is off, please turn o";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Scanner is off, please turn on"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 293;BA.debugLine="ShowPairedDevices";
_showpaireddevices();
 }else {
 //BA.debugLineNum = 295;BA.debugLine="Log(\"Still waiting for the scanner to reconnect";
anywheresoftware.b4a.keywords.Common.LogImpl("769206032","Still waiting for the scanner to reconnect: "+mostCurrent._scannermacaddress,0);
 //BA.debugLineNum = 296;BA.debugLine="Ts.Enabled=True";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 297;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 298;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 };
 };
 //BA.debugLineNum = 301;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 183;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 184;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 185;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 186;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 187;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 188;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 189;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 190;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 191;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 192;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 193;BA.debugLine="End Sub";
return "";
}
public static void  _showpaireddevices() throws Exception{
ResumableSub_ShowPairedDevices rsub = new ResumableSub_ShowPairedDevices(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_ShowPairedDevices extends BA.ResumableSub {
public ResumableSub_ShowPairedDevices(wingan.app.price_checking_module parent) {
this.parent = parent;
}
wingan.app.price_checking_module parent;
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
 //BA.debugLineNum = 252;BA.debugLine="Dim mac As String";
_mac = "";
 //BA.debugLineNum = 253;BA.debugLine="Dim PairedDevices As Map";
_paireddevices = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 254;BA.debugLine="PairedDevices = serial1.GetPairedDevices";
_paireddevices = parent._serial1.GetPairedDevices();
 //BA.debugLineNum = 255;BA.debugLine="Dim ls As List";
_ls = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 256;BA.debugLine="ls.Initialize";
_ls.Initialize();
 //BA.debugLineNum = 257;BA.debugLine="For Iq = 0 To PairedDevices.Size - 1";
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
 //BA.debugLineNum = 258;BA.debugLine="mac = PairedDevices.Get(PairedDevices.GetKeyAt(I";
_mac = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_iq)));
 //BA.debugLineNum = 259;BA.debugLine="ls.add(\"Scanner \" & mac.SubString2 (mac.Length -";
_ls.Add((Object)("Scanner "+_mac.substring((int) (_mac.length()-4),_mac.length())));
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 261;BA.debugLine="If ls.Size=0 Then";

case 4:
//if
this.state = 7;
if (_ls.getSize()==0) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 262;BA.debugLine="ls.Add(\"No device(s) found...\")";
_ls.Add((Object)("No device(s) found..."));
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 264;BA.debugLine="InputListAsync(ls, \"Choose scanner\", -1, True) 's";
anywheresoftware.b4a.keywords.Common.InputListAsync(_ls,BA.ObjectToCharSequence("Choose scanner"),(int) (-1),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 265;BA.debugLine="Wait For InputList_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("inputlist_result", processBA, this, null);
this.state = 20;
return;
case 20:
//C
this.state = 8;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 266;BA.debugLine="If Result <> DialogResponse.CANCEL Then";
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
 //BA.debugLineNum = 267;BA.debugLine="If ls.Get(Result)=\"No device(s) found...\" Then";
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
 //BA.debugLineNum = 268;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 270;BA.debugLine="ScannerMacAddress=PairedDevices.Get(PairedDevic";
parent.mostCurrent._scannermacaddress = BA.ObjectToString(_paireddevices.Get(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))));
 //BA.debugLineNum = 272;BA.debugLine="Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (R";
anywheresoftware.b4a.keywords.Common.LogImpl("769140501",BA.ObjectToString(_paireddevices.GetKeyAt(_ls.IndexOf(_ls.Get(_result)))),0);
 //BA.debugLineNum = 273;BA.debugLine="serial1.Connect(ScannerMacAddress)";
parent._serial1.Connect(processBA,parent.mostCurrent._scannermacaddress);
 //BA.debugLineNum = 274;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"blueto";
parent._cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 275;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
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
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _timer_tick() throws Exception{
 //BA.debugLineNum = 435;BA.debugLine="Sub Timer_Tick";
 //BA.debugLineNum = 436;BA.debugLine="Ts.Enabled=False";
_ts.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 437;BA.debugLine="serial1.Connect(ScannerMacAddress)";
_serial1.Connect(processBA,mostCurrent._scannermacaddress);
 //BA.debugLineNum = 438;BA.debugLine="Log (\"Trying to connect...\")";
anywheresoftware.b4a.keywords.Common.LogImpl("769468163","Trying to connect...",0);
 //BA.debugLineNum = 439;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"bluetoot";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bluetooth_connecting.png");
 //BA.debugLineNum = 440;BA.debugLine="UpdateIcon(\"cart\", cartBitmap)";
_updateicon("cart",_cartbitmap);
 //BA.debugLineNum = 441;BA.debugLine="End Sub";
return "";
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 204;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 205;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 206;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
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
