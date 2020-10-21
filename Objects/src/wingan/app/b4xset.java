package wingan.app;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xset extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "wingan.app.b4xset");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", wingan.app.b4xset.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public wingan.app.b4xorderedmap _map = null;
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
public wingan.app.receiving_module _receiving_module = null;
public wingan.app.receiving2_module _receiving2_module = null;
public wingan.app.scale_calc _scale_calc = null;
public wingan.app.starter _starter = null;
public wingan.app.httputils2service _httputils2service = null;
public wingan.app.b4xcollections _b4xcollections = null;
public String  _add(Object _value) throws Exception{
 //BA.debugLineNum = 9;BA.debugLine="Public Sub Add(Value As Object)";
 //BA.debugLineNum = 10;BA.debugLine="map.Put(Value, \"\")";
_map._put /*String*/ (_value,(Object)(""));
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.collections.List  _aslist() throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Public Sub AsList As List";
 //BA.debugLineNum = 30;BA.debugLine="Return map.Keys";
if (true) return _map._getkeys /*anywheresoftware.b4a.objects.collections.List*/ ();
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return null;
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 1;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 2;BA.debugLine="Private map As B4XOrderedMap";
_map = new wingan.app.b4xorderedmap();
 //BA.debugLineNum = 3;BA.debugLine="End Sub";
return "";
}
public String  _clear() throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Public Sub Clear";
 //BA.debugLineNum = 26;BA.debugLine="map.Clear";
_map._clear /*String*/ ();
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public boolean  _contains(Object _value) throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Public Sub Contains (Value As Object) As Boolean";
 //BA.debugLineNum = 18;BA.debugLine="Return map.ContainsKey(Value)";
if (true) return _map._containskey /*boolean*/ (_value);
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return false;
}
public int  _getsize() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Public Sub getSize As Int";
 //BA.debugLineNum = 22;BA.debugLine="Return map.Size";
if (true) return _map._getsize /*int*/ ();
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 5;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 6;BA.debugLine="map.Initialize";
_map._initialize /*String*/ (ba);
 //BA.debugLineNum = 7;BA.debugLine="End Sub";
return "";
}
public String  _remove(Object _value) throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Public Sub Remove(Value As Object)";
 //BA.debugLineNum = 14;BA.debugLine="map.Remove(Value)";
_map._remove /*String*/ (_value);
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
