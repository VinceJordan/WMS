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

public class daily_inventory_module extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static daily_inventory_module mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.daily_inventory_module");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (daily_inventory_module).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.daily_inventory_module");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.daily_inventory_module", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (daily_inventory_module) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (daily_inventory_module) Resume **");
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
		return daily_inventory_module.class;
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
            BA.LogInfo("** Activity (daily_inventory_module) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (daily_inventory_module) Pause event (activity is not paused). **");
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
            daily_inventory_module mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (daily_inventory_module) Resume **");
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
public static String _group_id = "";
public static String _group_id_new = "";
public static String _clear_trigger = "";
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _cartbitmap = null;
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _downbitmap = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xsearchtemplate _searchtemplate = null;
public wingan.app.b4xsearchtemplate _searchtemplate2 = null;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_group = null;
public wingan.app.b4xcombobox _cmb_create_year = null;
public wingan.app.b4xcombobox _cmb_create_month = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_create = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_create = null;
public wingan.app.b4xcombobox _cmb_group = null;
public wingan.app.b4xcombobox _cmb_year = null;
public wingan.app.b4xcombobox _cmb_month = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_save = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_daily_count = null;
public wingan.app.b4xtable _table_inventory_date = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_view = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_edit = null;
public wingan.app.b4xcombobox _cmb_tabid = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_download = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview_templates = null;
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
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4j.object.JavaObject _jo = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 78;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 80;BA.debugLine="Activity.LoadLayout(\"daily\")";
mostCurrent._activity.LoadLayout("daily",mostCurrent.activityBA);
 //BA.debugLineNum = 82;BA.debugLine="cartBitmap = LoadBitmap(File.DirAssets, \"add.png\"";
_cartbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"add.png");
 //BA.debugLineNum = 83;BA.debugLine="downBitmap = LoadBitmap(File.DirAssets, \"download";
_downbitmap = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"download.png");
 //BA.debugLineNum = 85;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 86;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 87;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 88;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 89;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 90;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 91;BA.debugLine="ACToolBarLight1.InitMenuListener";
mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 93;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 94;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 95;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 97;BA.debugLine="If connection.IsInitialized = False Then";
if (_connection.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 98;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
_connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 101;BA.debugLine="Base = Activity";
mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject()));
 //BA.debugLineNum = 102;BA.debugLine="Dialog.VisibleAnimationDuration = 300";
mostCurrent._dialog._visibleanimationduration /*int*/  = (int) (300);
 //BA.debugLineNum = 103;BA.debugLine="Dialog.Initialize(Base)";
mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,mostCurrent._base);
 //BA.debugLineNum = 104;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 105;BA.debugLine="Dialog.BorderCornersRadius = 5";
mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 106;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 107;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 108;BA.debugLine="Dialog.BackgroundColor = Colors.White";
mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 109;BA.debugLine="Dialog.ButtonsColor = Colors.White";
mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 110;BA.debugLine="Dialog.ButtonsTextColor = Colors.Black";
mostCurrent._dialog._buttonstextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 111;BA.debugLine="Dialog.PutAtTop = True";
mostCurrent._dialog._putattop /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 113;BA.debugLine="SearchTemplate.Initialize";
mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 114;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 115;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 116;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 117;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 118;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 121;BA.debugLine="SearchTemplate2.Initialize";
mostCurrent._searchtemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 122;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextBackgr";
mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 123;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextColor";
mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 124;BA.debugLine="SearchTemplate2.SearchField.TextField.TextColor =";
mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 125;BA.debugLine="SearchTemplate2.ItemHightlightColor = Colors.Whit";
mostCurrent._searchtemplate2._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 126;BA.debugLine="SearchTemplate2.TextHighlightColor = Colors.RGB(8";
mostCurrent._searchtemplate2._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 128;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 129;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 130;BA.debugLine="cvs.Initialize(p)";
mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 132;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 133;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 135;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 136;BA.debugLine="bg.Initialize2(Colors.RGB(0,142,255), 5, 0, Color";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (142),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 137;BA.debugLine="BUTTON_VIEW.Background = bg";
mostCurrent._button_view.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 138;BA.debugLine="BUTTON_VIEW.Text = \" View\"";
mostCurrent._button_view.setText(BA.ObjectToCharSequence(" View"));
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _activity_createmenu(de.amberhome.objects.appcompat.ACMenuWrapper _menu) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _item = null;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item1 = null;
 //BA.debugLineNum = 140;BA.debugLine="Sub Activity_CreateMenu(Menu As ACMenu)";
 //BA.debugLineNum = 141;BA.debugLine="Dim item As ACMenuItem = ACToolBarLight1.Menu.Add";
_item = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item = mostCurrent._actoolbarlight1.getMenu().Add2((int) (0),(int) (0),BA.ObjectToCharSequence("Create New Template"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 142;BA.debugLine="item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS";
_item.setShowAsAction(_item.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 143;BA.debugLine="UpdateIcon(\"Create New Template\", cartBitmap)";
_updateicon("Create New Template",_cartbitmap);
 //BA.debugLineNum = 145;BA.debugLine="Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Ad";
_item1 = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_item1 = mostCurrent._actoolbarlight1.getMenu().Add2((int) (1),(int) (1),BA.ObjectToCharSequence("Download Template"),(android.graphics.drawable.Drawable)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 146;BA.debugLine="item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS";
_item1.setShowAsAction(_item1.SHOW_AS_ACTION_ALWAYS);
 //BA.debugLineNum = 147;BA.debugLine="UpdateIcon(\"Download Template\", downBitmap)";
_updateicon("Download Template",_downbitmap);
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 163;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 151;BA.debugLine="If clear_trigger = 1 Then";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent._clear_trigger).equals(BA.NumberToString(1))) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 152;BA.debugLine="LOAD_GROUP";
_load_group();
 //BA.debugLineNum = 153;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 154;BA.debugLine="CMB_GROUP.SelectedIndex = -1";
parent.mostCurrent._cmb_group._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 155;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 156;BA.debugLine="OpenSpinner(CMB_GROUP.cmbBox)";
_openspinner(parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 157;BA.debugLine="ENABLE_VIEW";
_enable_view();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 159;BA.debugLine="LOAD_INVENTORY_DATE";
_load_inventory_date();
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
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
public ResumableSub_ACToolBarLight1_MenuItemClick(wingan.app.daily_inventory_module parent,de.amberhome.objects.appcompat.ACMenuItemWrapper _item) {
this.parent = parent;
this._item = _item;
}
wingan.app.daily_inventory_module parent;
de.amberhome.objects.appcompat.ACMenuItemWrapper _item;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 215;BA.debugLine="If Item.Title = \"Create New Template\" Then";
if (true) break;

case 1:
//if
this.state = 8;
if ((_item.getTitle()).equals("Create New Template")) { 
this.state = 3;
}else if((_item.getTitle()).equals("Download Template")) { 
this.state = 5;
}else {
this.state = 7;
}if (true) break;

case 3:
//C
this.state = 8;
 //BA.debugLineNum = 216;BA.debugLine="ENABLE_VIEW";
_enable_view();
 //BA.debugLineNum = 217;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 8;
;
 //BA.debugLineNum = 218;BA.debugLine="PANEL_BG_CREATE.BringToFront";
parent.mostCurrent._panel_bg_create.BringToFront();
 //BA.debugLineNum = 219;BA.debugLine="PANEL_BG_CREATE.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_create.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 220;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 10;
return;
case 10:
//C
this.state = 8;
;
 //BA.debugLineNum = 221;BA.debugLine="OpenLabel(LABEL_LOAD_GROUP)";
_openlabel(parent.mostCurrent._label_load_group);
 //BA.debugLineNum = 222;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 11;
return;
case 11:
//C
this.state = 8;
;
 //BA.debugLineNum = 223;BA.debugLine="LOAD_CREATE_YEAR";
_load_create_year();
 //BA.debugLineNum = 224;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 8;
;
 //BA.debugLineNum = 225;BA.debugLine="LOAD_CREATE_MONTH";
_load_create_month();
 if (true) break;

case 5:
//C
this.state = 8;
 //BA.debugLineNum = 227;BA.debugLine="PANEL_BG_DOWNLOAD.SetVisibleAnimated(300,True)";
parent.mostCurrent._panel_bg_download.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 228;BA.debugLine="PANEL_BG_DOWNLOAD.BringToFront";
parent.mostCurrent._panel_bg_download.BringToFront();
 //BA.debugLineNum = 229;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 8;
;
 //BA.debugLineNum = 230;BA.debugLine="GET_TABID";
_get_tabid();
 if (true) break;

case 7:
//C
this.state = 8;
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 234;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 195;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 196;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 197;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 //BA.debugLineNum = 198;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 199;BA.debugLine="clear_trigger = 1";
_clear_trigger = BA.NumberToString(1);
 //BA.debugLineNum = 200;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 190;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 191;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 192;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 193;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return null;
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 453;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 454;BA.debugLine="PANEL_BG_CREATE.SetVisibleAnimated(300, False)";
mostCurrent._panel_bg_create.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 455;BA.debugLine="LABEL_LOAD_GROUP.Text = \"Click me to  add new gro";
mostCurrent._label_load_group.setText(BA.ObjectToCharSequence("Click me to  add new group."));
 //BA.debugLineNum = 456;BA.debugLine="End Sub";
return "";
}
public static void  _button_create_click() throws Exception{
ResumableSub_BUTTON_CREATE_Click rsub = new ResumableSub_BUTTON_CREATE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_CREATE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_CREATE_Click(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 294;BA.debugLine="PANEL_BG_CREATE.BringToFront";
parent.mostCurrent._panel_bg_create.BringToFront();
 //BA.debugLineNum = 295;BA.debugLine="PANEL_BG_CREATE.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_create.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 296;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 297;BA.debugLine="OpenLabel(LABEL_LOAD_GROUP)";
_openlabel(parent.mostCurrent._label_load_group);
 //BA.debugLineNum = 298;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 299;BA.debugLine="LOAD_CREATE_YEAR";
_load_create_year();
 //BA.debugLineNum = 300;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 3;
return;
case 3:
//C
this.state = -1;
;
 //BA.debugLineNum = 301;BA.debugLine="LOAD_CREATE_MONTH";
_load_create_month();
 //BA.debugLineNum = 302;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_daily_count_click() throws Exception{
 //BA.debugLineNum = 542;BA.debugLine="Sub BUTTON_DAILY_COUNT_Click";
 //BA.debugLineNum = 543;BA.debugLine="StartActivity(DAILY_COUNT)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._daily_count.getObject()));
 //BA.debugLineNum = 544;BA.debugLine="DAILY_COUNT.inventory_date = DateTime.Date(DateTi";
mostCurrent._daily_count._inventory_date /*String*/  = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 545;BA.debugLine="clear_trigger = 0";
_clear_trigger = BA.NumberToString(0);
 //BA.debugLineNum = 546;BA.debugLine="End Sub";
return "";
}
public static String  _button_download_exit_click() throws Exception{
 //BA.debugLineNum = 847;BA.debugLine="Sub BUTTON_DOWNLOAD_EXIT_Click";
 //BA.debugLineNum = 848;BA.debugLine="PANEL_BG_DOWNLOAD.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_download.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 849;BA.debugLine="LISTVIEW_TEMPLATES.Clear";
mostCurrent._listview_templates.Clear();
 //BA.debugLineNum = 850;BA.debugLine="End Sub";
return "";
}
public static void  _button_edit_click() throws Exception{
ResumableSub_BUTTON_EDIT_Click rsub = new ResumableSub_BUTTON_EDIT_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_EDIT_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_EDIT_Click(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
String _query = "";
int _i = 0;
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
 //BA.debugLineNum = 459;BA.debugLine="If CMB_YEAR.cmbBox.SelectedIndex = CMB_YEAR.cmbBo";
if (true) break;

case 1:
//if
this.state = 34;
if (parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("") || parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 34;
 //BA.debugLineNum = 460;BA.debugLine="Msgbox2Async(\"Please choose year and month. Cann";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please choose year and month. Cannot proceed."),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 462;BA.debugLine="If CMB_GROUP.SelectedIndex = -1 Then CMB_GROUP.S";
if (true) break;

case 6:
//if
this.state = 11;
if (parent.mostCurrent._cmb_group._getselectedindex /*int*/ ()==-1) { 
this.state = 8;
;}if (true) break;

case 8:
//C
this.state = 11;
parent.mostCurrent._cmb_group._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 11:
//C
this.state = 12;
;
 //BA.debugLineNum = 463;BA.debugLine="If CMB_YEAR.SelectedIndex = -1 Then CMB_YEAR.Sel";
if (true) break;

case 12:
//if
this.state = 17;
if (parent.mostCurrent._cmb_year._getselectedindex /*int*/ ()==-1) { 
this.state = 14;
;}if (true) break;

case 14:
//C
this.state = 17;
parent.mostCurrent._cmb_year._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 464;BA.debugLine="If CMB_MONTH.SelectedIndex = -1 Then CMB_MONTH.S";
if (true) break;

case 18:
//if
this.state = 23;
if (parent.mostCurrent._cmb_month._getselectedindex /*int*/ ()==-1) { 
this.state = 20;
;}if (true) break;

case 20:
//C
this.state = 23;
parent.mostCurrent._cmb_month._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 23:
//C
this.state = 24;
;
 //BA.debugLineNum = 466;BA.debugLine="Dim query As String = \"SELECT * FROM daily_templ";
_query = "SELECT * FROM daily_template_table WHERE group_name = ? and year = ? and month = ?";
 //BA.debugLineNum = 467;BA.debugLine="cursor5 = connection.ExecQuery2(query,Array As S";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery2(_query,new String[]{parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()})));
 //BA.debugLineNum = 468;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 24:
//if
this.state = 33;
if (parent._cursor5.getRowCount()>0) { 
this.state = 26;
}else {
this.state = 32;
}if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 469;BA.debugLine="For i = 0 To cursor5.RowCount - 1";
if (true) break;

case 27:
//for
this.state = 30;
step10 = 1;
limit10 = (int) (parent._cursor5.getRowCount()-1);
_i = (int) (0) ;
this.state = 35;
if (true) break;

case 35:
//C
this.state = 30;
if ((step10 > 0 && _i <= limit10) || (step10 < 0 && _i >= limit10)) this.state = 29;
if (true) break;

case 36:
//C
this.state = 35;
_i = ((int)(0 + _i + step10)) ;
if (true) break;

case 29:
//C
this.state = 36;
 //BA.debugLineNum = 470;BA.debugLine="cursor5.Position = i";
parent._cursor5.setPosition(_i);
 //BA.debugLineNum = 471;BA.debugLine="group_id = cursor5.GetString(\"group_id\")";
parent._group_id = parent._cursor5.GetString("group_id");
 if (true) break;
if (true) break;

case 30:
//C
this.state = 33;
;
 //BA.debugLineNum = 473;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 37;
return;
case 37:
//C
this.state = 33;
;
 //BA.debugLineNum = 474;BA.debugLine="StartActivity(DAILY_TEMPLATE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._daily_template.getObject()));
 //BA.debugLineNum = 475;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 476;BA.debugLine="clear_trigger = 0";
parent._clear_trigger = BA.NumberToString(0);
 if (true) break;

case 32:
//C
this.state = 33;
 if (true) break;

case 33:
//C
this.state = 34;
;
 if (true) break;

case 34:
//C
this.state = -1;
;
 //BA.debugLineNum = 481;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _button_find_click() throws Exception{
 //BA.debugLineNum = 852;BA.debugLine="Sub BUTTON_FIND_Click";
 //BA.debugLineNum = 853;BA.debugLine="GET_TEMPLATES";
_get_templates();
 //BA.debugLineNum = 854;BA.debugLine="End Sub";
return "";
}
public static void  _button_save_click() throws Exception{
ResumableSub_BUTTON_SAVE_Click rsub = new ResumableSub_BUTTON_SAVE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_SAVE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_SAVE_Click(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
int _result = 0;
String _query = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 425;BA.debugLine="If LABEL_LOAD_GROUP.Text.Length <= 2 Then";
if (true) break;

case 1:
//if
this.state = 18;
if (parent.mostCurrent._label_load_group.getText().length()<=2) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 18;
 //BA.debugLineNum = 426;BA.debugLine="Msgbox2Async(\"Group name must be 3 or more chara";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Group name must be 3 or more characters or letters. Cannot proceed."),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 428;BA.debugLine="Msgbox2Async(\"Are you sure you want to create thi";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to create this template?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 429;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 19;
return;
case 19:
//C
this.state = 6;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 430;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 6:
//if
this.state = 17;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 8;
}else {
this.state = 16;
}if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 431;BA.debugLine="CREATE_GROUP_ID";
_create_group_id();
 //BA.debugLineNum = 432;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 9;
;
 //BA.debugLineNum = 433;BA.debugLine="Dim query As String = \"SELECT * FROM daily_templ";
_query = "SELECT * FROM daily_template_table WHERE group_name = ? and year = ? and month = ?";
 //BA.debugLineNum = 434;BA.debugLine="cursor3 = connection.ExecQuery2(query,Array As S";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery2(_query,new String[]{parent.mostCurrent._label_load_group.getText(),parent.mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()})));
 //BA.debugLineNum = 435;BA.debugLine="If cursor3.RowCount > 0 Then";
if (true) break;

case 9:
//if
this.state = 14;
if (parent._cursor3.getRowCount()>0) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 436;BA.debugLine="Msgbox2Async(\"The template you creating is alr";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The template you creating is already exisiting in the system. Cannot proceed."),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 438;BA.debugLine="Dim query As String = \"INSERT INTO daily_templa";
_query = "INSERT INTO daily_template_table VALUES (?,?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 439;BA.debugLine="connection.ExecNonQuery2(query,Array As String(";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent._group_id,parent.mostCurrent._label_load_group.getText(),parent.mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),BA.NumberToString(0),"-","-",parent.mostCurrent._login_module._username /*String*/ ,parent.mostCurrent._login_module._tab_id /*String*/ }));
 //BA.debugLineNum = 440;BA.debugLine="ToastMessageShow(\"Template created\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Template created"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 441;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 14;
;
 //BA.debugLineNum = 442;BA.debugLine="PANEL_BG_CREATE.SetVisibleAnimated(300, False)";
parent.mostCurrent._panel_bg_create.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 443;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 22;
return;
case 22:
//C
this.state = 14;
;
 //BA.debugLineNum = 444;BA.debugLine="StartActivity(DAILY_TEMPLATE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._daily_template.getObject()));
 //BA.debugLineNum = 445;BA.debugLine="SetAnimation(\"right_to_center\", \"center_to_left";
_setanimation("right_to_center","center_to_left");
 //BA.debugLineNum = 446;BA.debugLine="clear_trigger = 1";
parent._clear_trigger = BA.NumberToString(1);
 if (true) break;

case 14:
//C
this.state = 17;
;
 if (true) break;

case 16:
//C
this.state = 17;
 if (true) break;

case 17:
//C
this.state = 18;
;
 if (true) break;

case 18:
//C
this.state = -1;
;
 //BA.debugLineNum = 452;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static void  _button_view_click() throws Exception{
ResumableSub_BUTTON_VIEW_Click rsub = new ResumableSub_BUTTON_VIEW_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_VIEW_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_VIEW_Click(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
String _query = "";
int _i = 0;
int step11;
int limit11;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 509;BA.debugLine="If BUTTON_VIEW.Text = \" View\" Then";
if (true) break;

case 1:
//if
this.state = 40;
if ((parent.mostCurrent._button_view.getText()).equals(" View")) { 
this.state = 3;
}else {
this.state = 39;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 510;BA.debugLine="If CMB_YEAR.cmbBox.SelectedIndex = CMB_YEAR.cmbB";
if (true) break;

case 4:
//if
this.state = 37;
if (parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("") || parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedIndex()==parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 37;
 //BA.debugLineNum = 511;BA.debugLine="Msgbox2Async(\"Please choose year and month. Can";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please choose year and month. Cannot proceed."),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 513;BA.debugLine="If CMB_GROUP.SelectedIndex = -1 Then CMB_GROUP.";
if (true) break;

case 9:
//if
this.state = 14;
if (parent.mostCurrent._cmb_group._getselectedindex /*int*/ ()==-1) { 
this.state = 11;
;}if (true) break;

case 11:
//C
this.state = 14;
parent.mostCurrent._cmb_group._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 514;BA.debugLine="If CMB_YEAR.SelectedIndex = -1 Then CMB_YEAR.Se";
if (true) break;

case 15:
//if
this.state = 20;
if (parent.mostCurrent._cmb_year._getselectedindex /*int*/ ()==-1) { 
this.state = 17;
;}if (true) break;

case 17:
//C
this.state = 20;
parent.mostCurrent._cmb_year._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 20:
//C
this.state = 21;
;
 //BA.debugLineNum = 515;BA.debugLine="If CMB_MONTH.SelectedIndex = -1 Then CMB_MONTH.";
if (true) break;

case 21:
//if
this.state = 26;
if (parent.mostCurrent._cmb_month._getselectedindex /*int*/ ()==-1) { 
this.state = 23;
;}if (true) break;

case 23:
//C
this.state = 26;
parent.mostCurrent._cmb_month._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 26:
//C
this.state = 27;
;
 //BA.debugLineNum = 517;BA.debugLine="Dim query As String = \"SELECT * FROM daily_temp";
_query = "SELECT * FROM daily_template_table WHERE group_name = ? and year = ? and month = ?";
 //BA.debugLineNum = 518;BA.debugLine="cursor5 = connection.ExecQuery2(query,Array As";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery2(_query,new String[]{parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem(),parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()})));
 //BA.debugLineNum = 519;BA.debugLine="If cursor5.RowCount > 0 Then";
if (true) break;

case 27:
//if
this.state = 36;
if (parent._cursor5.getRowCount()>0) { 
this.state = 29;
}else {
this.state = 35;
}if (true) break;

case 29:
//C
this.state = 30;
 //BA.debugLineNum = 520;BA.debugLine="For i = 0 To cursor5.RowCount - 1";
if (true) break;

case 30:
//for
this.state = 33;
step11 = 1;
limit11 = (int) (parent._cursor5.getRowCount()-1);
_i = (int) (0) ;
this.state = 41;
if (true) break;

case 41:
//C
this.state = 33;
if ((step11 > 0 && _i <= limit11) || (step11 < 0 && _i >= limit11)) this.state = 32;
if (true) break;

case 42:
//C
this.state = 41;
_i = ((int)(0 + _i + step11)) ;
if (true) break;

case 32:
//C
this.state = 42;
 //BA.debugLineNum = 521;BA.debugLine="cursor5.Position = i";
parent._cursor5.setPosition(_i);
 //BA.debugLineNum = 522;BA.debugLine="group_id = cursor5.GetString(\"group_id\")";
parent._group_id = parent._cursor5.GetString("group_id");
 if (true) break;
if (true) break;

case 33:
//C
this.state = 36;
;
 //BA.debugLineNum = 524;BA.debugLine="LOAD_TABLE_HEADER";
_load_table_header();
 //BA.debugLineNum = 525;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 43;
return;
case 43:
//C
this.state = 36;
;
 //BA.debugLineNum = 526;BA.debugLine="LOAD_INVENTORY_DATE";
_load_inventory_date();
 //BA.debugLineNum = 527;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 44;
return;
case 44:
//C
this.state = 36;
;
 //BA.debugLineNum = 528;BA.debugLine="DISABLE_VIEW";
_disable_view();
 if (true) break;

case 35:
//C
this.state = 36;
 if (true) break;

case 36:
//C
this.state = 37;
;
 if (true) break;

case 37:
//C
this.state = 40;
;
 if (true) break;

case 39:
//C
this.state = 40;
 //BA.debugLineNum = 535;BA.debugLine="TABLE_INVENTORY_DATE.Clear";
parent.mostCurrent._table_inventory_date._clear /*void*/ ();
 //BA.debugLineNum = 536;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 45;
return;
case 45:
//C
this.state = 40;
;
 //BA.debugLineNum = 537;BA.debugLine="ENABLE_VIEW";
_enable_view();
 if (true) break;

case 40:
//C
this.state = -1;
;
 //BA.debugLineNum = 540;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_create_year_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_CREATE_YEAR_SelectedIndexChanged rsub = new ResumableSub_CMB_CREATE_YEAR_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_CREATE_YEAR_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_CREATE_YEAR_SelectedIndexChanged(wingan.app.daily_inventory_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.daily_inventory_module parent;
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
 //BA.debugLineNum = 365;BA.debugLine="OpenSpinner(CMB_CREATE_MONTH.cmbBox)";
_openspinner(parent.mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 366;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 367;BA.debugLine="CMB_CREATE_MONTH.SelectedIndex = - 1";
parent.mostCurrent._cmb_create_month._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 368;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_group_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_GROUP_SelectedIndexChanged rsub = new ResumableSub_CMB_GROUP_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_GROUP_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_GROUP_SelectedIndexChanged(wingan.app.daily_inventory_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.daily_inventory_module parent;
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
 //BA.debugLineNum = 252;BA.debugLine="LOAD_YEAR";
_load_year();
 //BA.debugLineNum = 253;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 254;BA.debugLine="CMB_YEAR.SelectedIndex = -1";
parent.mostCurrent._cmb_year._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 255;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 256;BA.debugLine="OpenSpinner(CMB_YEAR.cmbBox)";
_openspinner(parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 257;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _cmb_month_selectedindexchanged(int _index) throws Exception{
 //BA.debugLineNum = 386;BA.debugLine="Sub CMB_MONTH_SelectedIndexChanged (Index As Int)";
 //BA.debugLineNum = 388;BA.debugLine="End Sub";
return "";
}
public static void  _cmb_year_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_YEAR_SelectedIndexChanged rsub = new ResumableSub_CMB_YEAR_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_YEAR_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_YEAR_SelectedIndexChanged(wingan.app.daily_inventory_module parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.daily_inventory_module parent;
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
 //BA.debugLineNum = 273;BA.debugLine="LOAD_MONTH";
_load_month();
 //BA.debugLineNum = 274;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 275;BA.debugLine="CMB_MONTH.SelectedIndex = -1";
parent.mostCurrent._cmb_month._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 276;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 277;BA.debugLine="OpenSpinner(CMB_MONTH.cmbBox)";
_openspinner(parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 278;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _create_group_id() throws Exception{
String _month_num = "";
 //BA.debugLineNum = 389;BA.debugLine="Sub CREATE_GROUP_ID";
 //BA.debugLineNum = 390;BA.debugLine="Dim month_num As String";
_month_num = "";
 //BA.debugLineNum = 392;BA.debugLine="If CMB_CREATE_MONTH.SelectedIndex = -1 Then CMB_C";
if (mostCurrent._cmb_create_month._getselectedindex /*int*/ ()==-1) { 
mostCurrent._cmb_create_month._setselectedindex /*int*/ ((int) (0));};
 //BA.debugLineNum = 394;BA.debugLine="If CMB_CREATE_YEAR.SelectedIndex = -1 Then CMB_CR";
if (mostCurrent._cmb_create_year._getselectedindex /*int*/ ()==-1) { 
mostCurrent._cmb_create_year._setselectedindex /*int*/ ((int) (0));};
 //BA.debugLineNum = 396;BA.debugLine="If CMB_CREATE_MONTH.cmbBox.SelectedItem = \"Januar";
if ((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("January")) { 
 //BA.debugLineNum = 397;BA.debugLine="month_num = \"01\"";
_month_num = "01";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("February")) { 
 //BA.debugLineNum = 399;BA.debugLine="month_num = \"02\"";
_month_num = "02";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("March")) { 
 //BA.debugLineNum = 401;BA.debugLine="month_num = \"03\"";
_month_num = "03";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("April")) { 
 //BA.debugLineNum = 403;BA.debugLine="month_num = \"04\"";
_month_num = "04";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("May")) { 
 //BA.debugLineNum = 405;BA.debugLine="month_num = \"05\"";
_month_num = "05";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("June")) { 
 //BA.debugLineNum = 407;BA.debugLine="month_num = \"06\"";
_month_num = "06";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("July")) { 
 //BA.debugLineNum = 409;BA.debugLine="month_num = \"07\"";
_month_num = "07";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("August")) { 
 //BA.debugLineNum = 411;BA.debugLine="month_num = \"08\"";
_month_num = "08";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("September")) { 
 //BA.debugLineNum = 413;BA.debugLine="month_num = \"09\"";
_month_num = "09";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("October")) { 
 //BA.debugLineNum = 415;BA.debugLine="month_num = \"10\"";
_month_num = "10";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("November")) { 
 //BA.debugLineNum = 417;BA.debugLine="month_num = \"11\"";
_month_num = "11";
 }else if((mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()).equals("December")) { 
 //BA.debugLineNum = 419;BA.debugLine="month_num = \"12\"";
_month_num = "12";
 };
 //BA.debugLineNum = 422;BA.debugLine="group_id = LABEL_LOAD_GROUP.Text&CMB_CREATE_YEAR.";
_group_id = mostCurrent._label_load_group.getText()+mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+_month_num+mostCurrent._login_module._tab_id /*String*/ ;
 //BA.debugLineNum = 423;BA.debugLine="End Sub";
return "";
}
public static wingan.app.main._dbcommand  _createcommand(String _name,Object[] _parameters) throws Exception{
wingan.app.main._dbcommand _cmd = null;
 //BA.debugLineNum = 650;BA.debugLine="Sub CreateCommand(Name As String, Parameters() As";
 //BA.debugLineNum = 651;BA.debugLine="Dim cmd As DBCommand";
_cmd = new wingan.app.main._dbcommand();
 //BA.debugLineNum = 652;BA.debugLine="cmd.Initialize";
_cmd.Initialize();
 //BA.debugLineNum = 653;BA.debugLine="cmd.Name = Name";
_cmd.Name /*String*/  = _name;
 //BA.debugLineNum = 654;BA.debugLine="If Parameters <> Null Then cmd.Parameters = Param";
if (_parameters!= null) { 
_cmd.Parameters /*Object[]*/  = _parameters;};
 //BA.debugLineNum = 655;BA.debugLine="Return cmd";
if (true) return _cmd;
 //BA.debugLineNum = 656;BA.debugLine="End Sub";
return null;
}
public static wingan.app.dbrequestmanager  _createrequest() throws Exception{
wingan.app.dbrequestmanager _req = null;
 //BA.debugLineNum = 645;BA.debugLine="Sub CreateRequest As DBRequestManager";
 //BA.debugLineNum = 646;BA.debugLine="Dim req As DBRequestManager";
_req = new wingan.app.dbrequestmanager();
 //BA.debugLineNum = 647;BA.debugLine="req.Initialize(Me, Main.rdclink)";
_req._initialize /*String*/ (processBA,daily_inventory_module.getObject(),mostCurrent._main._rdclink /*String*/ );
 //BA.debugLineNum = 648;BA.debugLine="Return req";
if (true) return _req;
 //BA.debugLineNum = 649;BA.debugLine="End Sub";
return null;
}
public static void  _disable_view() throws Exception{
ResumableSub_DISABLE_VIEW rsub = new ResumableSub_DISABLE_VIEW(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_DISABLE_VIEW extends BA.ResumableSub {
public ResumableSub_DISABLE_VIEW(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 496;BA.debugLine="BUTTON_DAILY_COUNT.Visible = True";
parent.mostCurrent._button_daily_count.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 497;BA.debugLine="BUTTON_EDIT.Visible = True";
parent.mostCurrent._button_edit.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 498;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 499;BA.debugLine="CMB_GROUP.cmbBox.Enabled = False";
parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 500;BA.debugLine="CMB_YEAR.cmbBox.Enabled = False";
parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 501;BA.debugLine="CMB_MONTH.cmbBox.Enabled = False";
parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 502;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 503;BA.debugLine="bg.Initialize2(Colors.Red, 5, 0, Colors.LightGray";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.Red,(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 504;BA.debugLine="BUTTON_VIEW.Background = bg";
parent.mostCurrent._button_view.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 505;BA.debugLine="BUTTON_VIEW.Text = \" Close\"";
parent.mostCurrent._button_view.setText(BA.ObjectToCharSequence(" Close"));
 //BA.debugLineNum = 506;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _enable_view() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
 //BA.debugLineNum = 483;BA.debugLine="Sub ENABLE_VIEW";
 //BA.debugLineNum = 484;BA.debugLine="BUTTON_EDIT.Visible = False";
mostCurrent._button_edit.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 485;BA.debugLine="BUTTON_DAILY_COUNT.Visible = False";
mostCurrent._button_daily_count.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 486;BA.debugLine="CMB_GROUP.cmbBox.Enabled = True";
mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 487;BA.debugLine="CMB_YEAR.cmbBox.Enabled = True";
mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 488;BA.debugLine="CMB_MONTH.cmbBox.Enabled = True";
mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 489;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 490;BA.debugLine="bg.Initialize2(Colors.RGB(0,142,255), 5, 0, Color";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (0),(int) (142),(int) (255)),(int) (5),(int) (0),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 491;BA.debugLine="BUTTON_VIEW.Background = bg";
mostCurrent._button_view.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 492;BA.debugLine="BUTTON_VIEW.Text = \" View\"";
mostCurrent._button_view.setText(BA.ObjectToCharSequence(" View"));
 //BA.debugLineNum = 493;BA.debugLine="End Sub";
return "";
}
public static void  _get_daily_template() throws Exception{
ResumableSub_GET_DAILY_TEMPLATE rsub = new ResumableSub_GET_DAILY_TEMPLATE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_DAILY_TEMPLATE extends BA.ResumableSub {
public ResumableSub_GET_DAILY_TEMPLATE(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
int _i = 0;
String _month_num = "";
anywheresoftware.b4a.BA.IterableList group10;
int index10;
int groupLen10;
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
 //BA.debugLineNum = 714;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 715;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 716;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_tab_";
_cmd = _createcommand("select_tab_daily_template",new Object[]{(Object)(parent._group_id)});
 //BA.debugLineNum = 717;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 53;
return;
case 53:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 718;BA.debugLine="If jr.Success Then";
if (true) break;

case 1:
//if
this.state = 52;
if (_jr._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 51;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 719;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 720;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 721;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 54;
return;
case 54:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 722;BA.debugLine="If res.Rows.Size > 0 Then";
if (true) break;

case 4:
//if
this.state = 49;
if (_res.Rows /*anywheresoftware.b4a.objects.collections.List*/ .getSize()>0) { 
this.state = 6;
}else {
this.state = 48;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 723;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 46;
group10 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index10 = 0;
groupLen10 = group10.getSize();
this.state = 55;
if (true) break;

case 55:
//C
this.state = 46;
if (index10 < groupLen10) {
this.state = 9;
_row = (Object[])(group10.Get(index10));}
if (true) break;

case 56:
//C
this.state = 55;
index10++;
if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 724;BA.debugLine="Log(row(res.Columns.Get(\"group_name\")))";
anywheresoftware.b4a.keywords.Common.LogImpl("777987851",BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("group_name"))))]),0);
 //BA.debugLineNum = 725;BA.debugLine="Log(row(res.Columns.Get(\"year\")))";
anywheresoftware.b4a.keywords.Common.LogImpl("777987852",BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year"))))]),0);
 //BA.debugLineNum = 726;BA.debugLine="Log(row(res.Columns.Get(\"month\")))";
anywheresoftware.b4a.keywords.Common.LogImpl("777987853",BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]),0);
 //BA.debugLineNum = 727;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT * FROM";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_template_table WHERE group_name = '"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("group_name"))))])+"' AND year = '"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year"))))])+"' AND month = '"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))])+"'")));
 //BA.debugLineNum = 728;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 10:
//if
this.state = 45;
if (parent._cursor6.getRowCount()>0) { 
this.state = 12;
}else {
this.state = 18;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 729;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
if (true) break;

case 13:
//for
this.state = 16;
step16 = 1;
limit16 = (int) (parent._cursor6.getRowCount()-1);
_i = (int) (0) ;
this.state = 57;
if (true) break;

case 57:
//C
this.state = 16;
if ((step16 > 0 && _i <= limit16) || (step16 < 0 && _i >= limit16)) this.state = 15;
if (true) break;

case 58:
//C
this.state = 57;
_i = ((int)(0 + _i + step16)) ;
if (true) break;

case 15:
//C
this.state = 58;
 //BA.debugLineNum = 730;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 59;
return;
case 59:
//C
this.state = 58;
;
 //BA.debugLineNum = 731;BA.debugLine="cursor6.Position = i";
parent._cursor6.setPosition(_i);
 //BA.debugLineNum = 732;BA.debugLine="Msgbox2Async(\"This template is already exist";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This template is already existing in your tablet. Please check your template name you searching."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;
if (true) break;

case 16:
//C
this.state = 45;
;
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 736;BA.debugLine="Dim month_num As String";
_month_num = "";
 //BA.debugLineNum = 738;BA.debugLine="If row(res.Columns.Get(\"month\")) = \"January\"";
if (true) break;

case 19:
//if
this.state = 44;
if ((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("January"))) { 
this.state = 21;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("February"))) { 
this.state = 23;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("March"))) { 
this.state = 25;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("April"))) { 
this.state = 27;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("May"))) { 
this.state = 29;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("June"))) { 
this.state = 31;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("July"))) { 
this.state = 33;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("August"))) { 
this.state = 35;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("September"))) { 
this.state = 37;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("October"))) { 
this.state = 39;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("November"))) { 
this.state = 41;
}else if((_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))]).equals((Object)("December"))) { 
this.state = 43;
}if (true) break;

case 21:
//C
this.state = 44;
 //BA.debugLineNum = 739;BA.debugLine="month_num = \"01\"";
_month_num = "01";
 if (true) break;

case 23:
//C
this.state = 44;
 //BA.debugLineNum = 741;BA.debugLine="month_num = \"02\"";
_month_num = "02";
 if (true) break;

case 25:
//C
this.state = 44;
 //BA.debugLineNum = 743;BA.debugLine="month_num = \"03\"";
_month_num = "03";
 if (true) break;

case 27:
//C
this.state = 44;
 //BA.debugLineNum = 745;BA.debugLine="month_num = \"04\"";
_month_num = "04";
 if (true) break;

case 29:
//C
this.state = 44;
 //BA.debugLineNum = 747;BA.debugLine="month_num = \"05\"";
_month_num = "05";
 if (true) break;

case 31:
//C
this.state = 44;
 //BA.debugLineNum = 749;BA.debugLine="month_num = \"06\"";
_month_num = "06";
 if (true) break;

case 33:
//C
this.state = 44;
 //BA.debugLineNum = 751;BA.debugLine="month_num = \"07\"";
_month_num = "07";
 if (true) break;

case 35:
//C
this.state = 44;
 //BA.debugLineNum = 753;BA.debugLine="month_num = \"08\"";
_month_num = "08";
 if (true) break;

case 37:
//C
this.state = 44;
 //BA.debugLineNum = 755;BA.debugLine="month_num = \"09\"";
_month_num = "09";
 if (true) break;

case 39:
//C
this.state = 44;
 //BA.debugLineNum = 757;BA.debugLine="month_num = \"10\"";
_month_num = "10";
 if (true) break;

case 41:
//C
this.state = 44;
 //BA.debugLineNum = 759;BA.debugLine="month_num = \"11\"";
_month_num = "11";
 if (true) break;

case 43:
//C
this.state = 44;
 //BA.debugLineNum = 761;BA.debugLine="month_num = \"12\"";
_month_num = "12";
 if (true) break;

case 44:
//C
this.state = 45;
;
 //BA.debugLineNum = 765;BA.debugLine="group_id_new = row(res.Columns.Get(\"group_nam";
parent._group_id_new = BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("group_name"))))])+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year"))))])+_month_num+parent.mostCurrent._login_module._tab_id /*String*/ ;
 //BA.debugLineNum = 767;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO daily_te";
parent._connection.ExecNonQuery("INSERT INTO daily_template_table VALUES ('"+parent._group_id_new+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("group_name"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year"))))])+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','0','-','-','"+parent.mostCurrent._login_module._username /*String*/ +"','"+parent.mostCurrent._login_module._tab_id /*String*/ +"')");
 //BA.debugLineNum = 770;BA.debugLine="Sleep(00)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (00));
this.state = 60;
return;
case 60:
//C
this.state = 45;
;
 //BA.debugLineNum = 771;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 772;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 61;
return;
case 61:
//C
this.state = 45;
;
 //BA.debugLineNum = 773;BA.debugLine="GET_DAILY_TEMPLATE_REF";
_get_daily_template_ref();
 if (true) break;

case 45:
//C
this.state = 56;
;
 if (true) break;
if (true) break;

case 46:
//C
this.state = 49;
;
 if (true) break;

case 48:
//C
this.state = 49;
 if (true) break;

case 49:
//C
this.state = 52;
;
 if (true) break;

case 51:
//C
this.state = 52;
 //BA.debugLineNum = 780;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 781;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 62;
return;
case 62:
//C
this.state = 52;
;
 //BA.debugLineNum = 782;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 783;BA.debugLine="Msgbox2Async(\"CAN'T CONNECT TO SYSTEM.\" & CRLF &";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("CAN'T CONNECT TO SYSTEM."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 784;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 785;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 if (true) break;

case 52:
//C
this.state = -1;
;
 //BA.debugLineNum = 787;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(wingan.app.httpjob _jr) throws Exception{
}
public static void  _req_result(wingan.app.main._dbresult _res) throws Exception{
}
public static void  _get_daily_template_ref() throws Exception{
ResumableSub_GET_DAILY_TEMPLATE_REF rsub = new ResumableSub_GET_DAILY_TEMPLATE_REF(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_DAILY_TEMPLATE_REF extends BA.ResumableSub {
public ResumableSub_GET_DAILY_TEMPLATE_REF(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
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
 //BA.debugLineNum = 789;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 790;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_tab_";
_cmd = _createcommand("select_tab_template_ref",new Object[]{(Object)(parent._group_id)});
 //BA.debugLineNum = 791;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 792;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 793;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 794;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 18;
return;
case 18:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 795;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 796;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group8 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index8 = 0;
groupLen8 = group8.getSize();
this.state = 19;
if (true) break;

case 19:
//C
this.state = 10;
if (index8 < groupLen8) {
this.state = 9;
_row = (Object[])(group8.Get(index8));}
if (true) break;

case 20:
//C
this.state = 19;
index8++;
if (true) break;

case 9:
//C
this.state = 20;
 //BA.debugLineNum = 798;BA.debugLine="connection.ExecNonQuery(\"INSERT INTO daily_in";
parent._connection.ExecNonQuery("INSERT INTO daily_inventory_ref_table VALUES ('"+parent._group_id_new+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("principal_name"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_id"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_variant"))))])+"','"+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("product_description"))))])+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+parent.mostCurrent._login_module._tab_id /*String*/ +"','"+parent.mostCurrent._login_module._username /*String*/ +"')");
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 //BA.debugLineNum = 802;BA.debugLine="PANEL_BG_DOWNLOAD.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_download.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 803;BA.debugLine="ToastMessageShow(\"Template Downloaded Succesful";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Template Downloaded Succesfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 804;BA.debugLine="LOAD_GROUP";
_load_group();
 //BA.debugLineNum = 805;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 21;
return;
case 21:
//C
this.state = 13;
;
 //BA.debugLineNum = 806;BA.debugLine="CMB_GROUP.SelectedIndex = -1";
parent.mostCurrent._cmb_group._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 807;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 22;
return;
case 22:
//C
this.state = 13;
;
 //BA.debugLineNum = 808;BA.debugLine="OpenSpinner(CMB_GROUP.cmbBox)";
_openspinner(parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 809;BA.debugLine="ENABLE_VIEW";
_enable_view();
 //BA.debugLineNum = 810;BA.debugLine="LISTVIEW_TEMPLATES.Clear";
parent.mostCurrent._listview_templates.Clear();
 if (true) break;

case 12:
//C
this.state = 13;
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 815;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 816;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 23;
return;
case 23:
//C
this.state = 16;
;
 //BA.debugLineNum = 817;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 818;BA.debugLine="Msgbox2Async(\"CAN'T CONNECT TO SYSTEM.\" & CRLF &";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("CAN'T CONNECT TO SYSTEM."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 819;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 821;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 822;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_tabid() throws Exception{
ResumableSub_GET_TABID rsub = new ResumableSub_GET_TABID(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_TABID extends BA.ResumableSub {
public ResumableSub_GET_TABID(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group11;
int index11;
int groupLen11;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 658;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 659;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 660;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_tab_";
_cmd = _createcommand("select_tab_id",new Object[]{(Object)(parent.mostCurrent._login_module._tab_id /*String*/ )});
 //BA.debugLineNum = 661;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 662;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 663;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 664;BA.debugLine="CMB_TABID.cmbBox.Clear";
parent.mostCurrent._cmb_tabid._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 665;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 666;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 18;
return;
case 18:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 667;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 668;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group11 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index11 = 0;
groupLen11 = group11.getSize();
this.state = 19;
if (true) break;

case 19:
//C
this.state = 10;
if (index11 < groupLen11) {
this.state = 9;
_row = (Object[])(group11.Get(index11));}
if (true) break;

case 20:
//C
this.state = 19;
index11++;
if (true) break;

case 9:
//C
this.state = 20;
 //BA.debugLineNum = 669;BA.debugLine="CMB_TABID.cmbBox.Add(\"TABLET \" & row(res.Colum";
parent.mostCurrent._cmb_tabid._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("TABLET "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("tab_id"))))]));
 //BA.debugLineNum = 670;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 20;
;
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 673;BA.debugLine="CMB_TABID.cmbBox.Clear";
parent.mostCurrent._cmb_tabid._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 676;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 677;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 22;
return;
case 22:
//C
this.state = 16;
;
 //BA.debugLineNum = 678;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 679;BA.debugLine="Msgbox2Async(\"TAB ID IS NOT UPDATED.\" & CRLF & C";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("TAB ID IS NOT UPDATED."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 680;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 682;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 683;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_templates() throws Exception{
ResumableSub_GET_TEMPLATES rsub = new ResumableSub_GET_TEMPLATES(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_TEMPLATES extends BA.ResumableSub {
public ResumableSub_GET_TEMPLATES(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
wingan.app.dbrequestmanager _req = null;
wingan.app.main._dbcommand _cmd = null;
wingan.app.httpjob _jr = null;
wingan.app.main._dbresult _res = null;
Object[] _row = null;
anywheresoftware.b4a.BA.IterableList group13;
int index13;
int groupLen13;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 685;BA.debugLine="LIST_TEMPLATE";
_list_template();
 //BA.debugLineNum = 686;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 687;BA.debugLine="Dim req As DBRequestManager = CreateRequest";
_req = _createrequest();
 //BA.debugLineNum = 688;BA.debugLine="Log(CMB_TABID.cmbBox.SelectedItem.SubString2(7,8)";
anywheresoftware.b4a.keywords.Common.LogImpl("777922308",parent.mostCurrent._cmb_tabid._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem().substring((int) (7),(int) (8)),0);
 //BA.debugLineNum = 689;BA.debugLine="Dim cmd As DBCommand = CreateCommand(\"select_tab_";
_cmd = _createcommand("select_tab_template",new Object[]{(Object)(parent.mostCurrent._cmb_tabid._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem().substring((int) (7),(int) (8)))});
 //BA.debugLineNum = 690;BA.debugLine="Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_req._executequery /*wingan.app.httpjob*/ (_cmd,(int) (0),anywheresoftware.b4a.keywords.Common.Null)));
this.state = 17;
return;
case 17:
//C
this.state = 1;
_jr = (wingan.app.httpjob) result[0];
;
 //BA.debugLineNum = 691;BA.debugLine="If jr.Success Then";
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
 //BA.debugLineNum = 692;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 693;BA.debugLine="LISTVIEW_TEMPLATES.Clear";
parent.mostCurrent._listview_templates.Clear();
 //BA.debugLineNum = 694;BA.debugLine="req.HandleJobAsync(jr, \"req\")";
_req._handlejobasync /*void*/ (_jr,"req");
 //BA.debugLineNum = 695;BA.debugLine="Wait For (req) req_Result(res As DBResult)";
anywheresoftware.b4a.keywords.Common.WaitFor("req_result", processBA, this, (Object)(_req));
this.state = 18;
return;
case 18:
//C
this.state = 4;
_res = (wingan.app.main._dbresult) result[0];
;
 //BA.debugLineNum = 696;BA.debugLine="If res.Rows.Size > 0 Then";
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
 //BA.debugLineNum = 697;BA.debugLine="For Each row() As Object In res.Rows";
if (true) break;

case 7:
//for
this.state = 10;
group13 = _res.Rows /*anywheresoftware.b4a.objects.collections.List*/ ;
index13 = 0;
groupLen13 = group13.getSize();
this.state = 19;
if (true) break;

case 19:
//C
this.state = 10;
if (index13 < groupLen13) {
this.state = 9;
_row = (Object[])(group13.Get(index13));}
if (true) break;

case 20:
//C
this.state = 19;
index13++;
if (true) break;

case 9:
//C
this.state = 20;
 //BA.debugLineNum = 698;BA.debugLine="LISTVIEW_TEMPLATES.AddTwoLines2(row(res.Column";
parent.mostCurrent._listview_templates.AddTwoLines2(BA.ObjectToCharSequence(BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("month"))))])+" "+BA.ObjectToString(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("year"))))])),BA.ObjectToCharSequence(_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("group_name"))))]),_row[(int)(BA.ObjectToNumber(_res.Columns /*anywheresoftware.b4a.objects.collections.Map*/ .Get((Object)("group_id"))))]);
 //BA.debugLineNum = 699;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 20;
;
 if (true) break;
if (true) break;

case 10:
//C
this.state = 13;
;
 if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 702;BA.debugLine="LISTVIEW_TEMPLATES.Clear";
parent.mostCurrent._listview_templates.Clear();
 if (true) break;

case 13:
//C
this.state = 16;
;
 if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 705;BA.debugLine="ProgressDialogShow2(\"Error in Updating.\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Error in Updating."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 706;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 22;
return;
case 22:
//C
this.state = 16;
;
 //BA.debugLineNum = 707;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 708;BA.debugLine="Msgbox2Async(\"CAN'T CONNECT TO SYSTEM.\" & CRLF &";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("CAN'T CONNECT TO SYSTEM."+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Solution:"+anywheresoftware.b4a.keywords.Common.CRLF+"1. Make sure the device is connected on the WiFi network"+anywheresoftware.b4a.keywords.Common.CRLF+"2. If device is already connected and error still exist, Please inform IT Depatment"),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 709;BA.debugLine="ToastMessageShow(\"Updating Error\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating Error"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 711;BA.debugLine="jr.Release";
_jr._release /*String*/ ();
 //BA.debugLineNum = 712;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static de.amberhome.objects.appcompat.ACMenuItemWrapper  _getmenuitem(String _title) throws Exception{
int _i = 0;
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 205;BA.debugLine="Sub GetMenuItem(Title As String) As ACMenuItem";
 //BA.debugLineNum = 206;BA.debugLine="For i = 0 To ACToolBarLight1.Menu.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (mostCurrent._actoolbarlight1.getMenu().Size()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 207;BA.debugLine="Dim m As ACMenuItem = ACToolBarLight1.Menu.GetIt";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = mostCurrent._actoolbarlight1.getMenu().GetItem(_i);
 //BA.debugLineNum = 208;BA.debugLine="If m.Title = Title Then";
if ((_m.getTitle()).equals(_title)) { 
 //BA.debugLineNum = 209;BA.debugLine="Return m";
if (true) return _m;
 };
 }
};
 //BA.debugLineNum = 212;BA.debugLine="Return Null";
if (true) return (de.amberhome.objects.appcompat.ACMenuItemWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new de.amberhome.objects.appcompat.ACMenuItemWrapper(), (android.view.MenuItem)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return null;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 35;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 36;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 39;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 41;BA.debugLine="Private SearchTemplate2 As B4XSearchTemplate";
mostCurrent._searchtemplate2 = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 43;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 44;BA.debugLine="Private NameColumn(3) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (3)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 46;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 47;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 49;BA.debugLine="Private LABEL_LOAD_GROUP As Label";
mostCurrent._label_load_group = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private CMB_CREATE_YEAR As B4XComboBox";
mostCurrent._cmb_create_year = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 51;BA.debugLine="Private CMB_CREATE_MONTH As B4XComboBox";
mostCurrent._cmb_create_month = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 52;BA.debugLine="Private PANEL_BG_CREATE As Panel";
mostCurrent._panel_bg_create = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private PANEL_CREATE As Panel";
mostCurrent._panel_create = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private CMB_GROUP As B4XComboBox";
mostCurrent._cmb_group = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 55;BA.debugLine="Private CMB_YEAR As B4XComboBox";
mostCurrent._cmb_year = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 56;BA.debugLine="Private CMB_MONTH As B4XComboBox";
mostCurrent._cmb_month = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 57;BA.debugLine="Private BUTTON_SAVE As Button";
mostCurrent._button_save = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private BUTTON_DAILY_COUNT As Button";
mostCurrent._button_daily_count = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private TABLE_INVENTORY_DATE As B4XTable";
mostCurrent._table_inventory_date = new wingan.app.b4xtable();
 //BA.debugLineNum = 60;BA.debugLine="Private BUTTON_VIEW As Button";
mostCurrent._button_view = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private BUTTON_EDIT As Button";
mostCurrent._button_edit = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private CMB_TABID As B4XComboBox";
mostCurrent._cmb_tabid = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 63;BA.debugLine="Private PANEL_BG_DOWNLOAD As Panel";
mostCurrent._panel_bg_download = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private LISTVIEW_TEMPLATES As ListView";
mostCurrent._listview_templates = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static void  _group_suggestion() throws Exception{
ResumableSub_GROUP_SUGGESTION rsub = new ResumableSub_GROUP_SUGGESTION(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GROUP_SUGGESTION extends BA.ResumableSub {
public ResumableSub_GROUP_SUGGESTION(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
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
 //BA.debugLineNum = 304;BA.debugLine="SearchTemplate.CustomListView1.Clear";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 305;BA.debugLine="Dialog.Title = \"Creating a Group Name\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Creating a Group Name");
 //BA.debugLineNum = 306;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 307;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 308;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 309;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT group_name";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT group_name FROM daily_template_table GROUP BY group_name ORDER BY group_name ASC")));
 //BA.debugLineNum = 310;BA.debugLine="If cursor1.RowCount > 0 Then";
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
 //BA.debugLineNum = 311;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
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
 //BA.debugLineNum = 312;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 313;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 314;BA.debugLine="Items.Add(cursor1.GetString(\"group_name\"))";
_items.Add((Object)(parent._cursor1.GetString("group_name")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 316;BA.debugLine="SearchTemplate.SetItems(Items)";
parent.mostCurrent._searchtemplate._setitems /*Object*/ (_items);
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 318;BA.debugLine="Items.Add(\"\")";
_items.Add((Object)(""));
 //BA.debugLineNum = 319;BA.debugLine="SearchTemplate.SetItems(Items)";
parent.mostCurrent._searchtemplate._setitems /*Object*/ (_items);
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 321;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _label_load_group_click() throws Exception{
ResumableSub_LABEL_LOAD_GROUP_Click rsub = new ResumableSub_LABEL_LOAD_GROUP_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LABEL_LOAD_GROUP_Click extends BA.ResumableSub {
public ResumableSub_LABEL_LOAD_GROUP_Click(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
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
 //BA.debugLineNum = 323;BA.debugLine="GROUP_SUGGESTION";
_group_suggestion();
 //BA.debugLineNum = 324;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 21;
return;
case 21:
//C
this.state = 1;
;
 //BA.debugLineNum = 325;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sear";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate),(Object)(""),(Object)("ENTER"),(Object)("CANCEL"));
 //BA.debugLineNum = 326;BA.debugLine="Dialog.Base.Top = 38%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (38),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 327;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 22;
return;
case 22:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 328;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 20;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}else if(_result==parent.mostCurrent._xui.DialogResponse_Negative) { 
this.state = 11;
}else {
this.state = 19;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 329;BA.debugLine="If SearchTemplate.SelectedItem = \"\" Then";
if (true) break;

case 4:
//if
this.state = 9;
if ((parent.mostCurrent._searchtemplate._selecteditem /*String*/ ).equals("")) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 330;BA.debugLine="LABEL_LOAD_GROUP.Text = \"Click me to  add new g";
parent.mostCurrent._label_load_group.setText(BA.ObjectToCharSequence("Click me to  add new group."));
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 332;BA.debugLine="LABEL_LOAD_GROUP.Text = SearchTemplate.Selected";
parent.mostCurrent._label_load_group.setText(BA.ObjectToCharSequence(parent.mostCurrent._searchtemplate._selecteditem /*String*/ ));
 if (true) break;

case 9:
//C
this.state = 20;
;
 //BA.debugLineNum = 334;BA.debugLine="OpenSpinner(CMB_CREATE_YEAR.cmbBox)";
_openspinner(parent.mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 335;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 20;
;
 //BA.debugLineNum = 336;BA.debugLine="CMB_CREATE_YEAR.SelectedIndex = - 1";
parent.mostCurrent._cmb_create_year._setselectedindex /*int*/ ((int) (-1));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 338;BA.debugLine="If SearchTemplate.SearchField.Text = \"\" Then";
if (true) break;

case 12:
//if
this.state = 17;
if ((parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettext /*String*/ ()).equals("")) { 
this.state = 14;
}else {
this.state = 16;
}if (true) break;

case 14:
//C
this.state = 17;
 //BA.debugLineNum = 339;BA.debugLine="LABEL_LOAD_GROUP.Text = \"Click me to  add new g";
parent.mostCurrent._label_load_group.setText(BA.ObjectToCharSequence("Click me to  add new group."));
 //BA.debugLineNum = 340;BA.debugLine="OpenSpinner(CMB_CREATE_YEAR.cmbBox)";
_openspinner(parent.mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 341;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 17;
;
 //BA.debugLineNum = 342;BA.debugLine="CMB_CREATE_YEAR.SelectedIndex = - 1";
parent.mostCurrent._cmb_create_year._setselectedindex /*int*/ ((int) (-1));
 if (true) break;

case 16:
//C
this.state = 17;
 //BA.debugLineNum = 344;BA.debugLine="LABEL_LOAD_GROUP.Text = SearchTemplate.SearchFi";
parent.mostCurrent._label_load_group.setText(BA.ObjectToCharSequence(parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettext /*String*/ ().toUpperCase()));
 //BA.debugLineNum = 345;BA.debugLine="OpenSpinner(CMB_CREATE_YEAR.cmbBox)";
_openspinner(parent.mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 346;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 17;
;
 //BA.debugLineNum = 347;BA.debugLine="CMB_CREATE_YEAR.SelectedIndex = - 1";
parent.mostCurrent._cmb_create_year._setselectedindex /*int*/ ((int) (-1));
 if (true) break;

case 17:
//C
this.state = 20;
;
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 350;BA.debugLine="LABEL_LOAD_GROUP.Text = \"Click me to  add new gr";
parent.mostCurrent._label_load_group.setText(BA.ObjectToCharSequence("Click me to  add new group."));
 //BA.debugLineNum = 351;BA.debugLine="OpenSpinner(CMB_CREATE_YEAR.cmbBox)";
_openspinner(parent.mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 352;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 26;
return;
case 26:
//C
this.state = 20;
;
 //BA.debugLineNum = 353;BA.debugLine="CMB_CREATE_YEAR.SelectedIndex = - 1";
parent.mostCurrent._cmb_create_year._setselectedindex /*int*/ ((int) (-1));
 if (true) break;

case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 355;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static void  _list_template() throws Exception{
ResumableSub_LIST_TEMPLATE rsub = new ResumableSub_LIST_TEMPLATE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LIST_TEMPLATE extends BA.ResumableSub {
public ResumableSub_LIST_TEMPLATE(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 826;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 828;BA.debugLine="bg.Initialize2(Colors.White, 5, 1, Colors.LightGr";
_bg.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.White,(int) (5),(int) (1),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 829;BA.debugLine="LISTVIEW_TEMPLATES.Background = bg";
parent.mostCurrent._listview_templates.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 830;BA.debugLine="LISTVIEW_TEMPLATES.Clear";
parent.mostCurrent._listview_templates.Clear();
 //BA.debugLineNum = 831;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 832;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.Label.Typeface";
parent.mostCurrent._listview_templates.getTwoLinesLayout().Label.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 833;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.Label.TextSize";
parent.mostCurrent._listview_templates.getTwoLinesLayout().Label.setTextSize((float) (20));
 //BA.debugLineNum = 834;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.label.Top = 0.5";
parent.mostCurrent._listview_templates.getTwoLinesLayout().Label.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0.5),mostCurrent.activityBA));
 //BA.debugLineNum = 835;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.label.Height =";
parent.mostCurrent._listview_templates.getTwoLinesLayout().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 836;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.Label.TextColor";
parent.mostCurrent._listview_templates.getTwoLinesLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 837;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.Label.Gravity =";
parent.mostCurrent._listview_templates.getTwoLinesLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 838;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Typ";
parent.mostCurrent._listview_templates.getTwoLinesLayout().SecondLabel.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT);
 //BA.debugLineNum = 839;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Top";
parent.mostCurrent._listview_templates.getTwoLinesLayout().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4.5),mostCurrent.activityBA));
 //BA.debugLineNum = 840;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Tex";
parent.mostCurrent._listview_templates.getTwoLinesLayout().SecondLabel.setTextSize((float) (14));
 //BA.debugLineNum = 841;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Hei";
parent.mostCurrent._listview_templates.getTwoLinesLayout().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA));
 //BA.debugLineNum = 842;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Tex";
parent.mostCurrent._listview_templates.getTwoLinesLayout().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 843;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Gra";
parent.mostCurrent._listview_templates.getTwoLinesLayout().SecondLabel.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.TOP);
 //BA.debugLineNum = 844;BA.debugLine="LISTVIEW_TEMPLATES.TwoLinesLayout.ItemHeight = 8%";
parent.mostCurrent._listview_templates.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 845;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _listview_templates_itemclick(int _position,Object _value) throws Exception{
ResumableSub_LISTVIEW_TEMPLATES_ItemClick rsub = new ResumableSub_LISTVIEW_TEMPLATES_ItemClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_LISTVIEW_TEMPLATES_ItemClick extends BA.ResumableSub {
public ResumableSub_LISTVIEW_TEMPLATES_ItemClick(wingan.app.daily_inventory_module parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
wingan.app.daily_inventory_module parent;
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
 //BA.debugLineNum = 857;BA.debugLine="Msgbox2Async(\"Do you want to download this templa";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Do you want to download this template?"),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 858;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 5;
return;
case 5:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 859;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 860;BA.debugLine="group_id = Value";
parent._group_id = BA.ObjectToString(_value);
 //BA.debugLineNum = 861;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = 4;
;
 //BA.debugLineNum = 862;BA.debugLine="GET_DAILY_TEMPLATE";
_get_daily_template();
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 865;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_create_month() throws Exception{
 //BA.debugLineNum = 369;BA.debugLine="Sub LOAD_CREATE_MONTH";
 //BA.debugLineNum = 370;BA.debugLine="CMB_CREATE_MONTH.cmbBox.DropdownTextColor = Color";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 371;BA.debugLine="CMB_CREATE_MONTH.cmbBox.TextColor = Colors.White";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 372;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Clear";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 373;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"January\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("January");
 //BA.debugLineNum = 374;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"February\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("February");
 //BA.debugLineNum = 375;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"March\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("March");
 //BA.debugLineNum = 376;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"April\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("April");
 //BA.debugLineNum = 377;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"May\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("May");
 //BA.debugLineNum = 378;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"June\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("June");
 //BA.debugLineNum = 379;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"July\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("July");
 //BA.debugLineNum = 380;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"August\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("August");
 //BA.debugLineNum = 381;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"September\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("September");
 //BA.debugLineNum = 382;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"October\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("October");
 //BA.debugLineNum = 383;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"November\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("November");
 //BA.debugLineNum = 384;BA.debugLine="CMB_CREATE_MONTH.cmbBox.Add(\"December\")";
mostCurrent._cmb_create_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add("December");
 //BA.debugLineNum = 385;BA.debugLine="End Sub";
return "";
}
public static String  _load_create_year() throws Exception{
int _i = 0;
 //BA.debugLineNum = 356;BA.debugLine="Sub LOAD_CREATE_YEAR";
 //BA.debugLineNum = 357;BA.debugLine="CMB_CREATE_YEAR.cmbBox.DropdownTextColor = Colors";
mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setDropdownTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 358;BA.debugLine="CMB_CREATE_YEAR.cmbBox.TextColor = Colors.White";
mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 359;BA.debugLine="CMB_CREATE_YEAR.cmbBox.Clear";
mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 360;BA.debugLine="For i = 0 To 10";
{
final int step4 = 1;
final int limit4 = (int) (10);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 361;BA.debugLine="CMB_CREATE_YEAR.cmbBox.Add(DateTime.GetYear(Date";
mostCurrent._cmb_create_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+_i));
 }
};
 //BA.debugLineNum = 363;BA.debugLine="End Sub";
return "";
}
public static void  _load_group() throws Exception{
ResumableSub_LOAD_GROUP rsub = new ResumableSub_LOAD_GROUP(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_GROUP extends BA.ResumableSub {
public ResumableSub_LOAD_GROUP(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
int _i = 0;
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
 //BA.debugLineNum = 237;BA.debugLine="CMB_GROUP.cmbBox.Clear";
parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 238;BA.debugLine="CMB_YEAR.cmbBox.Clear";
parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 239;BA.debugLine="CMB_MONTH.cmbBox.Clear";
parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 240;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT group_name";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT group_name FROM daily_template_table GROUP BY group_name ORDER BY group_name ASC")));
 //BA.debugLineNum = 241;BA.debugLine="If cursor1.RowCount > 0 Then";
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
 //BA.debugLineNum = 242;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step6 = 1;
limit6 = (int) (parent._cursor1.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step6 > 0 && _i <= limit6) || (step6 < 0 && _i >= limit6)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step6)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 243;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 244;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 245;BA.debugLine="CMB_GROUP.cmbBox.Add(cursor1.GetString(\"group_n";
parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor1.GetString("group_name"));
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
 //BA.debugLineNum = 250;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_inventory_date() throws Exception{
ResumableSub_LOAD_INVENTORY_DATE rsub = new ResumableSub_LOAD_INVENTORY_DATE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_INVENTORY_DATE extends BA.ResumableSub {
public ResumableSub_LOAD_INVENTORY_DATE(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
anywheresoftware.b4a.objects.collections.List _data = null;
int _ia = 0;
Object[] _row = null;
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
 //BA.debugLineNum = 554;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 15;
return;
case 15:
//C
this.state = 1;
;
 //BA.debugLineNum = 555;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 556;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 557;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"+parent._group_id+"' GROUP BY inventory_date ORDER by inventory_date DESC")));
 //BA.debugLineNum = 558;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._cursor4.getRowCount()>0) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 559;BA.debugLine="For ia = 0 To cursor4.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step6 = 1;
limit6 = (int) (parent._cursor4.getRowCount()-1);
_ia = (int) (0) ;
this.state = 16;
if (true) break;

case 16:
//C
this.state = 7;
if ((step6 > 0 && _ia <= limit6) || (step6 < 0 && _ia >= limit6)) this.state = 6;
if (true) break;

case 17:
//C
this.state = 16;
_ia = ((int)(0 + _ia + step6)) ;
if (true) break;

case 6:
//C
this.state = 17;
 //BA.debugLineNum = 560;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 18;
return;
case 18:
//C
this.state = 17;
;
 //BA.debugLineNum = 561;BA.debugLine="cursor4.Position = ia";
parent._cursor4.setPosition(_ia);
 //BA.debugLineNum = 562;BA.debugLine="Dim row(3) As Object";
_row = new Object[(int) (3)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 563;BA.debugLine="row(0) = cursor4.GetString(\"inventory_date\")";
_row[(int) (0)] = (Object)(parent._cursor4.GetString("inventory_date"));
 //BA.debugLineNum = 564;BA.debugLine="row(1) = cursor4.GetString(\"user_info\")";
_row[(int) (1)] = (Object)(parent._cursor4.GetString("user_info"));
 //BA.debugLineNum = 565;BA.debugLine="row(2) = cursor4.GetString(\"status\")";
_row[(int) (2)] = (Object)(parent._cursor4.GetString("status"));
 //BA.debugLineNum = 566;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
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
 //BA.debugLineNum = 569;BA.debugLine="ToastMessageShow(\"No exisiting inventory date\",";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No exisiting inventory date"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 10:
//C
this.state = 11;
;
 //BA.debugLineNum = 571;BA.debugLine="TABLE_INVENTORY_DATE.SetData(Data)";
parent.mostCurrent._table_inventory_date._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 572;BA.debugLine="If XSelections.IsInitialized = False Then";
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
 //BA.debugLineNum = 573;BA.debugLine="XSelections.Initialize(TABLE_INVENTORY_DATE)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._table_inventory_date);
 //BA.debugLineNum = 574;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 576;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = -1;
;
 //BA.debugLineNum = 577;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_month() throws Exception{
ResumableSub_LOAD_MONTH rsub = new ResumableSub_LOAD_MONTH(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_MONTH extends BA.ResumableSub {
public ResumableSub_LOAD_MONTH(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
int _i = 0;
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
 //BA.debugLineNum = 280;BA.debugLine="CMB_MONTH.cmbBox.Clear";
parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 281;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT group_id,";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT group_id, month FROM daily_template_table WHERE group_name = '"+parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and year = '"+parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' ORDER BY group_id ASC")));
 //BA.debugLineNum = 282;BA.debugLine="If cursor3.RowCount > 0 Then";
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
 //BA.debugLineNum = 283;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step4 = 1;
limit4 = (int) (parent._cursor3.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step4)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 284;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 285;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 286;BA.debugLine="CMB_MONTH.cmbBox.Add(cursor3.GetString(\"month\")";
parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor3.GetString("month"));
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
 //BA.debugLineNum = 291;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_table_header() throws Exception{
 //BA.debugLineNum = 548;BA.debugLine="Sub LOAD_TABLE_HEADER";
 //BA.debugLineNum = 549;BA.debugLine="NameColumn(0)=TABLE_INVENTORY_DATE.AddColumn(\"Inv";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._table_inventory_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Inventory Date",mostCurrent._table_inventory_date._column_type_text /*int*/ );
 //BA.debugLineNum = 550;BA.debugLine="NameColumn(1)=TABLE_INVENTORY_DATE.AddColumn(\"Use";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._table_inventory_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("User",mostCurrent._table_inventory_date._column_type_text /*int*/ );
 //BA.debugLineNum = 551;BA.debugLine="NameColumn(2)=TABLE_INVENTORY_DATE.AddColumn(\"Sta";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._table_inventory_date._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Status",mostCurrent._table_inventory_date._column_type_text /*int*/ );
 //BA.debugLineNum = 552;BA.debugLine="End Sub";
return "";
}
public static void  _load_year() throws Exception{
ResumableSub_LOAD_YEAR rsub = new ResumableSub_LOAD_YEAR(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_YEAR extends BA.ResumableSub {
public ResumableSub_LOAD_YEAR(wingan.app.daily_inventory_module parent) {
this.parent = parent;
}
wingan.app.daily_inventory_module parent;
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
 //BA.debugLineNum = 259;BA.debugLine="CMB_YEAR.cmbBox.Clear";
parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 260;BA.debugLine="CMB_MONTH.cmbBox.Clear";
parent.mostCurrent._cmb_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 261;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT year FROM";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT year FROM daily_template_table WHERE group_name = '"+parent.mostCurrent._cmb_group._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' GROUP BY year ORDER BY year ASC")));
 //BA.debugLineNum = 262;BA.debugLine="If cursor2.RowCount > 0 Then";
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
 //BA.debugLineNum = 263;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 4:
//for
this.state = 7;
step5 = 1;
limit5 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_i = ((int)(0 + _i + step5)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 264;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 265;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 266;BA.debugLine="CMB_YEAR.cmbBox.Add(cursor2.GetString(\"year\"))";
parent.mostCurrent._cmb_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor2.GetString("year"));
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
 //BA.debugLineNum = 271;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _openlabel(anywheresoftware.b4a.objects.LabelWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 173;BA.debugLine="Sub OpenLabel(se As Label)";
 //BA.debugLineNum = 174;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 175;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 176;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 167;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 168;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 169;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 170;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 171;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 20;BA.debugLine="Public group_id As String";
_group_id = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim group_id_new As String";
_group_id_new = "";
 //BA.debugLineNum = 24;BA.debugLine="Public clear_trigger As String";
_clear_trigger = "";
 //BA.debugLineNum = 26;BA.debugLine="Private cartBitmap As Bitmap";
_cartbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private downBitmap As Bitmap";
_downbitmap = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 179;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 180;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 181;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 182;BA.debugLine="Dim in As Int";
_in = 0;
 //BA.debugLineNum = 183;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 184;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 185;BA.debugLine="in = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 186;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 187;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 188;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _table_inventory_date_cellclicked(String _columnid,long _rowid) throws Exception{
 //BA.debugLineNum = 578;BA.debugLine="Sub TABLE_INVENTORY_DATE_CellClicked (ColumnId As";
 //BA.debugLineNum = 579;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 580;BA.debugLine="End Sub";
return "";
}
public static void  _table_inventory_date_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_TABLE_INVENTORY_DATE_CellLongClicked rsub = new ResumableSub_TABLE_INVENTORY_DATE_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_TABLE_INVENTORY_DATE_CellLongClicked extends BA.ResumableSub {
public ResumableSub_TABLE_INVENTORY_DATE_CellLongClicked(wingan.app.daily_inventory_module parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.daily_inventory_module parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
String _cell = "";
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
 //BA.debugLineNum = 582;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 584;BA.debugLine="Dim RowData As Map = TABLE_INVENTORY_DATE.GetRow(";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._table_inventory_date._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 585;BA.debugLine="Dim cell As String = RowData.Get(ColumnId)";
_cell = BA.ObjectToString(_rowdata.Get((Object)(_columnid)));
 //BA.debugLineNum = 587;BA.debugLine="Log(RowData.Get(\"Invetory Date\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("777594630",BA.ObjectToString(_rowdata.Get((Object)("Invetory Date"))),0);
 //BA.debugLineNum = 589;BA.debugLine="Msgbox2Async(\"Inventory Date : \" & RowData.Get(\"I";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Inventory Date : "+BA.ObjectToString(_rowdata.Get((Object)("Inventory Date")))+anywheresoftware.b4a.keywords.Common.CRLF+"Status : "+BA.ObjectToString(_rowdata.Get((Object)("Status")))+anywheresoftware.b4a.keywords.Common.CRLF),BA.ObjectToCharSequence("Option"),"Proceed","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 592;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 9;
return;
case 9:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 593;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 8;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
this.state = 5;
}else if(_result==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
this.state = 7;
}if (true) break;

case 3:
//C
this.state = 8;
 //BA.debugLineNum = 594;BA.debugLine="StartActivity(DAILY_COUNT)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._daily_count.getObject()));
 //BA.debugLineNum = 595;BA.debugLine="DAILY_COUNT.inventory_date = RowData.Get(\"Invent";
parent.mostCurrent._daily_count._inventory_date /*String*/  = BA.ObjectToString(_rowdata.Get((Object)("Inventory Date")));
 //BA.debugLineNum = 596;BA.debugLine="clear_trigger = 0";
parent._clear_trigger = BA.NumberToString(0);
 if (true) break;

case 5:
//C
this.state = 8;
 if (true) break;

case 7:
//C
this.state = 8;
 //BA.debugLineNum = 600;BA.debugLine="TABLE_INVENTORY_DATE.Refresh";
parent.mostCurrent._table_inventory_date._refresh /*String*/ ();
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 602;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _table_inventory_date_dataupdated() throws Exception{
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
 //BA.debugLineNum = 603;BA.debugLine="Sub TABLE_INVENTORY_DATE_DataUpdated";
 //BA.debugLineNum = 604;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 605;BA.debugLine="For Each Column As B4XTableColumn In Array ()";
{
final Object[] group2 = new Object[]{};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 607;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 608;BA.debugLine="For i = 0 To TABLE_INVENTORY_DATE.VisibleRowIds.";
{
final int step4 = 1;
final int limit4 = mostCurrent._table_inventory_date._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 609;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 610;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 611;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 613;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 614;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 615;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 618;BA.debugLine="For i = 0 To TABLE_INVENTORY_DATE.VisibleRowIds.S";
{
final int step14 = 1;
final int limit14 = (int) (mostCurrent._table_inventory_date._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit14 ;_i = _i + step14 ) {
 //BA.debugLineNum = 619;BA.debugLine="Dim RowId As Long = TABLE_INVENTORY_DATE.Visible";
_rowid = BA.ObjectToLongNumber(mostCurrent._table_inventory_date._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 620;BA.debugLine="If RowId > 0 Then";
if (_rowid>0) { 
 //BA.debugLineNum = 621;BA.debugLine="Dim pnl1 As B4XView = NameColumn(2).CellsLayout";
_pnl1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._namecolumn[(int) (2)].CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 622;BA.debugLine="Dim row As Map = TABLE_INVENTORY_DATE.GetRow(Ro";
_row = new anywheresoftware.b4a.objects.collections.Map();
_row = mostCurrent._table_inventory_date._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 623;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 624;BA.debugLine="Dim OtherColumnValue As String = row.Get(NameCo";
_othercolumnvalue = BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (2)].Id /*String*/ )));
 //BA.debugLineNum = 625;BA.debugLine="Log(row.Get(NameColumn(2).Id))";
anywheresoftware.b4a.keywords.Common.LogImpl("777660182",BA.ObjectToString(_row.Get((Object)(mostCurrent._namecolumn[(int) (2)].Id /*String*/ ))),0);
 //BA.debugLineNum = 626;BA.debugLine="If OtherColumnValue = (\"SAVED\") Then";
if ((_othercolumnvalue).equals(("SAVED"))) { 
 //BA.debugLineNum = 627;BA.debugLine="clr = xui.Color_Red";
_clr = mostCurrent._xui.Color_Red;
 }else if((_othercolumnvalue).equals(("FINAL"))) { 
 //BA.debugLineNum = 629;BA.debugLine="clr = xui.Color_Green";
_clr = mostCurrent._xui.Color_Green;
 }else {
 //BA.debugLineNum = 631;BA.debugLine="clr = xui.Color_ARGB(255,82,169,255)";
_clr = mostCurrent._xui.Color_ARGB((int) (255),(int) (82),(int) (169),(int) (255));
 };
 //BA.debugLineNum = 633;BA.debugLine="pnl1.GetView(0).Color = clr";
_pnl1.GetView((int) (0)).setColor(_clr);
 };
 }
};
 //BA.debugLineNum = 637;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 638;BA.debugLine="TABLE_INVENTORY_DATE.Refresh";
mostCurrent._table_inventory_date._refresh /*String*/ ();
 //BA.debugLineNum = 639;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 //BA.debugLineNum = 640;BA.debugLine="XSelections.Refresh";
mostCurrent._xselections._refresh /*String*/ ();
 };
 //BA.debugLineNum = 642;BA.debugLine="End Sub";
return "";
}
public static String  _updateicon(String _menutitle,anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _icon) throws Exception{
de.amberhome.objects.appcompat.ACMenuItemWrapper _m = null;
 //BA.debugLineNum = 201;BA.debugLine="Sub UpdateIcon(MenuTitle As String, Icon As Bitmap";
 //BA.debugLineNum = 202;BA.debugLine="Dim m As ACMenuItem = GetMenuItem(MenuTitle)";
_m = new de.amberhome.objects.appcompat.ACMenuItemWrapper();
_m = _getmenuitem(_menutitle);
 //BA.debugLineNum = 203;BA.debugLine="m.Icon = BitmapToBitmapDrawable(Icon)";
_m.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(_icon).getObject()));
 //BA.debugLineNum = 204;BA.debugLine="End Sub";
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
