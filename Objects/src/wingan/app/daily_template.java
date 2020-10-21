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

public class daily_template extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static daily_template mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.daily_template");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (daily_template).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.daily_template");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.daily_template", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (daily_template) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (daily_template) Resume **");
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
		return daily_template.class;
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
            BA.LogInfo("** Activity (daily_template) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (daily_template) Pause event (activity is not paused). **");
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
            daily_template mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (daily_template) Resume **");
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
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public wingan.app.b4xtableselections _xselections = null;
public wingan.app.b4xtable._b4xtablecolumn[] _namecolumn = null;
public wingan.app.b4xdialog _dialog = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _base = null;
public wingan.app.b4xsearchtemplate _searchtemplate = null;
public wingan.app.b4xsearchtemplate _searchtemplate2 = null;
public anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_group = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_template = null;
public wingan.app.b4xtable _product_template_table = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_month = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_year = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_add = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_bg_copy = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_load_copy_group = null;
public wingan.app.b4xcombobox _cmb_copy_year = null;
public wingan.app.b4xcombobox _cmb_copy_month = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel_copy = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button_save = null;
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
public ResumableSub_Activity_Create(wingan.app.daily_template parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
wingan.app.daily_template parent;
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
 //BA.debugLineNum = 56;BA.debugLine="Activity.LoadLayout(\"daily_create\")";
parent.mostCurrent._activity.LoadLayout("daily_create",mostCurrent.activityBA);
 //BA.debugLineNum = 59;BA.debugLine="ToolbarHelper.Initialize";
parent.mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 60;BA.debugLine="ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadB";
parent.mostCurrent._toolbarhelper.setIcon((android.graphics.drawable.Drawable)(_bitmaptobitmapdrawable(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png")).getObject()));
 //BA.debugLineNum = 61;BA.debugLine="ToolbarHelper.ShowUpIndicator = True 'set to true";
parent.mostCurrent._toolbarhelper.setShowUpIndicator(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 62;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 63;BA.debugLine="bd.Initialize(LoadBitmap(File.DirAssets, \"back.pn";
_bd.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"back.png").getObject()));
 //BA.debugLineNum = 64;BA.debugLine="ToolbarHelper.UpIndicatorDrawable =  bd";
parent.mostCurrent._toolbarhelper.setUpIndicatorDrawable((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 65;BA.debugLine="ACToolBarLight1.InitMenuListener";
parent.mostCurrent._actoolbarlight1.InitMenuListener();
 //BA.debugLineNum = 67;BA.debugLine="Dim jo As JavaObject = ACToolBarLight1";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(parent.mostCurrent._actoolbarlight1.getObject()));
 //BA.debugLineNum = 68;BA.debugLine="jo.RunMethod(\"setContentInsetStartWithNavigation\"";
_jo.RunMethod("setContentInsetStartWithNavigation",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)))});
 //BA.debugLineNum = 69;BA.debugLine="jo.RunMethod(\"setTitleMarginStart\", Array(10dip))";
_jo.RunMethod("setTitleMarginStart",new Object[]{(Object)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)))});
 //BA.debugLineNum = 71;BA.debugLine="If connection.IsInitialized = False Then";
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
 //BA.debugLineNum = 72;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/W";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 75;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = parent.mostCurrent._xui.CreatePanel(processBA,"");
 //BA.debugLineNum = 76;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 77;BA.debugLine="cvs.Initialize(p)";
parent.mostCurrent._cvs.Initialize(_p);
 //BA.debugLineNum = 79;BA.debugLine="Base = Activity";
parent.mostCurrent._base = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(parent.mostCurrent._activity.getObject()));
 //BA.debugLineNum = 80;BA.debugLine="Dialog.Initialize(Base)";
parent.mostCurrent._dialog._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._base);
 //BA.debugLineNum = 81;BA.debugLine="Dialog.BorderColor = Colors.Transparent";
parent.mostCurrent._dialog._bordercolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Transparent;
 //BA.debugLineNum = 82;BA.debugLine="Dialog.BorderCornersRadius = 5";
parent.mostCurrent._dialog._bordercornersradius /*int*/  = (int) (5);
 //BA.debugLineNum = 83;BA.debugLine="Dialog.TitleBarColor = Colors.RGB(82,169,255)";
parent.mostCurrent._dialog._titlebarcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 84;BA.debugLine="Dialog.TitleBarTextColor = Colors.White";
parent.mostCurrent._dialog._titlebartextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 85;BA.debugLine="Dialog.BackgroundColor = Colors.White";
parent.mostCurrent._dialog._backgroundcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 86;BA.debugLine="Dialog.ButtonsColor = Colors.White";
parent.mostCurrent._dialog._buttonscolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 87;BA.debugLine="Dialog.ButtonsTextColor = Colors.Black";
parent.mostCurrent._dialog._buttonstextcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 88;BA.debugLine="Dialog.PutAtTop = True";
parent.mostCurrent._dialog._putattop /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 90;BA.debugLine="SearchTemplate.Initialize";
parent.mostCurrent._searchtemplate._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 91;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextBackgro";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 92;BA.debugLine="SearchTemplate.CustomListView1.DefaultTextColor =";
parent.mostCurrent._searchtemplate._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 93;BA.debugLine="SearchTemplate.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 94;BA.debugLine="SearchTemplate.ItemHightlightColor = Colors.White";
parent.mostCurrent._searchtemplate._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 95;BA.debugLine="SearchTemplate.TextHighlightColor = Colors.RGB(82";
parent.mostCurrent._searchtemplate._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 98;BA.debugLine="SearchTemplate2.Initialize";
parent.mostCurrent._searchtemplate2._initialize /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 99;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextBackgr";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextbackgroundcolor = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 100;BA.debugLine="SearchTemplate2.CustomListView1.DefaultTextColor";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._defaulttextcolor = anywheresoftware.b4a.keywords.Common.Colors.Black;
 //BA.debugLineNum = 101;BA.debugLine="SearchTemplate2.SearchField.TextField.TextColor =";
parent.mostCurrent._searchtemplate2._searchfield /*wingan.app.b4xfloattextfield*/ ._gettextfield /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 102;BA.debugLine="SearchTemplate2.ItemHightlightColor = Colors.Whit";
parent.mostCurrent._searchtemplate2._itemhightlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 103;BA.debugLine="SearchTemplate2.TextHighlightColor = Colors.RGB(8";
parent.mostCurrent._searchtemplate2._texthighlightcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (82),(int) (169),(int) (255));
 //BA.debugLineNum = 105;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 106;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 108;BA.debugLine="GET_TEMPLATE";
_get_template();
 //BA.debugLineNum = 109;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 5;
return;
case 5:
//C
this.state = -1;
;
 //BA.debugLineNum = 110;BA.debugLine="LOAD_TABLE_HEADER";
_load_table_header();
 //BA.debugLineNum = 111;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 6;
return;
case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 112;BA.debugLine="LOAD_PRODUCT";
_load_product();
 //BA.debugLineNum = 113;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = -1;
;
 //BA.debugLineNum = 114;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 121;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
public static String  _actoolbarlight1_navigationitemclick() throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Sub ACToolBarLight1_NavigationItemClick";
 //BA.debugLineNum = 144;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 145;BA.debugLine="StartActivity(DAILY_INVENTORY_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._daily_inventory_module.getObject()));
 //BA.debugLineNum = 146;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right\")";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.BitmapDrawable  _bitmaptobitmapdrawable(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap) throws Exception{
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 137;BA.debugLine="Sub BitmapToBitmapDrawable (bitmap As Bitmap) As B";
 //BA.debugLineNum = 138;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 139;BA.debugLine="bd.Initialize(bitmap)";
_bd.Initialize((android.graphics.Bitmap)(_bitmap.getObject()));
 //BA.debugLineNum = 140;BA.debugLine="Return bd";
if (true) return _bd;
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return null;
}
public static void  _button_add_click() throws Exception{
ResumableSub_BUTTON_ADD_Click rsub = new ResumableSub_BUTTON_ADD_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_ADD_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_ADD_Click(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;
anywheresoftware.b4a.keywords.Common.ResumableSubWrapper _rs = null;
int _result = 0;
anywheresoftware.b4a.objects.collections.List _data = null;
int _qrow = 0;
int _row = 0;
String _query = "";
int step16;
int limit16;
int step20;
int limit20;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 262;BA.debugLine="ProgressDialogShow2(\"Loading...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 263;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 29;
return;
case 29:
//C
this.state = 1;
;
 //BA.debugLineNum = 264;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 265;BA.debugLine="Dim rs As ResumableSub = Dialog.ShowTemplate(Sear";
_rs = new anywheresoftware.b4a.keywords.Common.ResumableSubWrapper();
_rs = parent.mostCurrent._dialog._showtemplate /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ ((Object)(parent.mostCurrent._searchtemplate2),(Object)(""),(Object)(""),(Object)("CANCEL"));
 //BA.debugLineNum = 266;BA.debugLine="Dialog.Base.Top = 40%y - Dialog.Base.Height / 2";
parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .setTop((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA)-parent.mostCurrent._dialog._base /*anywheresoftware.b4a.objects.B4XViewWrapper*/ .getHeight()/(double)2));
 //BA.debugLineNum = 267;BA.debugLine="Wait For (rs) Complete (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("complete", processBA, this, _rs);
this.state = 30;
return;
case 30:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 268;BA.debugLine="If Result = xui.DialogResponse_Positive Then";
if (true) break;

case 1:
//if
this.state = 28;
if (_result==parent.mostCurrent._xui.DialogResponse_Positive) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 270;BA.debugLine="cursor4 = connection.ExecQuery(\"SELECT * FROM da";
parent._cursor4 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE product_description ='"+parent.mostCurrent._searchtemplate2._selecteditem /*String*/ +"' and group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"'")));
 //BA.debugLineNum = 271;BA.debugLine="If cursor4.RowCount > 0 Then";
if (true) break;

case 4:
//if
this.state = 27;
if (parent._cursor4.getRowCount()>0) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 27;
 //BA.debugLineNum = 272;BA.debugLine="Msgbox2Async(\"The product you adding is already";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product you adding is already exisiting in the template. Cannot proceed."),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 273;BA.debugLine="OpenButton(BUTTON_ADD)";
_openbutton(parent.mostCurrent._button_add);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 275;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 276;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 277;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT * FROM pr";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"+parent.mostCurrent._searchtemplate2._selecteditem /*String*/ +"'")));
 //BA.debugLineNum = 278;BA.debugLine="For qrow = 0 To cursor3.RowCount - 1";
if (true) break;

case 9:
//for
this.state = 26;
step16 = 1;
limit16 = (int) (parent._cursor3.getRowCount()-1);
_qrow = (int) (0) ;
this.state = 31;
if (true) break;

case 31:
//C
this.state = 26;
if ((step16 > 0 && _qrow <= limit16) || (step16 < 0 && _qrow >= limit16)) this.state = 11;
if (true) break;

case 32:
//C
this.state = 31;
_qrow = ((int)(0 + _qrow + step16)) ;
if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 279;BA.debugLine="cursor3.Position = qrow";
parent._cursor3.setPosition(_qrow);
 //BA.debugLineNum = 280;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT principa";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='"+parent._cursor3.GetString("principal_id")+"'")));
 //BA.debugLineNum = 281;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 12:
//if
this.state = 25;
if (parent._cursor6.getRowCount()>0) { 
this.state = 14;
}if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 282;BA.debugLine="For row = 0 To cursor6.RowCount - 1";
if (true) break;

case 15:
//for
this.state = 24;
step20 = 1;
limit20 = (int) (parent._cursor6.getRowCount()-1);
_row = (int) (0) ;
this.state = 33;
if (true) break;

case 33:
//C
this.state = 24;
if ((step20 > 0 && _row <= limit20) || (step20 < 0 && _row >= limit20)) this.state = 17;
if (true) break;

case 34:
//C
this.state = 33;
_row = ((int)(0 + _row + step20)) ;
if (true) break;

case 17:
//C
this.state = 18;
 //BA.debugLineNum = 283;BA.debugLine="cursor6.Position = row";
parent._cursor6.setPosition(_row);
 //BA.debugLineNum = 284;BA.debugLine="Dim query As String = \"INSERT INTO daily_inve";
_query = "INSERT INTO daily_inventory_ref_table VALUES (?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 285;BA.debugLine="connection.ExecNonQuery2(query,Array As Strin";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent._cursor3.GetString("principal_id"),parent._cursor6.GetString("principal_name"),parent._cursor3.GetString("product_id"),parent._cursor3.GetString("product_variant"),parent._cursor3.GetString("product_desc"),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._tab_id /*String*/ ,parent.mostCurrent._login_module._username /*String*/ }));
 //BA.debugLineNum = 288;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 35;
return;
case 35:
//C
this.state = 18;
;
 //BA.debugLineNum = 289;BA.debugLine="ToastMessageShow(\"Product added\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product added"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 290;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 36;
return;
case 36:
//C
this.state = 18;
;
 //BA.debugLineNum = 291;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 //BA.debugLineNum = 292;BA.debugLine="Msgbox2Async(\"The product is added to your te";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("The product is added to your template. Do you want to add a product again?"),BA.ObjectToCharSequence("Notice"),"YES","","NO",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 293;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 37;
return;
case 37:
//C
this.state = 18;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 294;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 295;BA.debugLine="OpenButton(BUTTON_ADD)";
_openbutton(parent.mostCurrent._button_add);
 if (true) break;

case 22:
//C
this.state = 23;
 if (true) break;

case 23:
//C
this.state = 34;
;
 if (true) break;
if (true) break;

case 24:
//C
this.state = 25;
;
 if (true) break;

case 25:
//C
this.state = 32;
;
 if (true) break;
if (true) break;

case 26:
//C
this.state = 27;
;
 if (true) break;

case 27:
//C
this.state = 28;
;
 if (true) break;

case 28:
//C
this.state = -1;
;
 //BA.debugLineNum = 305;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _complete(int _result) throws Exception{
}
public static void  _msgbox_result(int _result) throws Exception{
}
public static String  _button_cancel_click() throws Exception{
 //BA.debugLineNum = 369;BA.debugLine="Sub BUTTON_CANCEL_Click";
 //BA.debugLineNum = 370;BA.debugLine="PANEL_BG_COPY.SetVisibleAnimated(300,False)";
mostCurrent._panel_bg_copy.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 371;BA.debugLine="End Sub";
return "";
}
public static void  _button_copy_click() throws Exception{
ResumableSub_BUTTON_COPY_Click rsub = new ResumableSub_BUTTON_COPY_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_COPY_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_COPY_Click(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 331;BA.debugLine="PANEL_BG_COPY.BringToFront";
parent.mostCurrent._panel_bg_copy.BringToFront();
 //BA.debugLineNum = 332;BA.debugLine="PANEL_BG_COPY.SetVisibleAnimated(300, True)";
parent.mostCurrent._panel_bg_copy.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 334;BA.debugLine="LABEL_LOAD_COPY_GROUP.Text = LABEL_LOAD_GROUP.Tex";
parent.mostCurrent._label_load_copy_group.setText(BA.ObjectToCharSequence(parent.mostCurrent._label_load_group.getText()));
 //BA.debugLineNum = 336;BA.debugLine="LOAD_COPY_YEAR";
_load_copy_year();
 //BA.debugLineNum = 337;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 338;BA.debugLine="CMB_COPY_YEAR.SelectedIndex = -1";
parent.mostCurrent._cmb_copy_year._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 339;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 340;BA.debugLine="OpenSpinner(CMB_COPY_YEAR.cmbBox)";
_openspinner(parent.mostCurrent._cmb_copy_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 341;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _button_delete_click() throws Exception{
ResumableSub_BUTTON_DELETE_Click rsub = new ResumableSub_BUTTON_DELETE_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_BUTTON_DELETE_Click extends BA.ResumableSub {
public ResumableSub_BUTTON_DELETE_Click(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;
int _result = 0;
String _insert_query = "";
String _query = "";
String _query2 = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 307;BA.debugLine="Msgbox2Async(\"Are you sure you want to delete thi";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to delete this template? All data in this template will be lost."),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 308;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 9;
return;
case 9:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 309;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 1:
//if
this.state = 8;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 310;BA.debugLine="Dim insert_query As String = \"INSERT INTO daily_";
_insert_query = "INSERT INTO daily_template_table_trail SELECT *,? as 'edit_by','DELETED' as 'edit_type',? as 'edit_date_time' from daily_template_table WHERE group_id = ?";
 //BA.debugLineNum = 311;BA.debugLine="connection.ExecNonQuery2(insert_query,Array As S";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._login_module._username /*String*/ ,anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._daily_inventory_module._group_id /*String*/ }));
 //BA.debugLineNum = 312;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 10;
return;
case 10:
//C
this.state = 4;
;
 //BA.debugLineNum = 313;BA.debugLine="Dim query As String = \"DELETE FROM daily_templat";
_query = "DELETE FROM daily_template_table WHERE group_id = ?";
 //BA.debugLineNum = 314;BA.debugLine="connection.ExecNonQuery2(query,Array As String(D";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._daily_inventory_module._group_id /*String*/ }));
 //BA.debugLineNum = 315;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 11;
return;
case 11:
//C
this.state = 4;
;
 //BA.debugLineNum = 316;BA.debugLine="Dim query2 As String = \"DELETE FROM daily_invent";
_query2 = "DELETE FROM daily_inventory_ref_table WHERE group_id = ?";
 //BA.debugLineNum = 317;BA.debugLine="connection.ExecNonQuery2(query2,Array As String(";
parent._connection.ExecNonQuery2(_query2,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._daily_inventory_module._group_id /*String*/ }));
 //BA.debugLineNum = 318;BA.debugLine="ToastMessageShow(\"Deleted Successfully\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Deleted Successfully"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 319;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 12;
return;
case 12:
//C
this.state = 4;
;
 //BA.debugLineNum = 320;BA.debugLine="Msgbox2Async(\"Template deleted successfully.\", \"";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Template deleted successfully."),BA.ObjectToCharSequence("Notice"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 321;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 4;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 322;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 323;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 324;BA.debugLine="StartActivity(DAILY_INVENTORY_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._daily_inventory_module.getObject()));
 //BA.debugLineNum = 325;BA.debugLine="SetAnimation(\"left_to_center\", \"center_to_right";
_setanimation("left_to_center","center_to_right");
 //BA.debugLineNum = 326;BA.debugLine="DAILY_INVENTORY_MODULE.clear_trigger = 1";
parent.mostCurrent._daily_inventory_module._clear_trigger /*String*/  = BA.NumberToString(1);
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
 //BA.debugLineNum = 329;BA.debugLine="End Sub";
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
public ResumableSub_BUTTON_SAVE_Click(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;
int _result = 0;
int _i = 0;
int _x = 0;
String _insert_query = "";
int step12;
int limit12;
int step17;
int limit17;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 373;BA.debugLine="If CMB_COPY_YEAR.SelectedIndex = CMB_COPY_YEAR.cm";
if (true) break;

case 1:
//if
this.state = 44;
if (parent.mostCurrent._cmb_copy_year._getselectedindex /*int*/ ()==parent.mostCurrent._cmb_copy_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("") || parent.mostCurrent._cmb_copy_month._getselectedindex /*int*/ ()==parent.mostCurrent._cmb_copy_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .IndexOf("")) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 44;
 //BA.debugLineNum = 374;BA.debugLine="Msgbox2Async(\"Please choose year and month. Cann";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please choose year and month. Cannot proceed."),BA.ObjectToCharSequence("Warning"),"OK","","",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 376;BA.debugLine="If CMB_COPY_YEAR.SelectedIndex = -1 Then CMB_COP";
if (true) break;

case 6:
//if
this.state = 11;
if (parent.mostCurrent._cmb_copy_year._getselectedindex /*int*/ ()==-1) { 
this.state = 8;
;}if (true) break;

case 8:
//C
this.state = 11;
parent.mostCurrent._cmb_copy_year._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 11:
//C
this.state = 12;
;
 //BA.debugLineNum = 377;BA.debugLine="If CMB_COPY_MONTH.SelectedIndex = -1 Then CMB_CO";
if (true) break;

case 12:
//if
this.state = 17;
if (parent.mostCurrent._cmb_copy_month._getselectedindex /*int*/ ()==-1) { 
this.state = 14;
;}if (true) break;

case 14:
//C
this.state = 17;
parent.mostCurrent._cmb_copy_month._setselectedindex /*int*/ ((int) (0));
if (true) break;

case 17:
//C
this.state = 18;
;
 //BA.debugLineNum = 379;BA.debugLine="Msgbox2Async(\"Are you sure you want to update th";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Are you sure you want to update this template? This will change your data in this template."),BA.ObjectToCharSequence("Warning"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 380;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 45;
return;
case 45:
//C
this.state = 18;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 381;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
if (true) break;

case 18:
//if
this.state = 43;
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 20;
}else {
this.state = 42;
}if (true) break;

case 20:
//C
this.state = 21;
 //BA.debugLineNum = 382;BA.debugLine="ProgressDialogShow2(\"Copying products to templa";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Copying products to template..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 383;BA.debugLine="cursor6 = connection.ExecQuery(\"SELECT group_id";
parent._cursor6 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT group_id FROM daily_template_table WHERE group_name = '"+parent.mostCurrent._label_load_copy_group.getText()+"' and year = '"+parent.mostCurrent._cmb_copy_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and month = '"+parent.mostCurrent._cmb_copy_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"'")));
 //BA.debugLineNum = 384;BA.debugLine="If cursor6.RowCount > 0 Then";
if (true) break;

case 21:
//if
this.state = 40;
if (parent._cursor6.getRowCount()>0) { 
this.state = 23;
}else {
this.state = 39;
}if (true) break;

case 23:
//C
this.state = 24;
 //BA.debugLineNum = 385;BA.debugLine="For i = 0 To cursor6.RowCount - 1";
if (true) break;

case 24:
//for
this.state = 37;
step12 = 1;
limit12 = (int) (parent._cursor6.getRowCount()-1);
_i = (int) (0) ;
this.state = 46;
if (true) break;

case 46:
//C
this.state = 37;
if ((step12 > 0 && _i <= limit12) || (step12 < 0 && _i >= limit12)) this.state = 26;
if (true) break;

case 47:
//C
this.state = 46;
_i = ((int)(0 + _i + step12)) ;
if (true) break;

case 26:
//C
this.state = 27;
 //BA.debugLineNum = 386;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 48;
return;
case 48:
//C
this.state = 27;
;
 //BA.debugLineNum = 387;BA.debugLine="cursor6.Position = i";
parent._cursor6.setPosition(_i);
 //BA.debugLineNum = 388;BA.debugLine="cursor5 = connection.ExecQuery(\"SELECT * FROM";
parent._cursor5 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"+parent._cursor6.GetString("group_id")+"' AND product_id NOT IN (SELECT product_id FROM daily_inventory_ref_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"')")));
 //BA.debugLineNum = 389;BA.debugLine="If cursor5.RowCount > 0 Then";
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
 //BA.debugLineNum = 390;BA.debugLine="For x = 0 To cursor5.RowCount - 1";
if (true) break;

case 30:
//for
this.state = 33;
step17 = 1;
limit17 = (int) (parent._cursor5.getRowCount()-1);
_x = (int) (0) ;
this.state = 49;
if (true) break;

case 49:
//C
this.state = 33;
if ((step17 > 0 && _x <= limit17) || (step17 < 0 && _x >= limit17)) this.state = 32;
if (true) break;

case 50:
//C
this.state = 49;
_x = ((int)(0 + _x + step17)) ;
if (true) break;

case 32:
//C
this.state = 50;
 //BA.debugLineNum = 391;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 51;
return;
case 51:
//C
this.state = 50;
;
 //BA.debugLineNum = 392;BA.debugLine="cursor5.Position = x";
parent._cursor5.setPosition(_x);
 //BA.debugLineNum = 393;BA.debugLine="Dim insert_query As String = \"INSERT INTO d";
_insert_query = "INSERT INTO daily_inventory_ref_table VALUES (?,?,?,?,?,?,?,?,?,?)";
 //BA.debugLineNum = 394;BA.debugLine="connection.ExecNonQuery2(insert_query,Array";
parent._connection.ExecNonQuery2(_insert_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{parent.mostCurrent._daily_inventory_module._group_id /*String*/ ,parent._cursor5.GetString("principal_id"),parent._cursor5.GetString("principal_name"),parent._cursor5.GetString("product_id"),parent._cursor5.GetString("product_variant"),parent._cursor5.GetString("product_description"),anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow()),parent.mostCurrent._login_module._tab_id /*String*/ ,parent.mostCurrent._login_module._username /*String*/ }));
 //BA.debugLineNum = 397;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 52;
return;
case 52:
//C
this.state = 50;
;
 if (true) break;
if (true) break;

case 33:
//C
this.state = 36;
;
 if (true) break;

case 35:
//C
this.state = 36;
 if (true) break;

case 36:
//C
this.state = 47;
;
 if (true) break;
if (true) break;

case 37:
//C
this.state = 40;
;
 //BA.debugLineNum = 403;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 404;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 53;
return;
case 53:
//C
this.state = 40;
;
 //BA.debugLineNum = 405;BA.debugLine="ToastMessageShow(\"Copied Successfully\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Copied Successfully"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 406;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 54;
return;
case 54:
//C
this.state = 40;
;
 //BA.debugLineNum = 407;BA.debugLine="PANEL_BG_COPY.SetVisibleAnimated(300,False)";
parent.mostCurrent._panel_bg_copy.SetVisibleAnimated((int) (300),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 408;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 55;
return;
case 55:
//C
this.state = 40;
;
 //BA.debugLineNum = 409;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 if (true) break;

case 39:
//C
this.state = 40;
 if (true) break;

case 40:
//C
this.state = 43;
;
 if (true) break;

case 42:
//C
this.state = 43;
 if (true) break;

case 43:
//C
this.state = 44;
;
 if (true) break;

case 44:
//C
this.state = -1;
;
 //BA.debugLineNum = 418;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _cmb_copy_year_selectedindexchanged(int _index) throws Exception{
ResumableSub_CMB_COPY_YEAR_SelectedIndexChanged rsub = new ResumableSub_CMB_COPY_YEAR_SelectedIndexChanged(null,_index);
rsub.resume(processBA, null);
}
public static class ResumableSub_CMB_COPY_YEAR_SelectedIndexChanged extends BA.ResumableSub {
public ResumableSub_CMB_COPY_YEAR_SelectedIndexChanged(wingan.app.daily_template parent,int _index) {
this.parent = parent;
this._index = _index;
}
wingan.app.daily_template parent;
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
 //BA.debugLineNum = 420;BA.debugLine="LOAD_COPY_MONTH";
_load_copy_month();
 //BA.debugLineNum = 421;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 422;BA.debugLine="CMB_COPY_MONTH.SelectedIndex = -1";
parent.mostCurrent._cmb_copy_month._setselectedindex /*int*/ ((int) (-1));
 //BA.debugLineNum = 423;BA.debugLine="Sleep(10)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (10));
this.state = 2;
return;
case 2:
//C
this.state = -1;
;
 //BA.debugLineNum = 424;BA.debugLine="OpenSpinner(CMB_COPY_MONTH.cmbBox)";
_openspinner(parent.mostCurrent._cmb_copy_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ );
 //BA.debugLineNum = 425;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _get_template() throws Exception{
ResumableSub_GET_TEMPLATE rsub = new ResumableSub_GET_TEMPLATE(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_GET_TEMPLATE extends BA.ResumableSub {
public ResumableSub_GET_TEMPLATE(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;
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
 //BA.debugLineNum = 162;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM dai";
parent._cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_template_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"'")));
 //BA.debugLineNum = 163;BA.debugLine="If cursor1.RowCount > 0 Then";
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
 //BA.debugLineNum = 164;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
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
 //BA.debugLineNum = 165;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 166;BA.debugLine="cursor1.Position = i";
parent._cursor1.setPosition(_i);
 //BA.debugLineNum = 167;BA.debugLine="LABEL_LOAD_GROUP.Text = cursor1.GetString(\"grou";
parent.mostCurrent._label_load_group.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("group_name")));
 //BA.debugLineNum = 168;BA.debugLine="LABEL_LOAD_YEAR.Text = cursor1.GetString(\"year\"";
parent.mostCurrent._label_load_year.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("year")));
 //BA.debugLineNum = 169;BA.debugLine="LABEL_LOAD_MONTH.Text = cursor1.GetString(\"mont";
parent.mostCurrent._label_load_month.setText(BA.ObjectToCharSequence(parent._cursor1.GetString("month")));
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
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 26;BA.debugLine="Dim ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 27;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private XSelections As B4XTableSelections";
mostCurrent._xselections = new wingan.app.b4xtableselections();
 //BA.debugLineNum = 31;BA.debugLine="Private NameColumn(3) As B4XTableColumn";
mostCurrent._namecolumn = new wingan.app.b4xtable._b4xtablecolumn[(int) (3)];
{
int d0 = mostCurrent._namecolumn.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._namecolumn[i0] = new wingan.app.b4xtable._b4xtablecolumn();
}
}
;
 //BA.debugLineNum = 33;BA.debugLine="Private Dialog As B4XDialog";
mostCurrent._dialog = new wingan.app.b4xdialog();
 //BA.debugLineNum = 34;BA.debugLine="Private Base As B4XView";
mostCurrent._base = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private SearchTemplate As B4XSearchTemplate";
mostCurrent._searchtemplate = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 36;BA.debugLine="Private SearchTemplate2 As B4XSearchTemplate";
mostCurrent._searchtemplate2 = new wingan.app.b4xsearchtemplate();
 //BA.debugLineNum = 38;BA.debugLine="Private cvs As B4XCanvas";
mostCurrent._cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 39;BA.debugLine="Private xui As XUI";
mostCurrent._xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 40;BA.debugLine="Private LABEL_LOAD_GROUP As Label";
mostCurrent._label_load_group = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private PANEL_TEMPLATE As Panel";
mostCurrent._panel_template = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private PRODUCT_TEMPLATE_TABLE As B4XTable";
mostCurrent._product_template_table = new wingan.app.b4xtable();
 //BA.debugLineNum = 43;BA.debugLine="Private LABEL_LOAD_MONTH As Label";
mostCurrent._label_load_month = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private LABEL_LOAD_YEAR As Label";
mostCurrent._label_load_year = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private BUTTON_ADD As Button";
mostCurrent._button_add = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private PANEL_BG_COPY As Panel";
mostCurrent._panel_bg_copy = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private LABEL_LOAD_COPY_GROUP As Label";
mostCurrent._label_load_copy_group = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private CMB_COPY_YEAR As B4XComboBox";
mostCurrent._cmb_copy_year = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 49;BA.debugLine="Private CMB_COPY_MONTH As B4XComboBox";
mostCurrent._cmb_copy_month = new wingan.app.b4xcombobox();
 //BA.debugLineNum = 50;BA.debugLine="Private PANEL_COPY As Panel";
mostCurrent._panel_copy = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private BUTTON_SAVE As Button";
mostCurrent._button_save = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static void  _load_copy_month() throws Exception{
ResumableSub_LOAD_COPY_MONTH rsub = new ResumableSub_LOAD_COPY_MONTH(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_COPY_MONTH extends BA.ResumableSub {
public ResumableSub_LOAD_COPY_MONTH(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;
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
 //BA.debugLineNum = 357;BA.debugLine="CMB_COPY_MONTH.cmbBox.Clear";
parent.mostCurrent._cmb_copy_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 358;BA.debugLine="cursor3 = connection.ExecQuery(\"SELECT group_id,";
parent._cursor3 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT group_id, month FROM daily_template_table WHERE group_name = '"+parent.mostCurrent._label_load_copy_group.getText()+"' and year = '"+parent.mostCurrent._cmb_copy_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .getSelectedItem()+"' and month <> '"+parent.mostCurrent._label_load_month.getText()+"' ORDER BY group_id ASC")));
 //BA.debugLineNum = 359;BA.debugLine="If cursor3.RowCount > 0 Then";
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
 //BA.debugLineNum = 360;BA.debugLine="For i = 0 To cursor3.RowCount - 1";
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
 //BA.debugLineNum = 361;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 362;BA.debugLine="cursor3.Position = i";
parent._cursor3.setPosition(_i);
 //BA.debugLineNum = 363;BA.debugLine="CMB_COPY_MONTH.cmbBox.Add(cursor3.GetString(\"mo";
parent.mostCurrent._cmb_copy_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor3.GetString("month"));
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
 //BA.debugLineNum = 368;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_copy_year() throws Exception{
ResumableSub_LOAD_COPY_YEAR rsub = new ResumableSub_LOAD_COPY_YEAR(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_COPY_YEAR extends BA.ResumableSub {
public ResumableSub_LOAD_COPY_YEAR(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;
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
 //BA.debugLineNum = 343;BA.debugLine="CMB_COPY_YEAR.cmbBox.Clear";
parent.mostCurrent._cmb_copy_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 344;BA.debugLine="CMB_COPY_MONTH.cmbBox.Clear";
parent.mostCurrent._cmb_copy_month._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Clear();
 //BA.debugLineNum = 345;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT year FROM";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT year FROM daily_template_table WHERE group_name = '"+parent.mostCurrent._label_load_group.getText()+"' GROUP BY year ORDER BY year ASC")));
 //BA.debugLineNum = 346;BA.debugLine="If cursor2.RowCount > 0 Then";
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
 //BA.debugLineNum = 347;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
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
 //BA.debugLineNum = 348;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 349;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 350;BA.debugLine="CMB_COPY_YEAR.cmbBox.Add(cursor2.GetString(\"yea";
parent.mostCurrent._cmb_copy_year._cmbbox /*anywheresoftware.b4a.objects.SpinnerWrapper*/ .Add(parent._cursor2.GetString("year"));
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
 //BA.debugLineNum = 355;BA.debugLine="End Sub";
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
public ResumableSub_LOAD_INVENTORY_TABLE(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;
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
 //BA.debugLineNum = 182;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 1;
;
 //BA.debugLineNum = 183;BA.debugLine="Dim Data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 184;BA.debugLine="Data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 185;BA.debugLine="Dim rs As ResultSet = connection.ExecQuery(\"SELEC";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"+parent.mostCurrent._daily_inventory_module._group_id /*String*/ +"' ORDER BY principal_name, product_variant, product_description ASC")));
 //BA.debugLineNum = 186;BA.debugLine="Do While rs.NextRow";
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
 //BA.debugLineNum = 187;BA.debugLine="Dim row(3) As Object";
_row = new Object[(int) (3)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 188;BA.debugLine="row(0) = rs.GetString(\"principal_name\")";
_row[(int) (0)] = (Object)(_rs.GetString("principal_name"));
 //BA.debugLineNum = 189;BA.debugLine="row(1) = rs.GetString(\"product_variant\")";
_row[(int) (1)] = (Object)(_rs.GetString("product_variant"));
 //BA.debugLineNum = 190;BA.debugLine="row(2) = rs.GetString(\"product_description\")";
_row[(int) (2)] = (Object)(_rs.GetString("product_description"));
 //BA.debugLineNum = 191;BA.debugLine="Data.Add(row)";
_data.Add((Object)(_row));
 if (true) break;

case 4:
//C
this.state = 5;
;
 //BA.debugLineNum = 193;BA.debugLine="rs.Close";
_rs.Close();
 //BA.debugLineNum = 194;BA.debugLine="PRODUCT_TEMPLATE_TABLE.SetData(Data)";
parent.mostCurrent._product_template_table._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 195;BA.debugLine="If XSelections.IsInitialized = False Then";
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
 //BA.debugLineNum = 196;BA.debugLine="XSelections.Initialize(PRODUCT_TEMPLATE_TABLE)";
parent.mostCurrent._xselections._initialize /*String*/ (mostCurrent.activityBA,parent.mostCurrent._product_template_table);
 //BA.debugLineNum = 197;BA.debugLine="XSelections.Mode = XSelections.MODE_SINGLE_LINE_";
parent.mostCurrent._xselections._setmode /*int*/ (parent.mostCurrent._xselections._mode_single_line_permanent /*int*/ );
 if (true) break;

case 8:
//C
this.state = -1;
;
 //BA.debugLineNum = 199;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _load_product() throws Exception{
ResumableSub_LOAD_PRODUCT rsub = new ResumableSub_LOAD_PRODUCT(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_LOAD_PRODUCT extends BA.ResumableSub {
public ResumableSub_LOAD_PRODUCT(wingan.app.daily_template parent) {
this.parent = parent;
}
wingan.app.daily_template parent;
anywheresoftware.b4a.objects.collections.List _items = null;
int _i = 0;
int step7;
int limit7;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 248;BA.debugLine="SearchTemplate2.CustomListView1.Clear";
parent.mostCurrent._searchtemplate2._customlistview1 /*b4a.example3.customlistview*/ ._clear();
 //BA.debugLineNum = 249;BA.debugLine="Dialog.Title = \"Find Product\"";
parent.mostCurrent._dialog._title /*Object*/  = (Object)("Find Product");
 //BA.debugLineNum = 250;BA.debugLine="Dim Items As List";
_items = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 251;BA.debugLine="Items.Initialize";
_items.Initialize();
 //BA.debugLineNum = 252;BA.debugLine="Items.Clear";
_items.Clear();
 //BA.debugLineNum = 253;BA.debugLine="cursor2 = connection.ExecQuery(\"SELECT * FROM pro";
parent._cursor2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(parent._connection.ExecQuery("SELECT * FROM product_table WHERE flag_deleted = '0' ORDER BY product_desc ASC")));
 //BA.debugLineNum = 254;BA.debugLine="For i = 0 To cursor2.RowCount - 1";
if (true) break;

case 1:
//for
this.state = 4;
step7 = 1;
limit7 = (int) (parent._cursor2.getRowCount()-1);
_i = (int) (0) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step7 > 0 && _i <= limit7) || (step7 < 0 && _i >= limit7)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_i = ((int)(0 + _i + step7)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 255;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 256;BA.debugLine="cursor2.Position = i";
parent._cursor2.setPosition(_i);
 //BA.debugLineNum = 257;BA.debugLine="Items.Add(cursor2.GetString(\"product_desc\"))";
_items.Add((Object)(parent._cursor2.GetString("product_desc")));
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 259;BA.debugLine="SearchTemplate2.SetItems(Items)";
parent.mostCurrent._searchtemplate2._setitems /*Object*/ (_items);
 //BA.debugLineNum = 260;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _load_table_header() throws Exception{
 //BA.debugLineNum = 176;BA.debugLine="Sub LOAD_TABLE_HEADER";
 //BA.debugLineNum = 177;BA.debugLine="NameColumn(0)=PRODUCT_TEMPLATE_TABLE.AddColumn(\"P";
mostCurrent._namecolumn[(int) (0)] = mostCurrent._product_template_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Principal",mostCurrent._product_template_table._column_type_text /*int*/ );
 //BA.debugLineNum = 178;BA.debugLine="NameColumn(1)=PRODUCT_TEMPLATE_TABLE.AddColumn(\"P";
mostCurrent._namecolumn[(int) (1)] = mostCurrent._product_template_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Variant",mostCurrent._product_template_table._column_type_text /*int*/ );
 //BA.debugLineNum = 179;BA.debugLine="NameColumn(2)=PRODUCT_TEMPLATE_TABLE.AddColumn(\"P";
mostCurrent._namecolumn[(int) (2)] = mostCurrent._product_template_table._addcolumn /*wingan.app.b4xtable._b4xtablecolumn*/ ("Product Description",mostCurrent._product_template_table._column_type_text /*int*/ );
 //BA.debugLineNum = 180;BA.debugLine="End Sub";
return "";
}
public static String  _openbutton(anywheresoftware.b4a.objects.ButtonWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 155;BA.debugLine="Sub OpenButton(se As Button)";
 //BA.debugLineNum = 156;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 157;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 158;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return "";
}
public static String  _openspinner(anywheresoftware.b4a.objects.SpinnerWrapper _se) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _reflect = null;
 //BA.debugLineNum = 149;BA.debugLine="Sub OpenSpinner(se As Spinner)";
 //BA.debugLineNum = 150;BA.debugLine="Dim reflect As Reflector";
_reflect = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 151;BA.debugLine="reflect.Target = se";
_reflect.Target = (Object)(_se.getObject());
 //BA.debugLineNum = 152;BA.debugLine="reflect.RunMethod(\"performClick\")";
_reflect.RunMethod("performClick");
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
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
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _product_template_table_cellclicked(String _columnid,long _rowid) throws Exception{
 //BA.debugLineNum = 220;BA.debugLine="Sub PRODUCT_TEMPLATE_TABLE_CellClicked (ColumnId A";
 //BA.debugLineNum = 221;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 222;BA.debugLine="Log(ColumnId & RowId)";
anywheresoftware.b4a.keywords.Common.LogImpl("779298562",_columnid+BA.NumberToString(_rowid),0);
 //BA.debugLineNum = 223;BA.debugLine="End Sub";
return "";
}
public static void  _product_template_table_celllongclicked(String _columnid,long _rowid) throws Exception{
ResumableSub_PRODUCT_TEMPLATE_TABLE_CellLongClicked rsub = new ResumableSub_PRODUCT_TEMPLATE_TABLE_CellLongClicked(null,_columnid,_rowid);
rsub.resume(processBA, null);
}
public static class ResumableSub_PRODUCT_TEMPLATE_TABLE_CellLongClicked extends BA.ResumableSub {
public ResumableSub_PRODUCT_TEMPLATE_TABLE_CellLongClicked(wingan.app.daily_template parent,String _columnid,long _rowid) {
this.parent = parent;
this._columnid = _columnid;
this._rowid = _rowid;
}
wingan.app.daily_template parent;
String _columnid;
long _rowid;
anywheresoftware.b4a.objects.collections.Map _rowdata = null;
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
 //BA.debugLineNum = 225;BA.debugLine="XSelections.CellClicked(ColumnId, RowId)";
parent.mostCurrent._xselections._cellclicked /*String*/ (_columnid,_rowid);
 //BA.debugLineNum = 226;BA.debugLine="Log(ColumnId & RowId)";
anywheresoftware.b4a.keywords.Common.LogImpl("779364098",_columnid+BA.NumberToString(_rowid),0);
 //BA.debugLineNum = 228;BA.debugLine="Dim RowData As Map = PRODUCT_TEMPLATE_TABLE.GetRo";
_rowdata = new anywheresoftware.b4a.objects.collections.Map();
_rowdata = parent.mostCurrent._product_template_table._getrow /*anywheresoftware.b4a.objects.collections.Map*/ (_rowid);
 //BA.debugLineNum = 230;BA.debugLine="Msgbox2Async(\"Do you want to delete this product";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Do you want to delete this product of this template?"),BA.ObjectToCharSequence("Notice"),"YES","","CANCEL",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 231;BA.debugLine="Wait For Msgbox_Result (Result As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_result = (Integer) result[0];
;
 //BA.debugLineNum = 232;BA.debugLine="If Result = DialogResponse.POSITIVE Then";
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
 //BA.debugLineNum = 233;BA.debugLine="Dim query As String = \"DELETE from daily_invento";
_query = "DELETE from daily_inventory_ref_table WHERE product_description = ?";
 //BA.debugLineNum = 234;BA.debugLine="connection.ExecNonQuery2(query,Array As String(R";
parent._connection.ExecNonQuery2(_query,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{BA.ObjectToString(_rowdata.Get((Object)("Product Description")))}));
 //BA.debugLineNum = 235;BA.debugLine="ProgressDialogShow2(\"Deleting...\", False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Deleting..."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 236;BA.debugLine="Sleep(1500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1500));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 237;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 238;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 9;
return;
case 9:
//C
this.state = 6;
;
 //BA.debugLineNum = 239;BA.debugLine="ToastMessageShow(\"Product Deleted\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Product Deleted"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 240;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 10;
return;
case 10:
//C
this.state = 6;
;
 //BA.debugLineNum = 241;BA.debugLine="LOAD_INVENTORY_TABLE";
_load_inventory_table();
 if (true) break;

case 5:
//C
this.state = 6;
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 245;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _product_template_table_dataupdated() throws Exception{
boolean _shouldrefresh = false;
wingan.app.b4xtable._b4xtablecolumn _column = null;
int _maxwidth = 0;
int _i = 0;
anywheresoftware.b4a.objects.B4XViewWrapper _pnl = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
 //BA.debugLineNum = 200;BA.debugLine="Sub PRODUCT_TEMPLATE_TABLE_DataUpdated";
 //BA.debugLineNum = 201;BA.debugLine="Dim ShouldRefresh As Boolean";
_shouldrefresh = false;
 //BA.debugLineNum = 202;BA.debugLine="For Each Column As B4XTableColumn In Array (NameC";
{
final Object[] group2 = new Object[]{(Object)(mostCurrent._namecolumn[(int) (0)]),(Object)(mostCurrent._namecolumn[(int) (1)]),(Object)(mostCurrent._namecolumn[(int) (2)])};
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_column = (wingan.app.b4xtable._b4xtablecolumn)(group2[index2]);
 //BA.debugLineNum = 203;BA.debugLine="Dim MaxWidth As Int";
_maxwidth = 0;
 //BA.debugLineNum = 204;BA.debugLine="For i = 0 To PRODUCT_TEMPLATE_TABLE.VisibleRowId";
{
final int step4 = 1;
final int limit4 = mostCurrent._product_template_table._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize();
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 205;BA.debugLine="Dim pnl As B4XView = Column.CellsLayouts.Get(i)";
_pnl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_pnl = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_column.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i)));
 //BA.debugLineNum = 206;BA.debugLine="Dim lbl As B4XView = pnl.GetView(0)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _pnl.GetView((int) (0));
 //BA.debugLineNum = 207;BA.debugLine="MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Te";
_maxwidth = (int) (anywheresoftware.b4a.keywords.Common.Max(_maxwidth,mostCurrent._cvs.MeasureText(_lbl.getText(),_lbl.getFont()).getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 }
};
 //BA.debugLineNum = 209;BA.debugLine="If MaxWidth > Column.ComputedWidth Or MaxWidth <";
if (_maxwidth>_column.ComputedWidth /*int*/  || _maxwidth<_column.ComputedWidth /*int*/ -anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))) { 
 //BA.debugLineNum = 210;BA.debugLine="Column.Width = MaxWidth + 10dip";
_column.Width /*int*/  = (int) (_maxwidth+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 211;BA.debugLine="ShouldRefresh = True";
_shouldrefresh = anywheresoftware.b4a.keywords.Common.True;
 };
 }
};
 //BA.debugLineNum = 214;BA.debugLine="If ShouldRefresh Then";
if (_shouldrefresh) { 
 //BA.debugLineNum = 215;BA.debugLine="PRODUCT_TEMPLATE_TABLE.Refresh";
mostCurrent._product_template_table._refresh /*String*/ ();
 //BA.debugLineNum = 216;BA.debugLine="XSelections.Clear";
mostCurrent._xselections._clear /*String*/ ();
 //BA.debugLineNum = 217;BA.debugLine="XSelections.Refresh";
mostCurrent._xselections._refresh /*String*/ ();
 };
 //BA.debugLineNum = 219;BA.debugLine="End Sub";
return "";
}
public static String  _setanimation(String _inanimation,String _outanimation) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
String _package = "";
int _in = 0;
int _out = 0;
 //BA.debugLineNum = 125;BA.debugLine="Sub SetAnimation(InAnimation As String, OutAnimati";
 //BA.debugLineNum = 126;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 127;BA.debugLine="Dim package As String";
_package = "";
 //BA.debugLineNum = 128;BA.debugLine="Dim IN As Int";
_in = 0;
 //BA.debugLineNum = 129;BA.debugLine="Dim out As Int";
_out = 0;
 //BA.debugLineNum = 130;BA.debugLine="package = r.GetStaticField(\"anywheresoftware.b4a.";
_package = BA.ObjectToString(_r.GetStaticField("anywheresoftware.b4a.BA","packageName"));
 //BA.debugLineNum = 131;BA.debugLine="IN = r.GetStaticField(package & \".R$anim\", InAnim";
_in = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_inanimation)));
 //BA.debugLineNum = 132;BA.debugLine="out = r.GetStaticField(package & \".R$anim\", OutAn";
_out = (int)(BA.ObjectToNumber(_r.GetStaticField(_package+".R$anim",_outanimation)));
 //BA.debugLineNum = 133;BA.debugLine="r.Target = r.GetActivity";
_r.Target = (Object)(_r.GetActivity(processBA));
 //BA.debugLineNum = 134;BA.debugLine="r.RunMethod4(\"overridePendingTransition\", Array A";
_r.RunMethod4("overridePendingTransition",new Object[]{(Object)(_in),(Object)(_out)},new String[]{"java.lang.int","java.lang.int"});
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
}
