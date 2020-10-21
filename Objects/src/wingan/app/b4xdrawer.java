package wingan.app;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xdrawer extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "wingan.app.b4xdrawer");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", wingan.app.b4xdrawer.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _meventname = "";
public Object _mcallback = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public int _msidewidth = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper _mleftpanel = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mdarkpanel = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mbasepanel = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _mcenterpanel = null;
public int _extrawidth = 0;
public float _touchxstart = 0f;
public float _touchystart = 0f;
public boolean _isopen = false;
public boolean _handlingswipe = false;
public boolean _startatscrim = false;
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
public boolean  _base_onintercepttouchevent(int _action,float _x,float _y,Object _motionevent) throws Exception{
float _dx = 0f;
float _dy = 0f;
 //BA.debugLineNum = 87;BA.debugLine="Private Sub Base_OnInterceptTouchEvent (Action As";
 //BA.debugLineNum = 88;BA.debugLine="If IsOpen = False And x > mLeftPanel.Left + mLeft";
if (_isopen==__c.False && _x>_mleftpanel.getLeft()+_mleftpanel.getWidth()+_extrawidth) { 
if (true) return __c.False;};
 //BA.debugLineNum = 89;BA.debugLine="If IsOpen And x > mLeftPanel.Left + mLeftPanel.Wi";
if (_isopen && _x>_mleftpanel.getLeft()+_mleftpanel.getWidth()) { 
 //BA.debugLineNum = 91;BA.debugLine="Return True";
if (true) return __c.True;
 };
 //BA.debugLineNum = 93;BA.debugLine="If HandlingSwipe Then Return True";
if (_handlingswipe) { 
if (true) return __c.True;};
 //BA.debugLineNum = 94;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,_mbasepanel.TOUCH_ACTION_DOWN,_mbasepanel.TOUCH_ACTION_MOVE)) {
case 0: {
 //BA.debugLineNum = 96;BA.debugLine="TouchXStart = X";
_touchxstart = _x;
 //BA.debugLineNum = 97;BA.debugLine="TouchYStart = Y";
_touchystart = _y;
 //BA.debugLineNum = 98;BA.debugLine="HandlingSwipe = False";
_handlingswipe = __c.False;
 break; }
case 1: {
 //BA.debugLineNum = 100;BA.debugLine="Dim dx As Float = Abs(x - TouchXStart)";
_dx = (float) (__c.Abs(_x-_touchxstart));
 //BA.debugLineNum = 101;BA.debugLine="Dim dy As Float = Abs(y - TouchYStart)";
_dy = (float) (__c.Abs(_y-_touchystart));
 //BA.debugLineNum = 102;BA.debugLine="If dy < 20dip And dx > 10dip Then";
if (_dy<__c.DipToCurrent((int) (20)) && _dx>__c.DipToCurrent((int) (10))) { 
 //BA.debugLineNum = 103;BA.debugLine="HandlingSwipe = True";
_handlingswipe = __c.True;
 };
 break; }
}
;
 //BA.debugLineNum = 106;BA.debugLine="Return HandlingSwipe";
if (true) return _handlingswipe;
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return false;
}
public boolean  _base_ontouchevent(int _action,float _x,float _y,Object _motionevent) throws Exception{
int _leftpanelrightside = 0;
float _dx = 0f;
 //BA.debugLineNum = 57;BA.debugLine="Private Sub Base_OnTouchEvent (Action As Int, X As";
 //BA.debugLineNum = 58;BA.debugLine="Dim LeftPanelRightSide As Int = mLeftPanel.Left +";
_leftpanelrightside = (int) (_mleftpanel.getLeft()+_mleftpanel.getWidth());
 //BA.debugLineNum = 59;BA.debugLine="If HandlingSwipe = False And x > LeftPanelRightSi";
if (_handlingswipe==__c.False && _x>_leftpanelrightside) { 
 //BA.debugLineNum = 60;BA.debugLine="If IsOpen Then";
if (_isopen) { 
 //BA.debugLineNum = 61;BA.debugLine="TouchXStart = X";
_touchxstart = _x;
 //BA.debugLineNum = 62;BA.debugLine="If Action = mBasePanel.TOUCH_ACTION_UP Then set";
if (_action==_mbasepanel.TOUCH_ACTION_UP) { 
_setleftopen(__c.False);};
 //BA.debugLineNum = 63;BA.debugLine="Return True";
if (true) return __c.True;
 };
 //BA.debugLineNum = 65;BA.debugLine="If IsOpen = False And x > LeftPanelRightSide + E";
if (_isopen==__c.False && _x>_leftpanelrightside+_extrawidth) { 
 //BA.debugLineNum = 66;BA.debugLine="Return False";
if (true) return __c.False;
 };
 };
 //BA.debugLineNum = 69;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,_mbasepanel.TOUCH_ACTION_MOVE,_mbasepanel.TOUCH_ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 71;BA.debugLine="Dim dx As Float = x - TouchXStart";
_dx = (float) (_x-_touchxstart);
 //BA.debugLineNum = 72;BA.debugLine="TouchXStart = X";
_touchxstart = _x;
 //BA.debugLineNum = 73;BA.debugLine="If HandlingSwipe Or Abs(dx) > 3dip Then";
if (_handlingswipe || __c.Abs(_dx)>__c.DipToCurrent((int) (3))) { 
 //BA.debugLineNum = 74;BA.debugLine="HandlingSwipe = True";
_handlingswipe = __c.True;
 //BA.debugLineNum = 75;BA.debugLine="ChangeOffset(mLeftPanel.Left + dx, True, False";
_changeoffset((float) (_mleftpanel.getLeft()+_dx),__c.True,__c.False);
 };
 break; }
case 1: {
 //BA.debugLineNum = 78;BA.debugLine="If HandlingSwipe Then";
if (_handlingswipe) { 
 //BA.debugLineNum = 79;BA.debugLine="ChangeOffset(mLeftPanel.Left, False, False)";
_changeoffset((float) (_mleftpanel.getLeft()),__c.False,__c.False);
 };
 //BA.debugLineNum = 81;BA.debugLine="HandlingSwipe = False";
_handlingswipe = __c.False;
 break; }
}
;
 //BA.debugLineNum = 83;BA.debugLine="Return True";
if (true) return __c.True;
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return false;
}
public String  _changeoffset(float _x,boolean _currentlytouching,boolean _noanimation) throws Exception{
int _visibleoffset = 0;
int _dx = 0;
int _duration = 0;
 //BA.debugLineNum = 159;BA.debugLine="Private Sub ChangeOffset (x As Float, CurrentlyTou";
 //BA.debugLineNum = 160;BA.debugLine="x = Max(-mSideWidth, Min(0, x))";
_x = (float) (__c.Max(-_msidewidth,__c.Min(0,_x)));
 //BA.debugLineNum = 161;BA.debugLine="Dim VisibleOffset As Int = mSideWidth + x";
_visibleoffset = (int) (_msidewidth+_x);
 //BA.debugLineNum = 170;BA.debugLine="If CurrentlyTouching = False Then";
if (_currentlytouching==__c.False) { 
 //BA.debugLineNum = 171;BA.debugLine="If (IsOpen And VisibleOffset < 0.8 * mSideWidth)";
if ((_isopen && _visibleoffset<0.8*_msidewidth) || (_isopen==__c.False && _visibleoffset<0.2*_msidewidth)) { 
 //BA.debugLineNum = 172;BA.debugLine="x = -mSideWidth";
_x = (float) (-_msidewidth);
 //BA.debugLineNum = 173;BA.debugLine="IsOpen = False";
_isopen = __c.False;
 }else {
 //BA.debugLineNum = 175;BA.debugLine="x = 0";
_x = (float) (0);
 //BA.debugLineNum = 176;BA.debugLine="IsOpen = True";
_isopen = __c.True;
 };
 //BA.debugLineNum = 178;BA.debugLine="Dim dx As Int = Abs(mLeftPanel.Left - x)";
_dx = (int) (__c.Abs(_mleftpanel.getLeft()-_x));
 //BA.debugLineNum = 179;BA.debugLine="Dim duration As Int = Max(0, 200 * dx / mSideWid";
_duration = (int) (__c.Max(0,200*_dx/(double)_msidewidth));
 //BA.debugLineNum = 180;BA.debugLine="If NoAnimation Then duration = 0";
if (_noanimation) { 
_duration = (int) (0);};
 //BA.debugLineNum = 181;BA.debugLine="mLeftPanel.SetLayoutAnimated(duration, x, 0, mLe";
_mleftpanel.SetLayoutAnimated(_duration,(int) (_x),(int) (0),_mleftpanel.getWidth(),_mleftpanel.getHeight());
 //BA.debugLineNum = 182;BA.debugLine="mDarkPanel.SetColorAnimated(duration, mDarkPanel";
_mdarkpanel.SetColorAnimated(_duration,_mdarkpanel.getColor(),_offsettocolor((int) (_x)));
 }else {
 //BA.debugLineNum = 190;BA.debugLine="mDarkPanel.Color = OffsetToColor(x)";
_mdarkpanel.setColor(_offsettocolor((int) (_x)));
 //BA.debugLineNum = 191;BA.debugLine="mLeftPanel.Left = x";
_mleftpanel.setLeft((int) (_x));
 };
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Private mEventName As String 'ignore";
_meventname = "";
 //BA.debugLineNum = 4;BA.debugLine="Private mCallBack As Object 'ignore";
_mcallback = new Object();
 //BA.debugLineNum = 5;BA.debugLine="Private xui As XUI 'ignore";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 6;BA.debugLine="Private mSideWidth As Int";
_msidewidth = 0;
 //BA.debugLineNum = 7;BA.debugLine="Private mLeftPanel As B4XView";
_mleftpanel = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 8;BA.debugLine="Private mDarkPanel As B4XView";
_mdarkpanel = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 9;BA.debugLine="Private mBasePanel As B4XView";
_mbasepanel = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Private mCenterPanel As B4XView";
_mcenterpanel = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Private ExtraWidth As Int = 50dip";
_extrawidth = __c.DipToCurrent((int) (50));
 //BA.debugLineNum = 12;BA.debugLine="Private TouchXStart, TouchYStart As Float 'ignore";
_touchxstart = 0f;
_touchystart = 0f;
 //BA.debugLineNum = 13;BA.debugLine="Private IsOpen As Boolean";
_isopen = false;
 //BA.debugLineNum = 14;BA.debugLine="Private HandlingSwipe As Boolean";
_handlingswipe = false;
 //BA.debugLineNum = 15;BA.debugLine="Private StartAtScrim As Boolean 'ignore";
_startatscrim = false;
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getcenterpanel() throws Exception{
 //BA.debugLineNum = 216;BA.debugLine="Public Sub getCenterPanel As B4XView";
 //BA.debugLineNum = 217;BA.debugLine="Return mCenterPanel";
if (true) return _mcenterpanel;
 //BA.debugLineNum = 218;BA.debugLine="End Sub";
return null;
}
public boolean  _getleftopen() throws Exception{
 //BA.debugLineNum = 201;BA.debugLine="Public Sub getLeftOpen As Boolean";
 //BA.debugLineNum = 202;BA.debugLine="Return IsOpen";
if (true) return _isopen;
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
return false;
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getleftpanel() throws Exception{
 //BA.debugLineNum = 212;BA.debugLine="Public Sub getLeftPanel As B4XView";
 //BA.debugLineNum = 213;BA.debugLine="Return mLeftPanel";
if (true) return _mleftpanel;
 //BA.debugLineNum = 214;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname,anywheresoftware.b4a.objects.B4XViewWrapper _parent,int _sidewidth) throws Exception{
innerInitialize(_ba);
anywheresoftware.b4a.objects.TouchPanelCreator _creator = null;
anywheresoftware.b4a.objects.PanelWrapper _p = null;
 //BA.debugLineNum = 18;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 19;BA.debugLine="mEventName = EventName";
_meventname = _eventname;
 //BA.debugLineNum = 20;BA.debugLine="mCallBack = Callback";
_mcallback = _callback;
 //BA.debugLineNum = 21;BA.debugLine="mSideWidth = SideWidth";
_msidewidth = _sidewidth;
 //BA.debugLineNum = 23;BA.debugLine="Dim creator As TouchPanelCreator";
_creator = new anywheresoftware.b4a.objects.TouchPanelCreator();
 //BA.debugLineNum = 24;BA.debugLine="mBasePanel = creator.CreateTouchPanel(\"base\")";
_mbasepanel = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_creator.CreateTouchPanel(ba,"base").getObject()));
 //BA.debugLineNum = 31;BA.debugLine="Parent.AddView(mBasePanel, 0, 0, Parent.Width, Pa";
_parent.AddView((android.view.View)(_mbasepanel.getObject()),(int) (0),(int) (0),_parent.getWidth(),_parent.getHeight());
 //BA.debugLineNum = 32;BA.debugLine="mCenterPanel = xui.CreatePanel(\"\")";
_mcenterpanel = _xui.CreatePanel(ba,"");
 //BA.debugLineNum = 33;BA.debugLine="mBasePanel.AddView(mCenterPanel, 0, 0, mBasePanel";
_mbasepanel.AddView((android.view.View)(_mcenterpanel.getObject()),(int) (0),(int) (0),_mbasepanel.getWidth(),_mbasepanel.getHeight());
 //BA.debugLineNum = 34;BA.debugLine="mDarkPanel = xui.CreatePanel(\"dark\")";
_mdarkpanel = _xui.CreatePanel(ba,"dark");
 //BA.debugLineNum = 35;BA.debugLine="mBasePanel.AddView(mDarkPanel, 0, 0, mBasePanel.W";
_mbasepanel.AddView((android.view.View)(_mdarkpanel.getObject()),(int) (0),(int) (0),_mbasepanel.getWidth(),_mbasepanel.getHeight());
 //BA.debugLineNum = 36;BA.debugLine="mLeftPanel = xui.CreatePanel(\"\")";
_mleftpanel = _xui.CreatePanel(ba,"");
 //BA.debugLineNum = 37;BA.debugLine="mBasePanel.AddView(mLeftPanel, -SideWidth, 0, Sid";
_mbasepanel.AddView((android.view.View)(_mleftpanel.getObject()),(int) (-_sidewidth),(int) (0),_sidewidth,_mbasepanel.getHeight());
 //BA.debugLineNum = 38;BA.debugLine="mLeftPanel.Color = xui.Color_Red";
_mleftpanel.setColor(_xui.Color_Red);
 //BA.debugLineNum = 40;BA.debugLine="Dim p As Panel = mLeftPanel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
_p = (anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(_mleftpanel.getObject()));
 //BA.debugLineNum = 41;BA.debugLine="p.Elevation = 4dip";
_p.setElevation((float) (__c.DipToCurrent((int) (4))));
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public int  _offsettocolor(int _x) throws Exception{
float _visible = 0f;
 //BA.debugLineNum = 196;BA.debugLine="Private Sub OffsetToColor (x As Int) As Int";
 //BA.debugLineNum = 197;BA.debugLine="Dim Visible As Float = (mSideWidth + x) / mSideWi";
_visible = (float) ((_msidewidth+_x)/(double)_msidewidth);
 //BA.debugLineNum = 198;BA.debugLine="Return xui.Color_ARGB(100 * Visible, 0, 0, 0)";
if (true) return _xui.Color_ARGB((int) (100*_visible),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 199;BA.debugLine="End Sub";
return 0;
}
public String  _resize(int _width,int _height) throws Exception{
 //BA.debugLineNum = 220;BA.debugLine="Public Sub Resize(Width As Int, Height As Int)";
 //BA.debugLineNum = 221;BA.debugLine="If IsOpen Then ChangeOffset(-mSideWidth, False, T";
if (_isopen) { 
_changeoffset((float) (-_msidewidth),__c.False,__c.True);};
 //BA.debugLineNum = 222;BA.debugLine="mBasePanel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_mbasepanel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 223;BA.debugLine="mLeftPanel.SetLayoutAnimated(0, mLeftPanel.Left,";
_mleftpanel.SetLayoutAnimated((int) (0),_mleftpanel.getLeft(),(int) (0),_mleftpanel.getWidth(),_mbasepanel.getHeight());
 //BA.debugLineNum = 224;BA.debugLine="mDarkPanel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_mdarkpanel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 225;BA.debugLine="mCenterPanel.SetLayoutAnimated(0, 0, 0, Width, He";
_mcenterpanel.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 226;BA.debugLine="End Sub";
return "";
}
public String  _setleftopen(boolean _b) throws Exception{
float _x = 0f;
 //BA.debugLineNum = 205;BA.debugLine="Public Sub setLeftOpen (b As Boolean)";
 //BA.debugLineNum = 206;BA.debugLine="If b = IsOpen Then Return";
if (_b==_isopen) { 
if (true) return "";};
 //BA.debugLineNum = 207;BA.debugLine="Dim x As Float";
_x = 0f;
 //BA.debugLineNum = 208;BA.debugLine="If b Then x = 0 Else x = -mSideWidth";
if (_b) { 
_x = (float) (0);}
else {
_x = (float) (-_msidewidth);};
 //BA.debugLineNum = 209;BA.debugLine="ChangeOffset(x, False, False)";
_changeoffset(_x,__c.False,__c.False);
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
