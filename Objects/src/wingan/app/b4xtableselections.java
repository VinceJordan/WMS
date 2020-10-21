package wingan.app;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xtableselections extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "wingan.app.b4xtableselections");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", wingan.app.b4xtableselections.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public int _mode_single_cell_temp = 0;
public int _mode_single_cell_permanent = 0;
public int _mode_single_line_permanent = 0;
public int _mode_multiple_cells = 0;
public int _mode_multiple_lines = 0;
public boolean _linemode = false;
public boolean _singlemode = false;
public int _mcurrentmode = 0;
public wingan.app.b4xorderedmap _selectedlines = null;
public int _selectioncolor = 0;
public wingan.app.b4xtable _mtable = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public boolean _autoremoveinvisibleselections = false;
public int _selectedtextcolor = 0;
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
public String  _cellclicked(String _columnid,long _rowid) throws Exception{
boolean _removedselection = false;
anywheresoftware.b4a.objects.collections.List _selectedcells = null;
int _i = 0;
 //BA.debugLineNum = 22;BA.debugLine="Public Sub CellClicked (ColumnId As String, RowId";
 //BA.debugLineNum = 23;BA.debugLine="If mCurrentMode = MODE_SINGLE_CELL_TEMP Then Retu";
if (_mcurrentmode==_mode_single_cell_temp) { 
if (true) return "";};
 //BA.debugLineNum = 24;BA.debugLine="Dim RemovedSelection As Boolean";
_removedselection = false;
 //BA.debugLineNum = 25;BA.debugLine="If SelectedLines.ContainsKey(RowId) Then";
if (_selectedlines._containskey /*boolean*/ ((Object)(_rowid))) { 
 //BA.debugLineNum = 26;BA.debugLine="If LineMode Then";
if (_linemode) { 
 //BA.debugLineNum = 27;BA.debugLine="RemovedSelection = True";
_removedselection = __c.True;
 //BA.debugLineNum = 28;BA.debugLine="SelectedLines.Remove(RowId)";
_selectedlines._remove /*String*/ ((Object)(_rowid));
 }else {
 //BA.debugLineNum = 30;BA.debugLine="Dim SelectedCells As List = SelectedLines.Get(R";
_selectedcells = new anywheresoftware.b4a.objects.collections.List();
_selectedcells = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_selectedlines._get /*Object*/ ((Object)(_rowid))));
 //BA.debugLineNum = 31;BA.debugLine="Dim i As Int = SelectedCells.IndexOf(ColumnId)";
_i = _selectedcells.IndexOf((Object)(_columnid));
 //BA.debugLineNum = 32;BA.debugLine="If i > -1 Then";
if (_i>-1) { 
 //BA.debugLineNum = 33;BA.debugLine="RemovedSelection = True";
_removedselection = __c.True;
 //BA.debugLineNum = 34;BA.debugLine="SelectedCells.RemoveAt(i)";
_selectedcells.RemoveAt(_i);
 //BA.debugLineNum = 35;BA.debugLine="If SelectedCells.Size = 0 Then";
if (_selectedcells.getSize()==0) { 
 //BA.debugLineNum = 36;BA.debugLine="SelectedLines.Remove(RowId)";
_selectedlines._remove /*String*/ ((Object)(_rowid));
 };
 };
 };
 };
 //BA.debugLineNum = 41;BA.debugLine="If RemovedSelection = False Then";
if (_removedselection==__c.False) { 
 //BA.debugLineNum = 42;BA.debugLine="If SingleMode Then SelectedLines.Clear";
if (_singlemode) { 
_selectedlines._clear /*String*/ ();};
 //BA.debugLineNum = 43;BA.debugLine="If LineMode Then";
if (_linemode) { 
 //BA.debugLineNum = 44;BA.debugLine="SelectedLines.Put(RowId, \"\")";
_selectedlines._put /*String*/ ((Object)(_rowid),(Object)(""));
 }else {
 //BA.debugLineNum = 46;BA.debugLine="Dim SelectedCells As List";
_selectedcells = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 47;BA.debugLine="If SelectedLines.ContainsKey(RowId) Then";
if (_selectedlines._containskey /*boolean*/ ((Object)(_rowid))) { 
 //BA.debugLineNum = 48;BA.debugLine="SelectedCells = SelectedLines.Get(RowId)";
_selectedcells = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_selectedlines._get /*Object*/ ((Object)(_rowid))));
 }else {
 //BA.debugLineNum = 50;BA.debugLine="Dim SelectedCells As List";
_selectedcells = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 51;BA.debugLine="SelectedCells.Initialize";
_selectedcells.Initialize();
 //BA.debugLineNum = 52;BA.debugLine="SelectedLines.Put(RowId, SelectedCells)";
_selectedlines._put /*String*/ ((Object)(_rowid),(Object)(_selectedcells.getObject()));
 };
 //BA.debugLineNum = 54;BA.debugLine="SelectedCells.Add(ColumnId)";
_selectedcells.Add((Object)(_columnid));
 };
 };
 //BA.debugLineNum = 57;BA.debugLine="Refresh";
_refresh();
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Public MODE_SINGLE_CELL_TEMP = 1, MODE_SINGLE_CEL";
_mode_single_cell_temp = (int) (1);
_mode_single_cell_permanent = (int) (2);
_mode_single_line_permanent = (int) (3);
 //BA.debugLineNum = 4;BA.debugLine="Public MODE_MULTIPLE_CELLS = 4, MODE_MULTIPLE_LIN";
_mode_multiple_cells = (int) (4);
_mode_multiple_lines = (int) (5);
 //BA.debugLineNum = 5;BA.debugLine="Private LineMode, SingleMode As Boolean";
_linemode = false;
_singlemode = false;
 //BA.debugLineNum = 6;BA.debugLine="Private mCurrentMode As Int";
_mcurrentmode = 0;
 //BA.debugLineNum = 7;BA.debugLine="Public SelectedLines As B4XOrderedMap";
_selectedlines = new wingan.app.b4xorderedmap();
 //BA.debugLineNum = 8;BA.debugLine="Public SelectionColor As Int";
_selectioncolor = 0;
 //BA.debugLineNum = 9;BA.debugLine="Private mTable As B4XTable";
_mtable = new wingan.app.b4xtable();
 //BA.debugLineNum = 10;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 11;BA.debugLine="Public AutoRemoveInvisibleSelections As Boolean";
_autoremoveinvisibleselections = false;
 //BA.debugLineNum = 12;BA.debugLine="Public SelectedTextColor As Int = xui.Color_White";
_selectedtextcolor = _xui.Color_White;
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
public String  _clear() throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Public Sub Clear";
 //BA.debugLineNum = 127;BA.debugLine="SelectedLines.Clear";
_selectedlines._clear /*String*/ ();
 //BA.debugLineNum = 128;BA.debugLine="Refresh";
_refresh();
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public String  _firstselectedcolumnid() throws Exception{
anywheresoftware.b4a.objects.collections.List _l = null;
 //BA.debugLineNum = 146;BA.debugLine="Public Sub FirstSelectedColumnId As String";
 //BA.debugLineNum = 147;BA.debugLine="If SelectedLines.Size > 0 Then";
if (_selectedlines._getsize /*int*/ ()>0) { 
 //BA.debugLineNum = 148;BA.debugLine="Dim l As List = SelectedLines.Values.Get(0)";
_l = new anywheresoftware.b4a.objects.collections.List();
_l = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_selectedlines._getvalues /*anywheresoftware.b4a.objects.collections.List*/ ().Get((int) (0))));
 //BA.debugLineNum = 149;BA.debugLine="Return l.Get(0)";
if (true) return BA.ObjectToString(_l.Get((int) (0)));
 }else {
 //BA.debugLineNum = 151;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public long  _firstselectedrowid() throws Exception{
 //BA.debugLineNum = 137;BA.debugLine="Public Sub FirstSelectedRowId As Long";
 //BA.debugLineNum = 138;BA.debugLine="If SelectedLines.Size > 0 Then";
if (_selectedlines._getsize /*int*/ ()>0) { 
 //BA.debugLineNum = 139;BA.debugLine="Return SelectedLines.Keys.Get(0)";
if (true) return BA.ObjectToLongNumber(_selectedlines._getkeys /*anywheresoftware.b4a.objects.collections.List*/ ().Get((int) (0)));
 }else {
 //BA.debugLineNum = 141;BA.debugLine="Return 0";
if (true) return (long) (0);
 };
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return 0L;
}
public boolean  _getisselected() throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Public Sub getIsSelected As Boolean";
 //BA.debugLineNum = 133;BA.debugLine="Return SelectedLines.Size > 0";
if (true) return _selectedlines._getsize /*int*/ ()>0;
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return false;
}
public int  _getmode() throws Exception{
 //BA.debugLineNum = 95;BA.debugLine="Public Sub getMode As Int";
 //BA.debugLineNum = 96;BA.debugLine="Return mCurrentMode";
if (true) return _mcurrentmode;
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,wingan.app.b4xtable _table) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 15;BA.debugLine="Public Sub Initialize (Table As B4XTable)";
 //BA.debugLineNum = 16;BA.debugLine="mTable = Table";
_mtable = _table;
 //BA.debugLineNum = 17;BA.debugLine="SelectionColor = mTable.SelectionColor";
_selectioncolor = _mtable._selectioncolor /*int*/ ;
 //BA.debugLineNum = 18;BA.debugLine="SelectedLines.Initialize";
_selectedlines._initialize /*String*/ (ba);
 //BA.debugLineNum = 19;BA.debugLine="mCurrentMode = MODE_SINGLE_CELL_TEMP";
_mcurrentmode = _mode_single_cell_temp;
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public String  _refresh() throws Exception{
int _i = 0;
int _clr = 0;
long _rowid = 0L;
boolean _rowselected = false;
anywheresoftware.b4a.objects.collections.List _selectedcells = null;
wingan.app.b4xtable._b4xtablecolumn _col = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
anywheresoftware.b4a.objects.B4XViewWrapper _lbl = null;
anywheresoftware.b4a.objects.collections.List _rowstoremove = null;
 //BA.debugLineNum = 60;BA.debugLine="Public Sub Refresh";
 //BA.debugLineNum = 61;BA.debugLine="For i = 0 To mTable.VisibleRowIds.Size - 1";
{
final int step1 = 1;
final int limit1 = (int) (_mtable._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit1 ;_i = _i + step1 ) {
 //BA.debugLineNum = 62;BA.debugLine="Dim clr As Int";
_clr = 0;
 //BA.debugLineNum = 63;BA.debugLine="If i Mod 2 = 0 Then clr = mTable.EvenRowColor El";
if (_i%2==0) { 
_clr = _mtable._evenrowcolor /*int*/ ;}
else {
_clr = _mtable._oddrowcolor /*int*/ ;};
 //BA.debugLineNum = 64;BA.debugLine="Dim RowId As Long = mTable.VisibleRowIds.Get(i)";
_rowid = BA.ObjectToLongNumber(_mtable._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 65;BA.debugLine="Dim RowSelected As Boolean = SelectedLines.Conta";
_rowselected = _selectedlines._containskey /*boolean*/ ((Object)(_rowid));
 //BA.debugLineNum = 66;BA.debugLine="If RowSelected And LineMode = False Then";
if (_rowselected && _linemode==__c.False) { 
 //BA.debugLineNum = 67;BA.debugLine="Dim SelectedCells As List = SelectedLines.Get(R";
_selectedcells = new anywheresoftware.b4a.objects.collections.List();
_selectedcells = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_selectedlines._get /*Object*/ ((Object)(_rowid))));
 };
 //BA.debugLineNum = 69;BA.debugLine="For Each col As B4XTableColumn In mTable.Columns";
{
final anywheresoftware.b4a.BA.IterableList group9 = _mtable._columns /*anywheresoftware.b4a.objects.collections.List*/ ;
final int groupLen9 = group9.getSize()
;int index9 = 0;
;
for (; index9 < groupLen9;index9++){
_col = (wingan.app.b4xtable._b4xtablecolumn)(group9.Get(index9));
 //BA.debugLineNum = 70;BA.debugLine="Dim p As B4XView = col.CellsLayouts.Get(i + 1)";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_col.CellsLayouts /*anywheresoftware.b4a.objects.collections.List*/ .Get((int) (_i+1))));
 //BA.debugLineNum = 71;BA.debugLine="Dim lbl As B4XView = p.GetView(col.LabelIndex)";
_lbl = new anywheresoftware.b4a.objects.B4XViewWrapper();
_lbl = _p.GetView(_col.LabelIndex /*int*/ );
 //BA.debugLineNum = 72;BA.debugLine="If RowSelected And (LineMode Or SelectedCells.I";
if (_rowselected && (_linemode || _selectedcells.IndexOf((Object)(_col.Id /*String*/ ))>-1)) { 
 //BA.debugLineNum = 73;BA.debugLine="p.Color = SelectionColor";
_p.setColor(_selectioncolor);
 //BA.debugLineNum = 74;BA.debugLine="lbl.TextColor = SelectedTextColor";
_lbl.setTextColor(_selectedtextcolor);
 }else {
 //BA.debugLineNum = 76;BA.debugLine="p.Color = clr";
_p.setColor(_clr);
 //BA.debugLineNum = 77;BA.debugLine="lbl.TextColor = mTable.TextColor";
_lbl.setTextColor(_mtable._textcolor /*int*/ );
 };
 }
};
 }
};
 //BA.debugLineNum = 81;BA.debugLine="If AutoRemoveInvisibleSelections Then";
if (_autoremoveinvisibleselections) { 
 //BA.debugLineNum = 82;BA.debugLine="Dim RowsToRemove As List";
_rowstoremove = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 83;BA.debugLine="RowsToRemove.Initialize";
_rowstoremove.Initialize();
 //BA.debugLineNum = 84;BA.debugLine="For Each RowId As Long In SelectedLines.Keys";
{
final anywheresoftware.b4a.BA.IterableList group24 = _selectedlines._getkeys /*anywheresoftware.b4a.objects.collections.List*/ ();
final int groupLen24 = group24.getSize()
;int index24 = 0;
;
for (; index24 < groupLen24;index24++){
_rowid = BA.ObjectToLongNumber(group24.Get(index24));
 //BA.debugLineNum = 85;BA.debugLine="If mTable.VisibleRowIds.IndexOf(RowId) = -1 The";
if (_mtable._visiblerowids /*anywheresoftware.b4a.objects.collections.List*/ .IndexOf((Object)(_rowid))==-1) { 
 //BA.debugLineNum = 86;BA.debugLine="RowsToRemove.Add(RowId)";
_rowstoremove.Add((Object)(_rowid));
 };
 }
};
 //BA.debugLineNum = 89;BA.debugLine="For Each RowId As Long In RowsToRemove";
{
final anywheresoftware.b4a.BA.IterableList group29 = _rowstoremove;
final int groupLen29 = group29.getSize()
;int index29 = 0;
;
for (; index29 < groupLen29;index29++){
_rowid = BA.ObjectToLongNumber(group29.Get(index29));
 //BA.debugLineNum = 90;BA.debugLine="SelectedLines.Remove(RowId)";
_selectedlines._remove /*String*/ ((Object)(_rowid));
 }
};
 };
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return "";
}
public String  _setmode(int _m) throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Public Sub setMode (m As Int)";
 //BA.debugLineNum = 100;BA.debugLine="mCurrentMode = m";
_mcurrentmode = _m;
 //BA.debugLineNum = 101;BA.debugLine="SelectedLines.Clear";
_selectedlines._clear /*String*/ ();
 //BA.debugLineNum = 102;BA.debugLine="If mCurrentMode = MODE_SINGLE_CELL_TEMP Then";
if (_mcurrentmode==_mode_single_cell_temp) { 
 //BA.debugLineNum = 103;BA.debugLine="mTable.SelectionColor = SelectionColor";
_mtable._selectioncolor /*int*/  = _selectioncolor;
 //BA.debugLineNum = 104;BA.debugLine="mTable.HighlightSearchResults = True";
_mtable._highlightsearchresults /*boolean*/  = __c.True;
 }else {
 //BA.debugLineNum = 106;BA.debugLine="mTable.SelectionColor = xui.Color_Transparent";
_mtable._selectioncolor /*int*/  = _xui.Color_Transparent;
 //BA.debugLineNum = 107;BA.debugLine="mTable.HighlightSearchResults = False";
_mtable._highlightsearchresults /*boolean*/  = __c.False;
 };
 //BA.debugLineNum = 109;BA.debugLine="Select mCurrentMode";
switch (BA.switchObjectToInt(_mcurrentmode,_mode_single_cell_permanent,_mode_single_line_permanent,_mode_multiple_cells,_mode_multiple_lines)) {
case 0: {
 //BA.debugLineNum = 111;BA.debugLine="SingleMode = True";
_singlemode = __c.True;
 //BA.debugLineNum = 112;BA.debugLine="LineMode = False";
_linemode = __c.False;
 break; }
case 1: {
 //BA.debugLineNum = 114;BA.debugLine="SingleMode = True";
_singlemode = __c.True;
 //BA.debugLineNum = 115;BA.debugLine="LineMode = True";
_linemode = __c.True;
 break; }
case 2: {
 //BA.debugLineNum = 117;BA.debugLine="SingleMode = False";
_singlemode = __c.False;
 //BA.debugLineNum = 118;BA.debugLine="LineMode = False";
_linemode = __c.False;
 break; }
case 3: {
 //BA.debugLineNum = 120;BA.debugLine="SingleMode = False";
_singlemode = __c.False;
 //BA.debugLineNum = 121;BA.debugLine="LineMode = True";
_linemode = __c.True;
 break; }
}
;
 //BA.debugLineNum = 123;BA.debugLine="Refresh";
_refresh();
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
