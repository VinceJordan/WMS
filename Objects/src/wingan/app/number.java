package wingan.app;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class number {
private static number mostCurrent = new number();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static String _tempformat = "";
public static String _tempformat1 = "";
public static double _numtemp = 0;
public static int _explen = 0;
public static int _lastsep = 0;
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
public wingan.app.receiving_module _receiving_module = null;
public wingan.app.receiving2_module _receiving2_module = null;
public wingan.app.scale_calc _scale_calc = null;
public wingan.app.starter _starter = null;
public wingan.app.httputils2service _httputils2service = null;
public wingan.app.b4xcollections _b4xcollections = null;
public static String  _engineeringnum(anywheresoftware.b4a.BA _ba,double _num,int _minint,int _maxdec,int _mindec,String _decsep,String _fstthousep,String _otherthousep,int _exptype,int _maxstrlen) throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub EngineeringNum(Num As Double,MinInt As Int,Max";
 //BA.debugLineNum = 118;BA.debugLine="Do While Abs(NumTemp) < 1";
while (anywheresoftware.b4a.keywords.Common.Abs(_numtemp)<1) {
 //BA.debugLineNum = 119;BA.debugLine="NumTemp = NumTemp * 1000";
_numtemp = _numtemp*1000;
 //BA.debugLineNum = 120;BA.debugLine="Explen = Explen - 3";
_explen = (int) (_explen-3);
 }
;
 //BA.debugLineNum = 122;BA.debugLine="Do While Abs(NumTemp) > 1000";
while (anywheresoftware.b4a.keywords.Common.Abs(_numtemp)>1000) {
 //BA.debugLineNum = 123;BA.debugLine="NumTemp = NumTemp/1000";
_numtemp = _numtemp/(double)1000;
 //BA.debugLineNum = 124;BA.debugLine="Explen = Explen + 3";
_explen = (int) (_explen+3);
 }
;
 //BA.debugLineNum = 126;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, MaxDe";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 127;BA.debugLine="If Explen <> 0 Then TempFormat = TempFormat&\"E\"&E";
if (_explen!=0) { 
_tempformat = _tempformat+"E"+BA.NumberToString(_explen);};
 //BA.debugLineNum = 128;BA.debugLine="Do While TempFormat.Length > MaxStrLen And MaxDec";
while (_tempformat.length()>_maxstrlen && _maxdec>_mindec) {
 //BA.debugLineNum = 129;BA.debugLine="MaxDec = MaxDec - 1";
_maxdec = (int) (_maxdec-1);
 //BA.debugLineNum = 130;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, MaxD";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 131;BA.debugLine="If Explen <> 0 Then TempFormat = TempFormat&\"E\"&";
if (_explen!=0) { 
_tempformat = _tempformat+"E"+BA.NumberToString(_explen);};
 }
;
 //BA.debugLineNum = 133;BA.debugLine="Return TempFormat";
if (true) return _tempformat;
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _floatnum(anywheresoftware.b4a.BA _ba,double _num,int _minint,int _maxdec,int _mindec,String _decsep,String _fstthousep,String _otherthousep,int _exptype,int _maxstrlen) throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Sub FloatNum(Num As Double,MinInt As Int,MaxDec As";
 //BA.debugLineNum = 60;BA.debugLine="TempFormat = NumberFormat2(Num, MinInt, MaxDec,Mi";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_num,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 61;BA.debugLine="If TempFormat.Length > MaxStrLen Then";
if (_tempformat.length()>_maxstrlen) { 
 //BA.debugLineNum = 62;BA.debugLine="If Abs(NumTemp) > 10 Then  '99999";
if (anywheresoftware.b4a.keywords.Common.Abs(_numtemp)>10) { 
 //BA.debugLineNum = 63;BA.debugLine="Do While TempFormat.Length > MaxStrLen And Abs(";
while (_tempformat.length()>_maxstrlen && anywheresoftware.b4a.keywords.Common.Abs(_numtemp)>10) {
 //BA.debugLineNum = 64;BA.debugLine="NumTemp = NumTemp/10";
_numtemp = _numtemp/(double)10;
 //BA.debugLineNum = 65;BA.debugLine="Explen = Explen + 1";
_explen = (int) (_explen+1);
 //BA.debugLineNum = 66;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, Ma";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 67;BA.debugLine="If Explen <> 0 Then TempFormat = TempFormat&\"E";
if (_explen!=0) { 
_tempformat = _tempformat+"E"+BA.NumberToString(_explen);};
 }
;
 //BA.debugLineNum = 69;BA.debugLine="Do While TempFormat.Length > MaxStrLen And MaxD";
while (_tempformat.length()>_maxstrlen && _maxdec>_mindec) {
 //BA.debugLineNum = 70;BA.debugLine="MaxDec = MaxDec - 1";
_maxdec = (int) (_maxdec-1);
 //BA.debugLineNum = 71;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, Ma";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 72;BA.debugLine="If Explen <> 0 Then TempFormat = TempFormat&\"E";
if (_explen!=0) { 
_tempformat = _tempformat+"E"+BA.NumberToString(_explen);};
 }
;
 }else if(anywheresoftware.b4a.keywords.Common.Abs(_numtemp)<1 && _numtemp!=0) { 
 //BA.debugLineNum = 76;BA.debugLine="Do While TempFormat.Length > MaxStrLen And Abs(";
while (_tempformat.length()>_maxstrlen && anywheresoftware.b4a.keywords.Common.Abs(_numtemp)<1) {
 //BA.debugLineNum = 77;BA.debugLine="NumTemp = NumTemp * 10";
_numtemp = _numtemp*10;
 //BA.debugLineNum = 78;BA.debugLine="Explen = Explen - 1";
_explen = (int) (_explen-1);
 //BA.debugLineNum = 79;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, Ma";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 80;BA.debugLine="If Explen <> 0 Then TempFormat = TempFormat&\"E";
if (_explen!=0) { 
_tempformat = _tempformat+"E"+BA.NumberToString(_explen);};
 }
;
 //BA.debugLineNum = 82;BA.debugLine="Do While TempFormat.Length > MaxStrLen And MaxD";
while (_tempformat.length()>_maxstrlen && _maxdec>_mindec) {
 //BA.debugLineNum = 83;BA.debugLine="MaxDec = MaxDec - 1";
_maxdec = (int) (_maxdec-1);
 //BA.debugLineNum = 84;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, Ma";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 85;BA.debugLine="If Explen <> 0 Then TempFormat = TempFormat&\"E";
if (_explen!=0) { 
_tempformat = _tempformat+"E"+BA.NumberToString(_explen);};
 }
;
 }else {
 //BA.debugLineNum = 89;BA.debugLine="Do While TempFormat.Length > MaxStrLen And MaxD";
while (_tempformat.length()>_maxstrlen && _maxdec>_mindec) {
 //BA.debugLineNum = 90;BA.debugLine="MaxDec = MaxDec - 1";
_maxdec = (int) (_maxdec-1);
 //BA.debugLineNum = 91;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, Ma";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 }
;
 };
 };
 //BA.debugLineNum = 95;BA.debugLine="Return TempFormat";
if (true) return _tempformat;
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _format3(anywheresoftware.b4a.BA _ba,double _num,int _minint,int _maxdec,int _mindec,String _decsep,String _fstthousep,String _otherthousep,int _exptype,int _maxstrlen) throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Format3(Num As Double,MinInt As Int,MaxDec As";
 //BA.debugLineNum = 30;BA.debugLine="NumTemp = Num";
_numtemp = _num;
 //BA.debugLineNum = 31;BA.debugLine="Explen = 0";
_explen = (int) (0);
 //BA.debugLineNum = 33;BA.debugLine="Select ExpType";
switch (_exptype) {
case 0: {
 //BA.debugLineNum = 35;BA.debugLine="TempFormat = FloatNum(Num, MinInt, MaxDec, MinD";
_tempformat = _floatnum(_ba,_num,_minint,_maxdec,_mindec,_decsep,_fstthousep,_otherthousep,_exptype,_maxstrlen);
 break; }
case 1: {
 //BA.debugLineNum = 38;BA.debugLine="TempFormat = ScientificNum(Num ,MinInt ,MaxDec";
_tempformat = _scientificnum(_ba,_num,_minint,_maxdec,_mindec,_decsep,_fstthousep,_otherthousep,_exptype,_maxstrlen);
 break; }
case 2: {
 //BA.debugLineNum = 41;BA.debugLine="TempFormat = EngineeringNum(Num ,MinInt ,MaxDec";
_tempformat = _engineeringnum(_ba,_num,_minint,_maxdec,_mindec,_decsep,_fstthousep,_otherthousep,_exptype,_maxstrlen);
 break; }
}
;
 //BA.debugLineNum = 46;BA.debugLine="TempFormat = TempFormat.Replace(\".\", \"?\")";
_tempformat = _tempformat.replace(".","?");
 //BA.debugLineNum = 47;BA.debugLine="TempFormat = TempFormat.Replace(\",\", \"#\")";
_tempformat = _tempformat.replace(",","#");
 //BA.debugLineNum = 48;BA.debugLine="If TempFormat.Contains(\"#\") Then";
if (_tempformat.contains("#")) { 
 //BA.debugLineNum = 49;BA.debugLine="LastSep = TempFormat.LastIndexOf(\"#\")";
_lastsep = _tempformat.lastIndexOf("#");
 //BA.debugLineNum = 50;BA.debugLine="TempFormat = TempFormat.SubString2(0,LastSep)&Fs";
_tempformat = _tempformat.substring((int) (0),_lastsep)+_fstthousep+_tempformat.substring((int) (_lastsep+1),_tempformat.length());
 };
 //BA.debugLineNum = 52;BA.debugLine="TempFormat = TempFormat.Replace(\"?\", DecSep)";
_tempformat = _tempformat.replace("?",_decsep);
 //BA.debugLineNum = 53;BA.debugLine="TempFormat = TempFormat.Replace(\"#\", OtherThouSep";
_tempformat = _tempformat.replace("#",_otherthousep);
 //BA.debugLineNum = 54;BA.debugLine="Return TempFormat";
if (true) return _tempformat;
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Public TempFormat, TempFormat1 As String";
_tempformat = "";
_tempformat1 = "";
 //BA.debugLineNum = 7;BA.debugLine="Private NumTemp As Double";
_numtemp = 0;
 //BA.debugLineNum = 8;BA.debugLine="Private Explen, LastSep As Int";
_explen = 0;
_lastsep = 0;
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public static String  _scientificnum(anywheresoftware.b4a.BA _ba,double _num,int _minint,int _maxdec,int _mindec,String _decsep,String _fstthousep,String _otherthousep,int _exptype,int _maxstrlen) throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub ScientificNum(Num As Double,MinInt As Int,MaxD";
 //BA.debugLineNum = 99;BA.debugLine="Do While Abs(NumTemp) < 1";
while (anywheresoftware.b4a.keywords.Common.Abs(_numtemp)<1) {
 //BA.debugLineNum = 100;BA.debugLine="NumTemp = NumTemp * 10";
_numtemp = _numtemp*10;
 //BA.debugLineNum = 101;BA.debugLine="Explen = Explen - 1";
_explen = (int) (_explen-1);
 }
;
 //BA.debugLineNum = 103;BA.debugLine="Do While Abs(NumTemp) > 10";
while (anywheresoftware.b4a.keywords.Common.Abs(_numtemp)>10) {
 //BA.debugLineNum = 104;BA.debugLine="NumTemp = NumTemp/10";
_numtemp = _numtemp/(double)10;
 //BA.debugLineNum = 105;BA.debugLine="Explen = Explen + 1";
_explen = (int) (_explen+1);
 }
;
 //BA.debugLineNum = 107;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, MaxDe";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 108;BA.debugLine="If Explen <> 0 Then TempFormat = TempFormat&\"E\"&E";
if (_explen!=0) { 
_tempformat = _tempformat+"E"+BA.NumberToString(_explen);};
 //BA.debugLineNum = 109;BA.debugLine="Do While TempFormat.Length > MaxStrLen And MaxDec";
while (_tempformat.length()>_maxstrlen && _maxdec>_mindec) {
 //BA.debugLineNum = 110;BA.debugLine="MaxDec = MaxDec - 1";
_maxdec = (int) (_maxdec-1);
 //BA.debugLineNum = 111;BA.debugLine="TempFormat = NumberFormat2(NumTemp, MinInt, MaxD";
_tempformat = anywheresoftware.b4a.keywords.Common.NumberFormat2(_numtemp,_minint,_maxdec,_mindec,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 112;BA.debugLine="If Explen <> 0 Then TempFormat = TempFormat&\"E\"&";
if (_explen!=0) { 
_tempformat = _tempformat+"E"+BA.NumberToString(_explen);};
 }
;
 //BA.debugLineNum = 114;BA.debugLine="Return TempFormat";
if (true) return _tempformat;
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
}
