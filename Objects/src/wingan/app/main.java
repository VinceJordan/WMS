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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "wingan.app", "wingan.app.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "wingan.app", "wingan.app.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "wingan.app.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
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
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
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
public static String _last_login = "";
public static anywheresoftware.b4a.objects.Timer _splash_timer = null;
public static anywheresoftware.b4a.sql.SQL _connection = null;
public static String _rdclink = "";
public static int _fade_count = 0;
public b4a.example.dateutils _dateutils = null;
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
public wingan.app.receiving_module _receiving_module = null;
public wingan.app.receiving2_module _receiving2_module = null;
public wingan.app.scale_calc _scale_calc = null;
public wingan.app.starter _starter = null;
public wingan.app.httputils2service _httputils2service = null;
public wingan.app.b4xcollections _b4xcollections = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (login_module.mostCurrent != null);
vis = vis | (dashboard_module.mostCurrent != null);
vis = vis | (return_module.mostCurrent != null);
vis = vis | (cancelled_module.mostCurrent != null);
vis = vis | (sales_return_module.mostCurrent != null);
vis = vis | (preparing_module.mostCurrent != null);
vis = vis | (loading_module.mostCurrent != null);
vis = vis | (daily_count.mostCurrent != null);
vis = vis | (inventory_table.mostCurrent != null);
vis = vis | (montlhy_inventory2_module.mostCurrent != null);
vis = vis | (price_checking_module.mostCurrent != null);
vis = vis | (expiration_module.mostCurrent != null);
vis = vis | (daily_inventory_module.mostCurrent != null);
vis = vis | (daily_template.mostCurrent != null);
vis = vis | (database_module.mostCurrent != null);
vis = vis | (monthly_inventory_module.mostCurrent != null);
vis = vis | (monthlyinv_module.mostCurrent != null);
vis = vis | (receiving_module.mostCurrent != null);
vis = vis | (receiving2_module.mostCurrent != null);
return vis;}
public static class _dbresult{
public boolean IsInitialized;
public Object Tag;
public anywheresoftware.b4a.objects.collections.Map Columns;
public anywheresoftware.b4a.objects.collections.List Rows;
public void Initialize() {
IsInitialized = true;
Tag = new Object();
Columns = new anywheresoftware.b4a.objects.collections.Map();
Rows = new anywheresoftware.b4a.objects.collections.List();
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _dbcommand{
public boolean IsInitialized;
public String Name;
public Object[] Parameters;
public void Initialize() {
IsInitialized = true;
Name = "";
Parameters = new Object[0];
{
int d0 = Parameters.length;
for (int i0 = 0;i0 < d0;i0++) {
Parameters[i0] = new Object();
}
}
;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_afterchange() throws Exception{
 //BA.debugLineNum = 62;BA.debugLine="Public Sub Activity_AfterChange";
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 49;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 50;BA.debugLine="Activity.LoadLayout(\"DESIGNER\")";
mostCurrent._activity.LoadLayout("DESIGNER",mostCurrent.activityBA);
 //BA.debugLineNum = 51;BA.debugLine="SPLASH_TIMER.Initialize(\"SPLASH_TIMER\", 50)";
_splash_timer.Initialize(processBA,"SPLASH_TIMER",(long) (50));
 //BA.debugLineNum = 52;BA.debugLine="SPLASH_TIMER.Enabled = True";
_splash_timer.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 53;BA.debugLine="FADE_COUNT = 0";
_fade_count = (int) (0);
 //BA.debugLineNum = 54;BA.debugLine="DateTime.TimeFormat = \"hh:mm a\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("hh:mm a");
 //BA.debugLineNum = 55;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 66;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _add_cancelled_invoice_disc() throws Exception{
 //BA.debugLineNum = 182;BA.debugLine="Sub ADD_CANCELLED_INVOICE_DISC";
 //BA.debugLineNum = 183;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","cancelled_invoice_disc_table","customer_id TEXT"+", customer_name TEXT, invoice_no TEXT, invoice_date TEXT, transaction_number TEXT,principal_id TEXT,principal_name TEXT, product_id TEXT, product_variant TEXT, product_description TEXT, unit TEXT"+", quantity TEXT, total_pieces TEXT, scan_code TEXT, input_reason TEXT, cancelled_reason TEXT, cancelled_id TEXT, date_registered TEXT, time_registered TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _add_cancelled_invoice_ref() throws Exception{
 //BA.debugLineNum = 176;BA.debugLine="Sub ADD_CANCELLED_INVOICE_REF";
 //BA.debugLineNum = 177;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","cancelled_invoice_ref_table","customer_id TEXT"+", customer_name TEXT, invoice_no TEXT, invoice_date TEXT, product_id TEXT, product_variant TEXT, product_description TEXT, unit TEXT"+", quantity TEXT, total_pieces TEXT, date_time_registered TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 180;BA.debugLine="End Sub";
return "";
}
public static String  _add_cancelled_upload() throws Exception{
 //BA.debugLineNum = 201;BA.debugLine="Sub ADD_CANCELLED_UPLOAD";
 //BA.debugLineNum = 202;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","cancelled_upload_table","invoice_no TEXT, upload_date TEXT"+", upload_time TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 204;BA.debugLine="End Sub";
return "";
}
public static String  _add_customer() throws Exception{
 //BA.debugLineNum = 206;BA.debugLine="Sub ADD_CUSTOMER";
 //BA.debugLineNum = 207;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","customer_table","customer_id TEXT, customer_name TEXT");
 //BA.debugLineNum = 208;BA.debugLine="End Sub";
return "";
}
public static String  _add_near_expiry() throws Exception{
 //BA.debugLineNum = 194;BA.debugLine="Sub ADD_NEAR_EXPIRY";
 //BA.debugLineNum = 195;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","near_expiry_table","invoice_no TEXT, transaction_number TEXT"+", principal_id TEXT,principal_name TEXT, product_id TEXT, product_variant TEXT, product_description TEXT, unit TEXT"+", quantity TEXT, total_pieces TEXT,month_expired TEXT,year_expired TEXT,date_expired TEXT,month_manufactured TEXT,year_manufactured TEXT"+", date_manufactured TEXT, scan_code TEXT, input_reason TEXT, date_registered TEXT, time_registered TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 199;BA.debugLine="End Sub";
return "";
}
public static String  _add_picklist_return_ref() throws Exception{
 //BA.debugLineNum = 170;BA.debugLine="Sub ADD_PICKLIST_RETURN_REF";
 //BA.debugLineNum = 171;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","picklist_return_ref_table","return_route_id TEXT ,picklist_id TEXT"+", picklist_name TEXT, route_name TEXT, plate_no TEXT, driver TEXT, helper1 TEXT, helper2 TEXT, helper3 TEXT"+", date_return TEXT, time_return TEXT, date_time_registered TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
return "";
}
public static String  _add_return_expiry() throws Exception{
 //BA.debugLineNum = 242;BA.debugLine="Sub ADD_RETURN_EXPIRY";
 //BA.debugLineNum = 243;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","sales_return_expiry_table","return_id TEXT, transaction_number TEXT"+", principal_id TEXT,principal_name TEXT, warehouse TEXT, product_id TEXT, product_variant TEXT, product_description TEXT, unit TEXT"+", quantity TEXT, total_pieces TEXT,month_expired TEXT,year_expired TEXT,date_expired TEXT,month_manufactured TEXT,year_manufactured TEXT"+", date_manufactured TEXT, scan_code TEXT, input_reason TEXT, date_registered TEXT, time_registered TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 247;BA.debugLine="End Sub";
return "";
}
public static String  _add_return_upload() throws Exception{
 //BA.debugLineNum = 237;BA.debugLine="Sub ADD_RETURN_UPLOAD";
 //BA.debugLineNum = 238;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","return_upload_table","return_id TEXT, upload_date TEXT"+", upload_time TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 240;BA.debugLine="End Sub";
return "";
}
public static String  _add_sales_return_disc() throws Exception{
 //BA.debugLineNum = 225;BA.debugLine="Sub ADD_SALES_RETURN_DISC";
 //BA.debugLineNum = 226;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","sales_return_disc_table","return_id TEXT, transaction_number TEXT"+", salesman_position_id TEXT, salesman_name TEXT, principal_id TEXT,principal_name TEXT, warehouse TEXT, product_id TEXT, product_variant TEXT, product_description TEXT, unit TEXT"+", quantity TEXT, total_pieces TEXT, scan_code TEXT, input_reason TEXT, return_reason TEXT, date_registered TEXT, time_registered TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return "";
}
public static String  _add_sales_return_disc_trail() throws Exception{
 //BA.debugLineNum = 231;BA.debugLine="Sub ADD_SALES_RETURN_DISC_TRAIL";
 //BA.debugLineNum = 232;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","sales_return_disc_table_trail","return_id TEXT, transaction_number TEXT"+", salesman_position_id TEXT, salesman_name TEXT, principal_id TEXT,principal_name TEXT, warehouse TEXT, product_id TEXT, product_variant TEXT, product_description TEXT, unit TEXT"+", quantity TEXT, total_pieces TEXT, scan_code TEXT, input_reason TEXT, return_reason TEXT, date_registered TEXT, time_registered TEXT, user_info TEXT, tab_id TEXT, date_modified TEXT, time_modified TEXT, modified_by TEXT, modified_type TEXT");
 //BA.debugLineNum = 235;BA.debugLine="End Sub";
return "";
}
public static String  _add_sales_return_ref() throws Exception{
 //BA.debugLineNum = 215;BA.debugLine="Sub ADD_SALES_RETURN_REF";
 //BA.debugLineNum = 216;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","sales_return_ref_table","return_route_id TEXT, return_id TEXT, return_date TEXT"+", return_number TEXT, customer_id TEXT, customer_name TEXT, date_registered TEXT, time_registered TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 218;BA.debugLine="End Sub";
return "";
}
public static String  _add_sales_return_ref_trail() throws Exception{
 //BA.debugLineNum = 220;BA.debugLine="Sub ADD_SALES_RETURN_REF_TRAIL";
 //BA.debugLineNum = 221;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","sales_return_ref_table_trail","return_route_id TEXT, return_id TEXT, return_date TEXT"+", return_number TEXT, customer_id TEXT, customer_name TEXT, date_registered TEXT, time_registered TEXT, user_info TEXT, tab_id TEXT, date_modified TEXT, time_modified TEXT, modified_by TEXT, modified_type TEXT");
 //BA.debugLineNum = 223;BA.debugLine="End Sub";
return "";
}
public static String  _add_salesman() throws Exception{
 //BA.debugLineNum = 210;BA.debugLine="Sub ADD_SALESMAN";
 //BA.debugLineNum = 211;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","salesman_table","employee_id TEXT, sales_position_id TEXT"+", sales_position_name TEXT, employee_name TEXT");
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return "";
}
public static String  _add_wrong_serve() throws Exception{
 //BA.debugLineNum = 188;BA.debugLine="Sub ADD_WRONG_SERVE";
 //BA.debugLineNum = 189;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",\"";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","wrong_served_table","invoice_no TEXT,cancelled_id TEXT"+", principal_id TEXT,principal_name TEXT, product_id TEXT, product_variant TEXT, product_description TEXT, unit TEXT"+", quantity TEXT, total_pieces TEXT, scan_code TEXT, input_reason TEXT, date_registered TEXT, time_registered TEXT, user_info TEXT, tab_id TEXT");
 //BA.debugLineNum = 192;BA.debugLine="End Sub";
return "";
}
public static boolean  _addtable(String _path,String _filename,String _nametable,String _fields) throws Exception{
anywheresoftware.b4a.sql.SQL _db = null;
String _s = "";
boolean _b = false;
 //BA.debugLineNum = 128;BA.debugLine="Sub AddTable(Path As String, FileName As String, N";
 //BA.debugLineNum = 131;BA.debugLine="Dim DB As SQL";
_db = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 132;BA.debugLine="Dim S As String";
_s = "";
 //BA.debugLineNum = 133;BA.debugLine="Dim B As Boolean = False";
_b = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 135;BA.debugLine="DB.Initialize(Path,FileName,False)";
_db.Initialize(_path,_filename,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 136;BA.debugLine="S=DB.ExecQuerySingleResult(\"SELECT name FROM sqli";
_s = _db.ExecQuerySingleResult("SELECT name FROM sqlite_master WHERE type='table' AND name='"+_nametable+"';");
 //BA.debugLineNum = 137;BA.debugLine="If S=Null Then";
if (_s== null) { 
 //BA.debugLineNum = 138;BA.debugLine="DB.ExecNonQuery(\"CREATE TABLE \" & NameTable &\" (";
_db.ExecNonQuery("CREATE TABLE "+_nametable+" ("+_fields+")");
 //BA.debugLineNum = 139;BA.debugLine="B=True";
_b = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 141;BA.debugLine="Return b";
if (true) return _b;
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return false;
}
public static String  _choose_token() throws Exception{
int _token = 0;
String _user = "";
int _row = 0;
 //BA.debugLineNum = 144;BA.debugLine="Sub CHOOSE_TOKEN";
 //BA.debugLineNum = 145;BA.debugLine="Dim TOKEN As Int";
_token = 0;
 //BA.debugLineNum = 146;BA.debugLine="Dim user As String";
_user = "";
 //BA.debugLineNum = 148;BA.debugLine="cursor1 = connection.ExecQuery(\"SELECT * FROM use";
_cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_connection.ExecQuery("SELECT * FROM user_token_table")));
 //BA.debugLineNum = 149;BA.debugLine="If cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 150;BA.debugLine="For row = 0 To cursor1.RowCount - 1";
{
final int step5 = 1;
final int limit5 = (int) (_cursor1.getRowCount()-1);
_row = (int) (0) ;
for (;_row <= limit5 ;_row = _row + step5 ) {
 //BA.debugLineNum = 151;BA.debugLine="cursor1.Position = row";
_cursor1.setPosition(_row);
 //BA.debugLineNum = 152;BA.debugLine="TOKEN = cursor1.GetString(\"token\")";
_token = (int)(Double.parseDouble(_cursor1.GetString("token")));
 //BA.debugLineNum = 153;BA.debugLine="user = cursor1.GetString(\"user\")";
_user = _cursor1.GetString("user");
 }
};
 //BA.debugLineNum = 155;BA.debugLine="If TOKEN = 1 Then";
if (_token==1) { 
 //BA.debugLineNum = 156;BA.debugLine="LOGIN_MODULE.username = user";
mostCurrent._login_module._username /*String*/  = _user;
 //BA.debugLineNum = 157;BA.debugLine="StartActivity(DASHBOARD_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._dashboard_module.getObject()));
 }else {
 //BA.debugLineNum = 159;BA.debugLine="StartActivity(LOGIN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._login_module.getObject()));
 };
 }else {
 //BA.debugLineNum = 162;BA.debugLine="StartActivity(LOGIN_MODULE)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._login_module.getObject()));
 };
 //BA.debugLineNum = 164;BA.debugLine="End Sub";
return "";
}
public static String  _create_tables() throws Exception{
 //BA.debugLineNum = 166;BA.debugLine="Sub CREATE_TABLES";
 //BA.debugLineNum = 168;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 46;BA.debugLine="Dim FADE_COUNT As Int";
_fade_count = 0;
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        b4a.example.dateutils._process_globals();
main._process_globals();
login_module._process_globals();
dashboard_module._process_globals();
return_module._process_globals();
cancelled_module._process_globals();
sales_return_module._process_globals();
preparing_module._process_globals();
loading_module._process_globals();
daily_count._process_globals();
inventory_table._process_globals();
montlhy_inventory2_module._process_globals();
price_checking_module._process_globals();
expiration_module._process_globals();
daily_inventory_module._process_globals();
daily_template._process_globals();
database_module._process_globals();
monthly_inventory_module._process_globals();
monthlyinv_module._process_globals();
number._process_globals();
receiving_module._process_globals();
receiving2_module._process_globals();
scale_calc._process_globals();
starter._process_globals();
httputils2service._process_globals();
b4xcollections._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="Private rp As RuntimePermissions";
_rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 22;BA.debugLine="Dim cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim cursor2 As Cursor";
_cursor2 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim cursor3 As Cursor";
_cursor3 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim last_login As String";
_last_login = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim SPLASH_TIMER As Timer";
_splash_timer = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 33;BA.debugLine="Dim connection As SQL";
_connection = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 36;BA.debugLine="Type DBResult (Tag As Object, Columns As Map, Row";
;
 //BA.debugLineNum = 37;BA.debugLine="Type DBCommand (Name As String, Parameters() As O";
;
 //BA.debugLineNum = 38;BA.debugLine="Public const rdclink As String = \"http://172.16.0";
_rdclink = "http://172.16.0.81:17180/rdc";
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public static void  _splash_end() throws Exception{
ResumableSub_Splash_End rsub = new ResumableSub_Splash_End(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Splash_End extends BA.ResumableSub {
public ResumableSub_Splash_End(wingan.app.main parent) {
this.parent = parent;
}
wingan.app.main parent;
String _permission = "";
boolean _result = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 77;BA.debugLine="SPLASH_TIMER.Enabled = False";
parent._splash_timer.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 78;BA.debugLine="rp.CheckAndRequest(rp.PERMISSION_READ_EXTERNAL_ST";
parent._rp.CheckAndRequest(processBA,parent._rp.PERMISSION_READ_EXTERNAL_STORAGE);
 //BA.debugLineNum = 79;BA.debugLine="Wait For Activity_PermissionResult (Permission As";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, this, null);
this.state = 15;
return;
case 15:
//C
this.state = 1;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 80;BA.debugLine="If Result Then";
if (true) break;

case 1:
//if
this.state = 14;
if (_result) { 
this.state = 3;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 81;BA.debugLine="Log(File.DirRootExternal & \"/WING AN APP/\")";
anywheresoftware.b4a.keywords.Common.LogImpl("793519877",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/",0);
 //BA.debugLineNum = 82;BA.debugLine="File.MakeDir(File.DirRootExternal,\"WING AN APP\")";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"WING AN APP");
 //BA.debugLineNum = 84;BA.debugLine="If File.Exists(File.DirRootExternal & \"/WING AN";
if (true) break;

case 4:
//if
this.state = 7;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 85;BA.debugLine="File.Copy(File.DirAssets,\"tablet_db.db\", File.D";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"tablet_db.db",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db");
 if (true) break;

case 7:
//C
this.state = 8;
;
 //BA.debugLineNum = 87;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 16;
return;
case 16:
//C
this.state = 8;
;
 //BA.debugLineNum = 88;BA.debugLine="If connection.IsInitialized = False Then";
if (true) break;

case 8:
//if
this.state = 11;
if (parent._connection.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 10;
}if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 89;BA.debugLine="connection.Initialize(File.DirRootExternal & \"/";
parent._connection.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db",anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 14;
;
 //BA.debugLineNum = 91;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 17;
return;
case 17:
//C
this.state = 14;
;
 //BA.debugLineNum = 92;BA.debugLine="AddTable(File.DirRootExternal & \"/WING AN APP/\",";
_addtable(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/WING AN APP/","tablet_db.db","user_token_table","user TEXT, token TEXT, last_login TEXT, last_logout TEXT");
 //BA.debugLineNum = 93;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 18;
return;
case 18:
//C
this.state = 14;
;
 //BA.debugLineNum = 94;BA.debugLine="ADD_PICKLIST_RETURN_REF";
_add_picklist_return_ref();
 //BA.debugLineNum = 95;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 19;
return;
case 19:
//C
this.state = 14;
;
 //BA.debugLineNum = 96;BA.debugLine="ADD_CANCELLED_INVOICE_REF";
_add_cancelled_invoice_ref();
 //BA.debugLineNum = 97;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 20;
return;
case 20:
//C
this.state = 14;
;
 //BA.debugLineNum = 98;BA.debugLine="ADD_CANCELLED_INVOICE_DISC";
_add_cancelled_invoice_disc();
 //BA.debugLineNum = 99;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 21;
return;
case 21:
//C
this.state = 14;
;
 //BA.debugLineNum = 100;BA.debugLine="ADD_WRONG_SERVE";
_add_wrong_serve();
 //BA.debugLineNum = 101;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 22;
return;
case 22:
//C
this.state = 14;
;
 //BA.debugLineNum = 102;BA.debugLine="ADD_NEAR_EXPIRY";
_add_near_expiry();
 //BA.debugLineNum = 103;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 23;
return;
case 23:
//C
this.state = 14;
;
 //BA.debugLineNum = 104;BA.debugLine="ADD_CANCELLED_UPLOAD";
_add_cancelled_upload();
 //BA.debugLineNum = 105;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 24;
return;
case 24:
//C
this.state = 14;
;
 //BA.debugLineNum = 106;BA.debugLine="ADD_CUSTOMER";
_add_customer();
 //BA.debugLineNum = 107;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 25;
return;
case 25:
//C
this.state = 14;
;
 //BA.debugLineNum = 108;BA.debugLine="ADD_SALESMAN";
_add_salesman();
 //BA.debugLineNum = 109;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 26;
return;
case 26:
//C
this.state = 14;
;
 //BA.debugLineNum = 110;BA.debugLine="ADD_SALES_RETURN_REF";
_add_sales_return_ref();
 //BA.debugLineNum = 111;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 27;
return;
case 27:
//C
this.state = 14;
;
 //BA.debugLineNum = 112;BA.debugLine="ADD_SALES_RETURN_REF_TRAIL";
_add_sales_return_ref_trail();
 //BA.debugLineNum = 113;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 28;
return;
case 28:
//C
this.state = 14;
;
 //BA.debugLineNum = 114;BA.debugLine="ADD_SALES_RETURN_DISC";
_add_sales_return_disc();
 //BA.debugLineNum = 115;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 29;
return;
case 29:
//C
this.state = 14;
;
 //BA.debugLineNum = 116;BA.debugLine="ADD_SALES_RETURN_DISC_TRAIL";
_add_sales_return_disc_trail();
 //BA.debugLineNum = 117;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 30;
return;
case 30:
//C
this.state = 14;
;
 //BA.debugLineNum = 118;BA.debugLine="ADD_RETURN_UPLOAD";
_add_return_upload();
 //BA.debugLineNum = 119;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 31;
return;
case 31:
//C
this.state = 14;
;
 //BA.debugLineNum = 120;BA.debugLine="ADD_RETURN_EXPIRY";
_add_return_expiry();
 //BA.debugLineNum = 121;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 32;
return;
case 32:
//C
this.state = 14;
;
 //BA.debugLineNum = 122;BA.debugLine="CHOOSE_TOKEN";
_choose_token();
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 124;BA.debugLine="Msgbox2Async(\"Please accept storage permission t";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Please accept storage permission to proceed."),BA.ObjectToCharSequence("Warning"),"","","OK",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOGO_3D.png"),processBA,anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_permissionresult(String _permission,boolean _result) throws Exception{
}
public static String  _splash_timer_tick() throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Sub SPLASH_TIMER_Tick";
 //BA.debugLineNum = 71;BA.debugLine="FADE_COUNT = FADE_COUNT + 1";
_fade_count = (int) (_fade_count+1);
 //BA.debugLineNum = 72;BA.debugLine="Activity.Color = Colors.ARGB(Min(FADE_COUNT * 5,";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (anywheresoftware.b4a.keywords.Common.Min(_fade_count*5,0)),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 73;BA.debugLine="If FADE_COUNT > 60 Then Splash_End";
if (_fade_count>60) { 
_splash_end();};
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
}
