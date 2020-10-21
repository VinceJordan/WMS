package wingan.app;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class scale_calc {
private static scale_calc mostCurrent = new scale_calc();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static double _rate = 0;
public static double _cscalex = 0;
public static double _cscaley = 0;
public static double _cscaleds = 0;
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
public wingan.app.starter _starter = null;
public wingan.app.httputils2service _httputils2service = null;
public wingan.app.b4xcollections _b4xcollections = null;
public static int  _bottom(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v) throws Exception{
 //BA.debugLineNum = 477;BA.debugLine="Public Sub Bottom(v As View) As Int";
 //BA.debugLineNum = 478;BA.debugLine="Return v.Top + v.Height";
if (true) return (int) (_v.getTop()+_v.getHeight());
 //BA.debugLineNum = 479;BA.debugLine="End Sub";
return 0;
}
public static float  _getdevicephysicalsize(anywheresoftware.b4a.BA _ba) throws Exception{
anywheresoftware.b4a.keywords.LayoutValues _lv = null;
 //BA.debugLineNum = 118;BA.debugLine="Public Sub GetDevicePhysicalSize As Float";
 //BA.debugLineNum = 119;BA.debugLine="Dim lv As LayoutValues";
_lv = new anywheresoftware.b4a.keywords.LayoutValues();
 //BA.debugLineNum = 121;BA.debugLine="lv = GetDeviceLayoutValues";
_lv = anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(_ba);
 //BA.debugLineNum = 122;BA.debugLine="Return Sqrt(Power(lv.Height / lv.Scale / 160, 2)";
if (true) return (float) (anywheresoftware.b4a.keywords.Common.Sqrt(anywheresoftware.b4a.keywords.Common.Power(_lv.Height/(double)_lv.Scale/(double)160,2)+anywheresoftware.b4a.keywords.Common.Power(_lv.Width/(double)_lv.Scale/(double)160,2)));
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return 0f;
}
public static double  _getscaleds(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 102;BA.debugLine="Public Sub GetScaleDS As Double";
 //BA.debugLineNum = 103;BA.debugLine="Return cScaleDS";
if (true) return _cscaleds;
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return 0;
}
public static double  _getscalex(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Public Sub GetScaleX As Double";
 //BA.debugLineNum = 42;BA.debugLine="Return cScaleX";
if (true) return _cscalex;
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return 0;
}
public static double  _getscalex_l(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 79;BA.debugLine="Public Sub GetScaleX_L As Double";
 //BA.debugLineNum = 80;BA.debugLine="If GetDevicePhysicalSize < 6 Then";
if (_getdevicephysicalsize(_ba)<6) { 
 //BA.debugLineNum = 81;BA.debugLine="Return (100%y / 480dip)";
if (true) return (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (480)));
 }else {
 //BA.debugLineNum = 83;BA.debugLine="Return (1 + Rate * (100%y / 480dip - 1))";
if (true) return (1+_rate*(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (480))-1));
 };
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return 0;
}
public static double  _getscalex_p(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 91;BA.debugLine="Public Sub GetScaleX_P As Double";
 //BA.debugLineNum = 92;BA.debugLine="If GetDevicePhysicalSize < 6 Then";
if (_getdevicephysicalsize(_ba)<6) { 
 //BA.debugLineNum = 93;BA.debugLine="Return (100%y / 320dip)";
if (true) return (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (320)));
 }else {
 //BA.debugLineNum = 95;BA.debugLine="Return (1 + Rate * (100%y / 320dip - 1))";
if (true) return (1+_rate*(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (320))-1));
 };
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return 0;
}
public static double  _getscaley(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 47;BA.debugLine="Public Sub GetScaleY As Double";
 //BA.debugLineNum = 48;BA.debugLine="Return cScaleY";
if (true) return _cscaley;
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return 0;
}
public static double  _getscaley_l(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Public Sub GetScaleY_L As Double";
 //BA.debugLineNum = 56;BA.debugLine="If GetDevicePhysicalSize < 6 Then";
if (_getdevicephysicalsize(_ba)<6) { 
 //BA.debugLineNum = 57;BA.debugLine="Return (100%y / 270dip)";
if (true) return (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (270)));
 }else {
 //BA.debugLineNum = 59;BA.debugLine="Return (1 + Rate * (100%y / 270dip - 1))";
if (true) return (1+_rate*(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (270))-1));
 };
 //BA.debugLineNum = 61;BA.debugLine="End Sub";
return 0;
}
public static double  _getscaley_p(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 67;BA.debugLine="Public Sub GetScaleY_P As Double";
 //BA.debugLineNum = 68;BA.debugLine="If GetDevicePhysicalSize < 6 Then";
if (_getdevicephysicalsize(_ba)<6) { 
 //BA.debugLineNum = 69;BA.debugLine="Return (100%y / 430dip)";
if (true) return (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (430)));
 }else {
 //BA.debugLineNum = 71;BA.debugLine="Return (1 + Rate * (100%y / 430dip - 1))";
if (true) return (1+_rate*(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (430))-1));
 };
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return 0;
}
public static String  _horizontalcenter(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v) throws Exception{
 //BA.debugLineNum = 311;BA.debugLine="Public Sub HorizontalCenter(v As View)";
 //BA.debugLineNum = 312;BA.debugLine="v.Left = (100%x - v.Width) / 2";
_v.setLeft((int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-_v.getWidth())/(double)2));
 //BA.debugLineNum = 313;BA.debugLine="End Sub";
return "";
}
public static String  _horizontalcenter2(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v,anywheresoftware.b4a.objects.ConcreteViewWrapper _vleft,anywheresoftware.b4a.objects.ConcreteViewWrapper _vright) throws Exception{
 //BA.debugLineNum = 317;BA.debugLine="Public Sub HorizontalCenter2(v As View, vLeft As V";
 //BA.debugLineNum = 319;BA.debugLine="If IsActivity(v) Then";
if (_isactivity(_ba,_v)) { 
 //BA.debugLineNum = 320;BA.debugLine="ToastMessageShow(\"The view is an Activity !\", Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("The view is an Activity !"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 321;BA.debugLine="Return";
if (true) return "";
 }else {
 //BA.debugLineNum = 323;BA.debugLine="If IsActivity(vLeft) Then";
if (_isactivity(_ba,_vleft)) { 
 //BA.debugLineNum = 324;BA.debugLine="If IsActivity(vRight) Then";
if (_isactivity(_ba,_vright)) { 
 //BA.debugLineNum = 325;BA.debugLine="v.Left = (100%x - v.Width) / 2";
_v.setLeft((int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-_v.getWidth())/(double)2));
 }else {
 //BA.debugLineNum = 327;BA.debugLine="v.Left = (vRight.Left - v.Width) / 2";
_v.setLeft((int) ((_vright.getLeft()-_v.getWidth())/(double)2));
 };
 }else {
 //BA.debugLineNum = 330;BA.debugLine="If IsActivity(vRight) Then";
if (_isactivity(_ba,_vright)) { 
 //BA.debugLineNum = 331;BA.debugLine="v.Left = vLeft.Left + vLeft.Width + (100%x - (";
_v.setLeft((int) (_vleft.getLeft()+_vleft.getWidth()+(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-(_vleft.getLeft()+_vleft.getWidth())-_v.getWidth())/(double)2));
 }else {
 //BA.debugLineNum = 333;BA.debugLine="v.Left = vLeft.Left + vLeft.Width + (vRight.Le";
_v.setLeft((int) (_vleft.getLeft()+_vleft.getWidth()+(_vright.getLeft()-(_vleft.getLeft()+_vleft.getWidth())-_v.getWidth())/(double)2));
 };
 };
 };
 //BA.debugLineNum = 337;BA.debugLine="End Sub";
return "";
}
public static String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 15;BA.debugLine="If GetDevicePhysicalSize < 6 Then";
if (_getdevicephysicalsize(_ba)<6) { 
 //BA.debugLineNum = 16;BA.debugLine="If 100%x > 100%y Then";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)) { 
 //BA.debugLineNum = 18;BA.debugLine="cScaleX = 100%x / 480dip";
_cscalex = anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (480));
 //BA.debugLineNum = 19;BA.debugLine="cScaleY = 100%y / 270dip";
_cscaley = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (270));
 }else {
 //BA.debugLineNum = 22;BA.debugLine="cScaleX = 100%x / 320dip";
_cscalex = anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (320));
 //BA.debugLineNum = 23;BA.debugLine="cScaleY = 100%y / 430dip";
_cscaley = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (430));
 };
 }else {
 //BA.debugLineNum = 26;BA.debugLine="If 100%x > 100%y Then";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)) { 
 //BA.debugLineNum = 28;BA.debugLine="cScaleX = 1 + Rate * (100%x / 480dip - 1)";
_cscalex = 1+_rate*(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (480))-1);
 //BA.debugLineNum = 29;BA.debugLine="cScaleY = 1 + Rate * (100%y / 270dip - 1)";
_cscaley = 1+_rate*(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (270))-1);
 }else {
 //BA.debugLineNum = 32;BA.debugLine="cScaleX = 1 + Rate * (100%x / 320dip - 1)";
_cscalex = 1+_rate*(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (320))-1);
 //BA.debugLineNum = 33;BA.debugLine="cScaleY = 1 + Rate * (100%y / 430dip - 1)";
_cscaley = 1+_rate*(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)/(double)anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (430))-1);
 };
 };
 //BA.debugLineNum = 36;BA.debugLine="cScaleDS = 1 + Rate * ((100%x + 100%y) / (320dip";
_cscaleds = 1+_rate*((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba))/(double)(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (320))+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (430)))-1);
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static boolean  _isactivity(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v) throws Exception{
 //BA.debugLineNum = 498;BA.debugLine="Public Sub IsActivity(v As View) As Boolean";
 //BA.debugLineNum = 499;BA.debugLine="Try";
try { //BA.debugLineNum = 500;BA.debugLine="v.Left = v.Left";
_v.setLeft(_v.getLeft());
 //BA.debugLineNum = 501;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 } 
       catch (Exception e5) {
			(_ba.processBA == null ? _ba : _ba.processBA).setLastException(e5); //BA.debugLineNum = 503;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 505;BA.debugLine="End Sub";
return false;
}
public static boolean  _ispanel(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v) throws Exception{
 //BA.debugLineNum = 483;BA.debugLine="Public Sub IsPanel(v As View) As Boolean";
 //BA.debugLineNum = 484;BA.debugLine="If GetType(v) = \"anywheresoftware.b4a.BALayout\" T";
if ((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("anywheresoftware.b4a.BALayout")) { 
 //BA.debugLineNum = 485;BA.debugLine="Try";
try { //BA.debugLineNum = 486;BA.debugLine="v.Left = v.Left";
_v.setLeft(_v.getLeft());
 //BA.debugLineNum = 487;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 } 
       catch (Exception e6) {
			(_ba.processBA == null ? _ba : _ba.processBA).setLastException(e6); //BA.debugLineNum = 489;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 }else {
 //BA.debugLineNum = 492;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 494;BA.debugLine="End Sub";
return false;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Public Rate As Double";
_rate = 0;
 //BA.debugLineNum = 9;BA.debugLine="Rate = 0.3 'value between 0 to 1.";
_rate = 0.3;
 //BA.debugLineNum = 10;BA.debugLine="Private cScaleX, cScaleY, cScaleDS As Double";
_cscalex = 0;
_cscaley = 0;
_cscaleds = 0;
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static int  _right(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v) throws Exception{
 //BA.debugLineNum = 472;BA.debugLine="Public Sub Right(v As View) As Int";
 //BA.debugLineNum = 473;BA.debugLine="Return v.Left + v.Width";
if (true) return (int) (_v.getLeft()+_v.getWidth());
 //BA.debugLineNum = 474;BA.debugLine="End Sub";
return 0;
}
public static String  _scaleall(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ActivityWrapper _act,boolean _firsttime) throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 208;BA.debugLine="Public Sub ScaleAll(act As Activity, FirstTime As";
 //BA.debugLineNum = 209;BA.debugLine="Dim I As Int";
_i = 0;
 //BA.debugLineNum = 212;BA.debugLine="If IsPanel(act) And FirstTime = True Then";
if (_ispanel(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_act.getObject()))) && _firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 214;BA.debugLine="ScaleView(act)";
_scaleview(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_act.getObject())));
 }else {
 //BA.debugLineNum = 216;BA.debugLine="For I = 0 To act.NumberOfViews - 1";
{
final int step5 = 1;
final int limit5 = (int) (_act.getNumberOfViews()-1);
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 217;BA.debugLine="Dim v As View";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 218;BA.debugLine="v = act.GetView(I)";
_v = _act.GetView(_i);
 //BA.debugLineNum = 219;BA.debugLine="ScaleView(v)";
_scaleview(_ba,_v);
 }
};
 };
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
return "";
}
public static String  _scaleallds(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ActivityWrapper _act,boolean _firsttime) throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
 //BA.debugLineNum = 294;BA.debugLine="Public Sub ScaleAllDS(act As Activity, FirstTime A";
 //BA.debugLineNum = 295;BA.debugLine="Dim I As Int";
_i = 0;
 //BA.debugLineNum = 298;BA.debugLine="If IsPanel(act) And FirstTime = True Then";
if (_ispanel(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_act.getObject()))) && _firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 300;BA.debugLine="ScaleViewDS(act)";
_scaleviewds(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_act.getObject())));
 }else {
 //BA.debugLineNum = 302;BA.debugLine="For I = 0 To act.NumberOfViews - 1";
{
final int step5 = 1;
final int limit5 = (int) (_act.getNumberOfViews()-1);
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 303;BA.debugLine="Dim v As View";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 304;BA.debugLine="v = act.GetView(I)";
_v = _act.GetView(_i);
 //BA.debugLineNum = 305;BA.debugLine="ScaleViewDS(v)";
_scaleviewds(_ba,_v);
 }
};
 };
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
return "";
}
public static String  _scaleview(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.ScrollViewWrapper _scv = null;
anywheresoftware.b4a.objects.ListViewWrapper _ltv = null;
anywheresoftware.b4a.objects.SpinnerWrapper _spn = null;
 //BA.debugLineNum = 128;BA.debugLine="Public Sub ScaleView(v As View)";
 //BA.debugLineNum = 129;BA.debugLine="If IsActivity(v) Then";
if (_isactivity(_ba,_v)) { 
 //BA.debugLineNum = 130;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 133;BA.debugLine="v.Left = v.Left * cScaleX";
_v.setLeft((int) (_v.getLeft()*_cscalex));
 //BA.debugLineNum = 134;BA.debugLine="v.Top = v.Top * cScaleY";
_v.setTop((int) (_v.getTop()*_cscaley));
 //BA.debugLineNum = 135;BA.debugLine="If IsPanel(v) Then";
if (_ispanel(_ba,_v)) { 
 //BA.debugLineNum = 136;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 137;BA.debugLine="pnl = v";
_pnl = (anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(_v.getObject()));
 //BA.debugLineNum = 138;BA.debugLine="If pnl.Background Is BitmapDrawable Then";
if (_pnl.getBackground() instanceof android.graphics.drawable.BitmapDrawable) { 
 //BA.debugLineNum = 141;BA.debugLine="v.Width = v.Width * Min(cScaleX, cScaleY)";
_v.setWidth((int) (_v.getWidth()*anywheresoftware.b4a.keywords.Common.Min(_cscalex,_cscaley)));
 //BA.debugLineNum = 142;BA.debugLine="v.Height = v.Height * Min(cScaleX, cScaleY)";
_v.setHeight((int) (_v.getHeight()*anywheresoftware.b4a.keywords.Common.Min(_cscalex,_cscaley)));
 }else {
 //BA.debugLineNum = 144;BA.debugLine="v.Width = v.Width * cScaleX";
_v.setWidth((int) (_v.getWidth()*_cscalex));
 //BA.debugLineNum = 145;BA.debugLine="v.Height = v.Height * cScaleY";
_v.setHeight((int) (_v.getHeight()*_cscaley));
 };
 //BA.debugLineNum = 147;BA.debugLine="ScaleAll(pnl, False)";
_scaleall(_ba,(anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(_pnl.getObject())),anywheresoftware.b4a.keywords.Common.False);
 }else if(_v.getObjectOrNull() instanceof android.widget.ImageView) { 
 //BA.debugLineNum = 151;BA.debugLine="v.Width = v.Width * Min(cScaleX, cScaleY)";
_v.setWidth((int) (_v.getWidth()*anywheresoftware.b4a.keywords.Common.Min(_cscalex,_cscaley)));
 //BA.debugLineNum = 152;BA.debugLine="v.Height = v.Height * Min(cScaleX, cScaleY)";
_v.setHeight((int) (_v.getHeight()*anywheresoftware.b4a.keywords.Common.Min(_cscalex,_cscaley)));
 }else {
 //BA.debugLineNum = 154;BA.debugLine="v.Width = v.Width * cScaleX";
_v.setWidth((int) (_v.getWidth()*_cscalex));
 //BA.debugLineNum = 155;BA.debugLine="v.Height = v.Height * cScaleY";
_v.setHeight((int) (_v.getHeight()*_cscaley));
 };
 //BA.debugLineNum = 158;BA.debugLine="If v Is Label Then 'this will catch ALL views wit";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 159;BA.debugLine="Dim lbl As Label = v";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_lbl = (anywheresoftware.b4a.objects.LabelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.LabelWrapper(), (android.widget.TextView)(_v.getObject()));
 //BA.debugLineNum = 160;BA.debugLine="lbl.TextSize = lbl.TextSize * cScaleX";
_lbl.setTextSize((float) (_lbl.getTextSize()*_cscalex));
 };
 //BA.debugLineNum = 163;BA.debugLine="If GetType(v) = \"anywheresoftware.b4a.objects.Scr";
if ((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("anywheresoftware.b4a.objects.ScrollViewWrapper$MyScrollView")) { 
 //BA.debugLineNum = 166;BA.debugLine="Dim scv As ScrollView";
_scv = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 167;BA.debugLine="scv = v";
_scv = (anywheresoftware.b4a.objects.ScrollViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ScrollViewWrapper(), (android.widget.ScrollView)(_v.getObject()));
 //BA.debugLineNum = 168;BA.debugLine="ScaleAll(scv.Panel, False)";
_scaleall(_ba,(anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(_scv.getPanel().getObject())),anywheresoftware.b4a.keywords.Common.False);
 }else if((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("flm.b4a.scrollview2d.ScrollView2DWrapper$MyScrollView")) { 
 }else if((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("anywheresoftware.b4a.objects.ListViewWrapper$SimpleListView")) { 
 //BA.debugLineNum = 178;BA.debugLine="Dim ltv As ListView";
_ltv = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 179;BA.debugLine="ltv = v";
_ltv = (anywheresoftware.b4a.objects.ListViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ListViewWrapper(), (anywheresoftware.b4a.objects.ListViewWrapper.SimpleListView)(_v.getObject()));
 //BA.debugLineNum = 180;BA.debugLine="ScaleView(ltv.SingleLineLayout.Label)";
_scaleview(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getSingleLineLayout().Label.getObject())));
 //BA.debugLineNum = 181;BA.debugLine="ltv.SingleLineLayout.ItemHeight = ltv.SingleLine";
_ltv.getSingleLineLayout().setItemHeight((int) (_ltv.getSingleLineLayout().getItemHeight()*_cscaley));
 //BA.debugLineNum = 183;BA.debugLine="ScaleView(ltv.TwoLinesLayout.Label)";
_scaleview(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesLayout().Label.getObject())));
 //BA.debugLineNum = 184;BA.debugLine="ScaleView(ltv.TwoLinesLayout.SecondLabel)";
_scaleview(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesLayout().SecondLabel.getObject())));
 //BA.debugLineNum = 185;BA.debugLine="ltv.TwoLinesLayout.ItemHeight = ltv.TwoLinesLayo";
_ltv.getTwoLinesLayout().setItemHeight((int) (_ltv.getTwoLinesLayout().getItemHeight()*_cscaley));
 //BA.debugLineNum = 187;BA.debugLine="ScaleView(ltv.TwoLinesAndBitmap.Label)";
_scaleview(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesAndBitmap().Label.getObject())));
 //BA.debugLineNum = 188;BA.debugLine="ScaleView(ltv.TwoLinesAndBitmap.SecondLabel)";
_scaleview(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesAndBitmap().SecondLabel.getObject())));
 //BA.debugLineNum = 189;BA.debugLine="ScaleView(ltv.TwoLinesAndBitmap.ImageView)";
_scaleview(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesAndBitmap().ImageView.getObject())));
 //BA.debugLineNum = 190;BA.debugLine="ltv.TwoLinesAndBitmap.ItemHeight = ltv.TwoLinesA";
_ltv.getTwoLinesAndBitmap().setItemHeight((int) (_ltv.getTwoLinesAndBitmap().getItemHeight()*_cscaley));
 //BA.debugLineNum = 192;BA.debugLine="ltv.TwoLinesAndBitmap.ImageView.Top = (ltv.TwoLi";
_ltv.getTwoLinesAndBitmap().ImageView.setTop((int) ((_ltv.getTwoLinesAndBitmap().getItemHeight()-_ltv.getTwoLinesAndBitmap().ImageView.getHeight())/(double)2));
 }else if((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("anywheresoftware.b4a.objects.SpinnerWrapper$B4ASpinner")) { 
 //BA.debugLineNum = 196;BA.debugLine="Dim spn As Spinner";
_spn = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 197;BA.debugLine="spn = v";
_spn = (anywheresoftware.b4a.objects.SpinnerWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.SpinnerWrapper(), (anywheresoftware.b4a.objects.SpinnerWrapper.B4ASpinner)(_v.getObject()));
 //BA.debugLineNum = 198;BA.debugLine="spn.TextSize = spn.TextSize * cScaleX";
_spn.setTextSize((float) (_spn.getTextSize()*_cscalex));
 };
 //BA.debugLineNum = 200;BA.debugLine="End Sub";
return "";
}
public static String  _scaleviewds(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.ScrollViewWrapper _scv = null;
anywheresoftware.b4a.objects.ListViewWrapper _ltv = null;
anywheresoftware.b4a.objects.SpinnerWrapper _spn = null;
 //BA.debugLineNum = 227;BA.debugLine="Public Sub ScaleViewDS(v As View)";
 //BA.debugLineNum = 228;BA.debugLine="If IsActivity(v) Then";
if (_isactivity(_ba,_v)) { 
 //BA.debugLineNum = 229;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 232;BA.debugLine="v.Left = v.Left * cScaleDS";
_v.setLeft((int) (_v.getLeft()*_cscaleds));
 //BA.debugLineNum = 233;BA.debugLine="v.Top = v.Top * cScaleDS";
_v.setTop((int) (_v.getTop()*_cscaleds));
 //BA.debugLineNum = 234;BA.debugLine="v.Width = v.Width * cScaleDS";
_v.setWidth((int) (_v.getWidth()*_cscaleds));
 //BA.debugLineNum = 235;BA.debugLine="v.Height = v.Height * cScaleDS";
_v.setHeight((int) (_v.getHeight()*_cscaleds));
 //BA.debugLineNum = 237;BA.debugLine="If IsPanel(v) Then";
if (_ispanel(_ba,_v)) { 
 //BA.debugLineNum = 238;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 239;BA.debugLine="pnl = v";
_pnl = (anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(_v.getObject()));
 //BA.debugLineNum = 240;BA.debugLine="ScaleAllDS(pnl, False)";
_scaleallds(_ba,(anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(_pnl.getObject())),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 243;BA.debugLine="If v Is Label Then 'this will catch ALL views wit";
if (_v.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 244;BA.debugLine="Dim lbl As Label = v";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_lbl = (anywheresoftware.b4a.objects.LabelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.LabelWrapper(), (android.widget.TextView)(_v.getObject()));
 //BA.debugLineNum = 245;BA.debugLine="lbl.TextSize = lbl.TextSize * cScaleDS";
_lbl.setTextSize((float) (_lbl.getTextSize()*_cscaleds));
 };
 //BA.debugLineNum = 248;BA.debugLine="If GetType(v) = \"anywheresoftware.b4a.objects.Scr";
if ((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("anywheresoftware.b4a.objects.ScrollViewWrapper$MyScrollView")) { 
 //BA.debugLineNum = 251;BA.debugLine="Dim scv As ScrollView";
_scv = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 252;BA.debugLine="scv = v";
_scv = (anywheresoftware.b4a.objects.ScrollViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ScrollViewWrapper(), (android.widget.ScrollView)(_v.getObject()));
 //BA.debugLineNum = 253;BA.debugLine="ScaleAllDS(scv.Panel, False)";
_scaleallds(_ba,(anywheresoftware.b4a.objects.ActivityWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ActivityWrapper(), (anywheresoftware.b4a.BALayout)(_scv.getPanel().getObject())),anywheresoftware.b4a.keywords.Common.False);
 }else if((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("flm.b4a.scrollview2d.ScrollView2DWrapper$MyScrollView")) { 
 }else if((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("anywheresoftware.b4a.objects.ListViewWrapper$SimpleListView")) { 
 //BA.debugLineNum = 263;BA.debugLine="Dim ltv As ListView";
_ltv = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 264;BA.debugLine="ltv = v";
_ltv = (anywheresoftware.b4a.objects.ListViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ListViewWrapper(), (anywheresoftware.b4a.objects.ListViewWrapper.SimpleListView)(_v.getObject()));
 //BA.debugLineNum = 265;BA.debugLine="ScaleViewDS(ltv.SingleLineLayout.Label)";
_scaleviewds(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getSingleLineLayout().Label.getObject())));
 //BA.debugLineNum = 266;BA.debugLine="ltv.SingleLineLayout.ItemHeight = ltv.SingleLine";
_ltv.getSingleLineLayout().setItemHeight((int) (_ltv.getSingleLineLayout().getItemHeight()*_cscaleds));
 //BA.debugLineNum = 268;BA.debugLine="ScaleViewDS(ltv.TwoLinesLayout.Label)";
_scaleviewds(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesLayout().Label.getObject())));
 //BA.debugLineNum = 269;BA.debugLine="ScaleViewDS(ltv.TwoLinesLayout.SecondLabel)";
_scaleviewds(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesLayout().SecondLabel.getObject())));
 //BA.debugLineNum = 270;BA.debugLine="ltv.TwoLinesLayout.ItemHeight = ltv.TwoLinesLayo";
_ltv.getTwoLinesLayout().setItemHeight((int) (_ltv.getTwoLinesLayout().getItemHeight()*_cscaleds));
 //BA.debugLineNum = 272;BA.debugLine="ScaleViewDS(ltv.TwoLinesAndBitmap.Label)";
_scaleviewds(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesAndBitmap().Label.getObject())));
 //BA.debugLineNum = 273;BA.debugLine="ScaleViewDS(ltv.TwoLinesAndBitmap.SecondLabel)";
_scaleviewds(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesAndBitmap().SecondLabel.getObject())));
 //BA.debugLineNum = 274;BA.debugLine="ScaleViewDS(ltv.TwoLinesAndBitmap.ImageView)";
_scaleviewds(_ba,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_ltv.getTwoLinesAndBitmap().ImageView.getObject())));
 //BA.debugLineNum = 275;BA.debugLine="ltv.TwoLinesAndBitmap.ItemHeight = ltv.TwoLinesA";
_ltv.getTwoLinesAndBitmap().setItemHeight((int) (_ltv.getTwoLinesAndBitmap().getItemHeight()*_cscaleds));
 //BA.debugLineNum = 277;BA.debugLine="ltv.TwoLinesAndBitmap.ImageView.Top = (ltv.TwoLi";
_ltv.getTwoLinesAndBitmap().ImageView.setTop((int) ((_ltv.getTwoLinesAndBitmap().getItemHeight()-_ltv.getTwoLinesAndBitmap().ImageView.getHeight())/(double)2));
 }else if((anywheresoftware.b4a.keywords.Common.GetType((Object)(_v.getObject()))).equals("anywheresoftware.b4a.objects.SpinnerWrapper$B4ASpinner")) { 
 //BA.debugLineNum = 281;BA.debugLine="Dim spn As Spinner";
_spn = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 282;BA.debugLine="spn = v";
_spn = (anywheresoftware.b4a.objects.SpinnerWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.SpinnerWrapper(), (anywheresoftware.b4a.objects.SpinnerWrapper.B4ASpinner)(_v.getObject()));
 //BA.debugLineNum = 283;BA.debugLine="spn.TextSize = spn.TextSize * cScaleDS";
_spn.setTextSize((float) (_spn.getTextSize()*_cscaleds));
 };
 //BA.debugLineNum = 285;BA.debugLine="End Sub";
return "";
}
public static String  _setbottom(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v,int _ybottom) throws Exception{
 //BA.debugLineNum = 375;BA.debugLine="Sub SetBottom(v As View, yBottom As Int)";
 //BA.debugLineNum = 376;BA.debugLine="v.Top = yBottom - v.Height";
_v.setTop((int) (_ybottom-_v.getHeight()));
 //BA.debugLineNum = 377;BA.debugLine="End Sub";
return "";
}
public static String  _setleftandright(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v,int _xleft,int _xright) throws Exception{
 //BA.debugLineNum = 381;BA.debugLine="Public Sub SetLeftAndRight(v As View, xLeft As Int";
 //BA.debugLineNum = 383;BA.debugLine="v.Left = Min(xLeft, xRight)";
_v.setLeft((int) (anywheresoftware.b4a.keywords.Common.Min(_xleft,_xright)));
 //BA.debugLineNum = 384;BA.debugLine="v.Width = Max(xLeft, xRight) - v.Left";
_v.setWidth((int) (anywheresoftware.b4a.keywords.Common.Max(_xleft,_xright)-_v.getLeft()));
 //BA.debugLineNum = 385;BA.debugLine="End Sub";
return "";
}
public static String  _setleftandright2(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v,anywheresoftware.b4a.objects.ConcreteViewWrapper _vleft,int _dxl,anywheresoftware.b4a.objects.ConcreteViewWrapper _vright,int _dxr) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _v1 = null;
 //BA.debugLineNum = 391;BA.debugLine="Public Sub SetLeftAndRight2(v As View, vLeft As Vi";
 //BA.debugLineNum = 393;BA.debugLine="If IsActivity(v) Then";
if (_isactivity(_ba,_v)) { 
 //BA.debugLineNum = 394;BA.debugLine="ToastMessageShow(\"The view is an Activity !\", Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("The view is an Activity !"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 395;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 399;BA.debugLine="If IsActivity(vLeft) = False And IsActivity(vRigh";
if (_isactivity(_ba,_vleft)==anywheresoftware.b4a.keywords.Common.False && _isactivity(_ba,_vright)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 400;BA.debugLine="If vLeft.Left > vRight.Left Then";
if (_vleft.getLeft()>_vright.getLeft()) { 
 //BA.debugLineNum = 401;BA.debugLine="Dim v1 As View";
_v1 = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 402;BA.debugLine="v1 = vLeft";
_v1 = _vleft;
 //BA.debugLineNum = 403;BA.debugLine="vLeft = vRight";
_vleft = _vright;
 //BA.debugLineNum = 404;BA.debugLine="vRight = v1";
_vright = _v1;
 };
 };
 //BA.debugLineNum = 408;BA.debugLine="If IsActivity(vLeft) Then";
if (_isactivity(_ba,_vleft)) { 
 //BA.debugLineNum = 409;BA.debugLine="v.Left = dxL";
_v.setLeft(_dxl);
 //BA.debugLineNum = 410;BA.debugLine="If IsActivity(vRight) Then";
if (_isactivity(_ba,_vright)) { 
 //BA.debugLineNum = 411;BA.debugLine="v.Width = 100%x - dxL - dxR";
_v.setWidth((int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-_dxl-_dxr));
 }else {
 //BA.debugLineNum = 413;BA.debugLine="v.Width = vRight.Left - dxL - dxR";
_v.setWidth((int) (_vright.getLeft()-_dxl-_dxr));
 };
 }else {
 //BA.debugLineNum = 416;BA.debugLine="v.Left = vLeft.Left + vLeft.Width + dxL";
_v.setLeft((int) (_vleft.getLeft()+_vleft.getWidth()+_dxl));
 //BA.debugLineNum = 417;BA.debugLine="If IsActivity(vRight) Then";
if (_isactivity(_ba,_vright)) { 
 //BA.debugLineNum = 418;BA.debugLine="v.Width = 100%x - v.Left";
_v.setWidth((int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba)-_v.getLeft()));
 }else {
 //BA.debugLineNum = 420;BA.debugLine="v.Width = vRight.Left - v.Left - dxR";
_v.setWidth((int) (_vright.getLeft()-_v.getLeft()-_dxr));
 };
 };
 //BA.debugLineNum = 423;BA.debugLine="End Sub";
return "";
}
public static String  _setrate(anywheresoftware.b4a.BA _ba,double _crate) throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Public Sub SetRate(cRate As Double)";
 //BA.debugLineNum = 111;BA.debugLine="Rate = cRate";
_rate = _crate;
 //BA.debugLineNum = 112;BA.debugLine="Initialize";
_initialize(_ba);
 //BA.debugLineNum = 113;BA.debugLine="End Sub";
return "";
}
public static String  _setright(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v,int _xright) throws Exception{
 //BA.debugLineNum = 369;BA.debugLine="Sub SetRight(v As View, xRight As Int)";
 //BA.debugLineNum = 370;BA.debugLine="v.Left = xRight - v.Width";
_v.setLeft((int) (_xright-_v.getWidth()));
 //BA.debugLineNum = 371;BA.debugLine="End Sub";
return "";
}
public static String  _settopandbottom(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v,int _ytop,int _ybottom) throws Exception{
 //BA.debugLineNum = 427;BA.debugLine="Public Sub SetTopAndBottom(v As View, yTop As Int,";
 //BA.debugLineNum = 429;BA.debugLine="v.Top = Min(yTop, yBottom)";
_v.setTop((int) (anywheresoftware.b4a.keywords.Common.Min(_ytop,_ybottom)));
 //BA.debugLineNum = 430;BA.debugLine="v.Height = Max(yTop, yBottom) - v.Top";
_v.setHeight((int) (anywheresoftware.b4a.keywords.Common.Max(_ytop,_ybottom)-_v.getTop()));
 //BA.debugLineNum = 431;BA.debugLine="End Sub";
return "";
}
public static String  _settopandbottom2(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v,anywheresoftware.b4a.objects.ConcreteViewWrapper _vtop,int _dyt,anywheresoftware.b4a.objects.ConcreteViewWrapper _vbottom,int _dyb) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _v1 = null;
 //BA.debugLineNum = 437;BA.debugLine="Public Sub SetTopAndBottom2(v As View, vTop As Vie";
 //BA.debugLineNum = 439;BA.debugLine="If IsActivity(v) Then";
if (_isactivity(_ba,_v)) { 
 //BA.debugLineNum = 440;BA.debugLine="ToastMessageShow(\"The view is an Activity !\", Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("The view is an Activity !"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 441;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 444;BA.debugLine="If IsActivity(vTop) = False And IsActivity(vBotto";
if (_isactivity(_ba,_vtop)==anywheresoftware.b4a.keywords.Common.False && _isactivity(_ba,_vbottom)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 446;BA.debugLine="If vTop.Top > vBottom.Top Then";
if (_vtop.getTop()>_vbottom.getTop()) { 
 //BA.debugLineNum = 447;BA.debugLine="Dim v1 As View";
_v1 = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 448;BA.debugLine="v1 = vTop";
_v1 = _vtop;
 //BA.debugLineNum = 449;BA.debugLine="vTop = vBottom";
_vtop = _vbottom;
 //BA.debugLineNum = 450;BA.debugLine="vBottom = v1";
_vbottom = _v1;
 };
 };
 //BA.debugLineNum = 454;BA.debugLine="If IsActivity(vTop) Then";
if (_isactivity(_ba,_vtop)) { 
 //BA.debugLineNum = 455;BA.debugLine="v.Top = dyT";
_v.setTop(_dyt);
 //BA.debugLineNum = 456;BA.debugLine="If IsActivity(vBottom) Then";
if (_isactivity(_ba,_vbottom)) { 
 //BA.debugLineNum = 457;BA.debugLine="v.Height = 100%y - dyT - dyB";
_v.setHeight((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)-_dyt-_dyb));
 }else {
 //BA.debugLineNum = 459;BA.debugLine="v.Height = vBottom.Top - dyT - dyB";
_v.setHeight((int) (_vbottom.getTop()-_dyt-_dyb));
 };
 }else {
 //BA.debugLineNum = 462;BA.debugLine="v.Top = vTop.Top + vTop.Height + dyT";
_v.setTop((int) (_vtop.getTop()+_vtop.getHeight()+_dyt));
 //BA.debugLineNum = 463;BA.debugLine="If IsActivity(vBottom) Then";
if (_isactivity(_ba,_vbottom)) { 
 //BA.debugLineNum = 464;BA.debugLine="v.Height = 100%y - v.Top - dyB";
_v.setHeight((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)-_v.getTop()-_dyb));
 }else {
 //BA.debugLineNum = 466;BA.debugLine="v.Height = vBottom.Top - v.Top - dyB";
_v.setHeight((int) (_vbottom.getTop()-_v.getTop()-_dyb));
 };
 };
 //BA.debugLineNum = 469;BA.debugLine="End Sub";
return "";
}
public static String  _verticalcenter(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v) throws Exception{
 //BA.debugLineNum = 340;BA.debugLine="Public Sub VerticalCenter(v As View)";
 //BA.debugLineNum = 341;BA.debugLine="v.Top = (100%y - v.Height) / 2";
_v.setTop((int) ((anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)-_v.getHeight())/(double)2));
 //BA.debugLineNum = 342;BA.debugLine="End Sub";
return "";
}
public static String  _verticalcenter2(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.ConcreteViewWrapper _v,anywheresoftware.b4a.objects.ConcreteViewWrapper _vtop,anywheresoftware.b4a.objects.ConcreteViewWrapper _vbottom) throws Exception{
 //BA.debugLineNum = 346;BA.debugLine="Public Sub VerticalCenter2(v As View, vTop As View";
 //BA.debugLineNum = 347;BA.debugLine="If IsActivity(v) Then";
if (_isactivity(_ba,_v)) { 
 //BA.debugLineNum = 348;BA.debugLine="ToastMessageShow(\"The view is an Activity !\", Fa";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("The view is an Activity !"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 349;BA.debugLine="Return";
if (true) return "";
 }else {
 //BA.debugLineNum = 351;BA.debugLine="If IsActivity(vTop) Then";
if (_isactivity(_ba,_vtop)) { 
 //BA.debugLineNum = 352;BA.debugLine="If IsActivity(vBottom) Then";
if (_isactivity(_ba,_vbottom)) { 
 //BA.debugLineNum = 353;BA.debugLine="v.Top = (100%y - v.Height) / 2";
_v.setTop((int) ((anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)-_v.getHeight())/(double)2));
 }else {
 //BA.debugLineNum = 355;BA.debugLine="v.Top = (vBottom.Top - v.Height) / 2";
_v.setTop((int) ((_vbottom.getTop()-_v.getHeight())/(double)2));
 };
 }else {
 //BA.debugLineNum = 358;BA.debugLine="If IsActivity(vBottom) Then";
if (_isactivity(_ba,_vbottom)) { 
 //BA.debugLineNum = 359;BA.debugLine="v.Top = vTop.Top + vTop.Height + (100%y - (vTo";
_v.setTop((int) (_vtop.getTop()+_vtop.getHeight()+(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)-(_vtop.getTop()+_vtop.getHeight())-_v.getHeight())/(double)2));
 }else {
 //BA.debugLineNum = 361;BA.debugLine="v.Top = vTop.Top + vTop.Height + (vBottom.Top";
_v.setTop((int) (_vtop.getTop()+_vtop.getHeight()+(_vbottom.getTop()-(_vtop.getTop()+_vtop.getHeight())-_v.getHeight())/(double)2));
 };
 };
 };
 //BA.debugLineNum = 365;BA.debugLine="End Sub";
return "";
}
}
