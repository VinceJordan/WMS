B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=9.801
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

	#Extends: android.support.v7.app.AppCompatActivity

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
	Dim connection As SQL
	Dim cursor1 As Cursor
	Dim cursor2 As Cursor
	Dim cursor3 As Cursor
	Dim cursor4 As Cursor
	Dim cursor5 As Cursor
	Dim cursor6 As Cursor
	
	Public transaction_id As String
	Dim principal_acronym As String
	Dim principal_id As String
	Dim principal_name As String
	
	Private plusBitmap As Bitmap
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Private cvs As B4XCanvas
    Private xui As XUI
    Private NameColumn(6) As B4XTableColumn
	
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	
	Private B4XTable1 As B4XTable
	Private XSelections As B4XTableSelections
'	Private SelectionColor As Int = 0xFF009DFF
'	Private LastSelection As Int
	Private PANEL_BG_NEW As Panel
	Private LABEL_LOAD_INVDATE As Label
	Private CMB_PRINCIPAL As B4XComboBox
	Private CMB_WAREHOUSE As B4XComboBox
	Private CMB_AREA As B4XComboBox
	Private Dialog As B4XDialog
	Private Base As B4XView
	Private DateTemplate As B4XDateTemplate
	Private LABEL_NEW_HEADER As Label
	Private BUTTON_CREATE As Button
End Sub

#If Java
	public boolean _onCreateOptionsMenu(android.view.Menu menu) {
		if (processBA.subExists("activity_createmenu")) {
			processBA.raiseEvent2(null, true, "activity_createmenu", false, new de.amberhome.objects.appcompat.ACMenuWrapper(menu));
			return true;
		}
		else
			return false;
	}
#End If

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("monthly")
	
	plusBitmap = LoadBitmap(File.DirAssets, "add.png")
	
	
	ToolbarHelper.Initialize
	ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadBitmap(File.DirAssets, "LOGO_3D.png"))
	ToolbarHelper.ShowUpIndicator = True 'set to true to show the up arrow
	Dim bd As BitmapDrawable
	bd.Initialize(LoadBitmap(File.DirAssets, "back.png"))
	ToolbarHelper.UpIndicatorDrawable =  bd
	ACToolBarLight1.InitMenuListener
	
	Dim jo As JavaObject = ACToolBarLight1
	jo.RunMethod("setContentInsetStartWithNavigation", Array(5dip))
	jo.RunMethod("setTitleMarginStart", Array(10dip))

	If connection.IsInitialized = False Then
		connection.Initialize(File.DirRootExternal & "/WING AN APP/","tablet_db.db", False)
	End If
	
	Dim p As B4XView = xui.CreatePanel("")
	p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)
	cvs.Initialize(p)
	
	Base = Activity
	Dialog.Initialize (Base)
	Dialog.BorderColor = Colors.Transparent
	Dialog.BorderCornersRadius = 5
	Dialog.TitleBarColor = Colors.RGB(82,169,255)
	Dialog.TitleBarTextColor = Colors.White
	Dialog.BackgroundColor = Colors.White
	Dialog.ButtonsColor = Colors.White
	Dialog.BodyTextColor = Colors.Black
	
	DateTemplate.Initialize
	DateTemplate.MinYear = 2016
	DateTemplate.MaxYear = 2030
	DateTemplate.lblMonth.TextColor = Colors.Black
	DateTemplate.lblYear.TextColor = Colors.Black
	DateTemplate.btnMonthLeft.Color = Colors.RGB(82,169,255)
	DateTemplate.btnMonthRight.Color = Colors.RGB(82,169,255)
	DateTemplate.btnYearLeft.Color = Colors.RGB(82,169,255)
	DateTemplate.btnYearRight.Color = Colors.RGB(82,169,255)
	DateTemplate.DaysInMonthColor = Colors.Black

	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	Sleep(0)
	LOAD_INVDATE_HEADER
	LOAD_INVDATE
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "create", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("create", plusBitmap)
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub OpenSpinner(se As Spinner)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
End Sub
Sub OpenLabel(se As Label)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
End Sub

Sub LOAD_INVDATE_HEADER
	NameColumn(0)=B4XTable1.AddColumn("Inventory Date", B4XTable1.COLUMN_TYPE_TEXT)
	NameColumn(1)=B4XTable1.AddColumn("Principal", B4XTable1.COLUMN_TYPE_TEXT)
	NameColumn(2)=B4XTable1.AddColumn("Warehouse", B4XTable1.COLUMN_TYPE_TEXT)
	NameColumn(3)=B4XTable1.AddColumn("Area", B4XTable1.COLUMN_TYPE_TEXT)
	NameColumn(4)=B4XTable1.AddColumn("User", B4XTable1.COLUMN_TYPE_TEXT)
	NameColumn(5)=B4XTable1.AddColumn("Status", B4XTable1.COLUMN_TYPE_TEXT)
	
	B4XTable1.NumberOfFrozenColumns = 1
End Sub
Sub LOAD_INVDATE
	Sleep(0)
	Dim Data As List
	Data.Initialize
	Dim rs As ResultSet = connection.ExecQuery("SELECT * FROM inventory_ref_table GROUP BY transaction_id ORDER by inventory_date DESC")
	Do While rs.NextRow
		Dim row(6) As Object
		row(0) = rs.GetString("inventory_date")
		row(1) = rs.GetString("principal_name")
		row(2) = rs.GetString("warehouse")
'		'Some of the values are Null. We need to convert them to empty strings:
'		If row(2) = Null Then row(2) = ""
		row(3) = rs.GetString("area")
		row(4) = rs.GetString("user_info")
		row(5) = rs.GetString("transaction_status")
		Data.Add(row)
	Loop
	rs.Close
	B4XTable1.SetData(Data)
	If XSelections.IsInitialized = False Then
	XSelections.Initialize(B4XTable1)
	XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
End Sub
Sub B4XTable1_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0), NameColumn(1), NameColumn(2), NameColumn(3),NameColumn(4),NameColumn(5))
		Dim MaxWidth As Int
		For i = 0 To B4XTable1.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 10dip)
		Next
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
	Next
	If ShouldRefresh Then
		B4XTable1.Refresh
		XSelections.Clear
	End If
End Sub
Sub B4XTable1_CellClicked (ColumnId As String, RowId As Long)
'	Dim Index As Int = B4XTable1.VisibleRowIds.IndexOf(RowId)
'	SetRowColor(Index)
'	LastSelection = Index

	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = B4XTable1.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)

	Log(RowData.Get("Inventory Date"))
	Log(RowData.Get("Principal"))
	Log(RowData.Get("Warehouse"))
	Log(RowData.Get("Area"))
	
	Msgbox2Async("Inventory Date : " & RowData.Get("Inventory Date") & CRLF _
	& "Principal : " & RowData.Get("Principal") & CRLF _
	& "Warehouse : " & RowData.Get("Warehouse") & CRLF _
	& "Area : " & RowData.Get("Area") & CRLF _
	, "Option", "Continue", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Dim query As String = "SELECT * FROM inventory_ref_table WHERE inventory_date = ? AND warehouse = ? AND area = ? AND principal_name = ?"
		cursor1 = connection.ExecQuery2(query,Array As String(RowData.Get("Inventory Date"),RowData.Get("Warehouse"),RowData.Get("Area"),RowData.Get("Principal")))
		For i = 0 To cursor1.RowCount - 1
			Sleep(0)
			cursor1.Position = i
			transaction_id = cursor1.GetString("transaction_id")
			principal_id = cursor1.GetString("principal_id")
			principal_name = cursor1.GetString("principal_name")
		Next
		B4XTable1.Refresh
		StartActivity(MONTLHY_INVENTORY2_MODULE)
		SetAnimation("right_to_center", "center_to_left")
		MONTLHY_INVENTORY2_MODULE.cmb_trigger = 1
	Else if Result = DialogResponse.NEGATIVE Then
	
	Else if Result = DialogResponse.CANCEL Then
		B4XTable1.Refresh
	End If
End Sub
Sub B4XTable1_CellLongClicked (ColumnId As String, RowId As Long)
'	Dim Index As Int = B4XTable1.VisibleRowIds.IndexOf(RowId)
'	SetRowColor(Index)
'	LastSelection = Index

	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = B4XTable1.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)

	Log(RowData.Get("Inventory Date"))
	Log(RowData.Get("Principal"))
	Log(RowData.Get("Warehouse"))
	Log(RowData.Get("Area"))
	
	Msgbox2Async("Inventory Date : " & RowData.Get("Inventory Date") & CRLF _
	& "Principal : " & RowData.Get("Principal") & CRLF _
	& "Warehouse : " & RowData.Get("Warehouse") & CRLF _
	& "Area : " & RowData.Get("Area") & CRLF _
	, "Option", "Edit", "", "Delete", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		POPULATE_PRINCIPAL
		Sleep(100)
		CMB_PRINCIPAL.SelectedIndex = 0
		Sleep(100)
		POPULATE_WAREHOUSE
		Sleep(100)
		CMB_WAREHOUSE.SelectedIndex = 0
		Sleep(100)
		POPULATE_AREA
		Sleep(100)
		CMB_AREA.SelectedIndex = 0
		Sleep(100)
		
		Dim query As String = "SELECT * FROM inventory_ref_table WHERE inventory_date = ? AND warehouse = ? AND area = ? AND principal_name = ?"
		cursor6 = connection.ExecQuery2(query,Array As String(RowData.Get("Inventory Date"),RowData.Get("Warehouse"),RowData.Get("Area"),RowData.Get("Principal")))
		For i = 0 To cursor6.RowCount - 1
			cursor6.Position = i
			transaction_id = cursor6.GetString("transaction_id")
			Log(CMB_PRINCIPAL.cmbBox.IndexOf(cursor6.GetString("principal_name")))
			Log(CMB_WAREHOUSE.cmbBox.IndexOf(cursor6.GetString("warehouse")))
			Log(CMB_AREA.cmbBox.IndexOf(cursor6.GetString("area")))
			CMB_PRINCIPAL.SelectedIndex = CMB_PRINCIPAL.cmbBox.IndexOf(cursor6.GetString("principal_name"))
			CMB_WAREHOUSE.SelectedIndex = CMB_WAREHOUSE.cmbBox.IndexOf(cursor6.GetString("warehouse"))
			CMB_AREA.SelectedIndex = CMB_AREA.cmbBox.IndexOf(cursor6.GetString("area"))
		Next
		Sleep(0)
		LABEL_LOAD_INVDATE.Text = RowData.Get("Inventory Date")
		PANEL_BG_NEW.SetVisibleAnimated(300, True)
		PANEL_BG_NEW.BringToFront
		LABEL_NEW_HEADER.Text = "Editing Transaction"
		BUTTON_CREATE.Text = " Edit"

	Else if Result = DialogResponse.NEGATIVE Then
		Msgbox2Async("Are you sure you want to delete this transaction? All data in this transaction will be lost.", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Dim insert_query As String = "INSERT INTO inventory_ref_table_trail SELECT transaction_id,principal_name,principal_id,inventory_date,warehouse,area,user_info,tab_id,date_registered,time_registered,transaction_status,edit_count,? as 'edit_by','DELETED' as 'edit_type' from inventory_ref_table WHERE inventory_date = ? AND warehouse = ? AND area = ? AND principal_name = ?  "
			connection.ExecNonQuery2(insert_query,Array As String(LOGIN_MODULE.username, RowData.Get("Inventory Date"),RowData.Get("Warehouse"),RowData.Get("Area"),RowData.Get("Principal")))
			Sleep(0)
			Dim query As String = "DELETE from inventory_ref_table WHERE inventory_date = ? AND warehouse = ? AND area = ? AND principal_name = ?"
			connection.ExecNonQuery2(query,Array As String(RowData.Get("Inventory Date"),RowData.Get("Warehouse"),RowData.Get("Area"),RowData.Get("Principal")))
			ToastMessageShow("Deleted Successfully", True)
			LOAD_INVDATE
			PANEL_BG_NEW.SetVisibleAnimated(300,False)
		End If
	Else

	End If
End Sub

Sub PANEL_BG_NEW_Click
	Return True
End Sub
Sub POPULATE_PRINCIPAL
	CMB_PRINCIPAL.cmbBox.Clear
	CMB_PRINCIPAL.cmbBox.DropdownTextColor = Colors.Black
	CMB_PRINCIPAL.cmbBox.TextColor = Colors.White
	CMB_PRINCIPAL.cmbBox.Add("ALL")
	cursor1 = connection.ExecQuery("SELECT principal_name FROM principal_table ORDER BY principal_name ASC")
	For i = 0 To cursor1.RowCount - 1
		Sleep(0)
		cursor1.Position = i
		CMB_PRINCIPAL.cmbBox.Add(cursor1.GetString("principal_name"))
	Next
End Sub
Sub POPULATE_WAREHOUSE
	CMB_WAREHOUSE.cmbBox.Clear
	CMB_WAREHOUSE.cmbBox.DropdownTextColor = Colors.Black
	CMB_WAREHOUSE.cmbBox.TextColor = Colors.White
	cursor2 = connection.ExecQuery("SELECT warehouse_name FROM warehouse_table ORDER BY warehouse_name ASC")
	For i = 0 To cursor2.RowCount - 1
		Sleep(0)
		cursor2.Position = i
		CMB_WAREHOUSE.cmbBox.Add(cursor2.GetString("warehouse_name"))
	Next
End Sub
Sub POPULATE_AREA
	CMB_AREA.cmbBox.Clear
	CMB_AREA.cmbBox.DropdownTextColor = Colors.Black
	CMB_AREA.cmbBox.TextColor = Colors.White
	CMB_AREA.cmbBox.Add("WAREHOUSE 1")
	CMB_AREA.cmbBox.Add("WAREHOUSE 2")
	CMB_AREA.cmbBox.Add("WAREHOUSE 3")
End Sub
Sub CMB_PRINCIPAL_SelectedIndexChanged (Index As Int)
	POPULATE_WAREHOUSE
	Sleep(0)
	OpenSpinner(CMB_WAREHOUSE.cmbBox)
End Sub
Sub CMB_WAREHOUSE_SelectedIndexChanged (Index As Int)
	POPULATE_AREA
	Sleep(0)
	OpenSpinner(CMB_AREA.cmbBox)
End Sub
Sub LABEL_LOAD_INVDATE_Click
	Dialog.Title = "Select Date"
	Wait For (Dialog.ShowTemplate(DateTemplate, "", "", "CANCEL")) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_INVDATE.Text = DateTime.Date(DateTemplate.Date)
	End If
End Sub
Sub BUTTON_CANCEL_Click
	PANEL_BG_NEW.SetVisibleAnimated(300,False)
End Sub
Sub GET_PRINCIPALID
	If CMB_PRINCIPAL.cmbBox.SelectedIndex <> CMB_PRINCIPAL.cmbBox.IndexOf("ALL") Then
		cursor3 = connection.ExecQuery("SELECT * FROM principal_table WHERE principal_name ='"& CMB_PRINCIPAL.cmbBox.SelectedItem &"'")
		For i = 0 To cursor3.RowCount - 1
			Sleep(0)
			cursor3.Position = i
		
			principal_acronym = cursor3.GetString("principal_acronym")
			principal_id = cursor3.GetString("principal_id")

		Next
	Else
		principal_acronym = "ALL"
		principal_id = "ALL"
	End If
End Sub
Sub GET_TRANSACTIONID
	Dim CExpDate As String = DateTime.GetYear(DateTime.Now)
	Dim Cxmo As String = DateTime.GetMonth(DateTime.Now)
	Dim Cxday As String = DateTime.GetDayOfMonth(DateTime.Now)
	Dim Cxtime As String = DateTime.GetHour(DateTime.Now)
	Dim Cxmin As String = DateTime.GetMinute(DateTime.Now)
	Dim Cxsecs As String = DateTime.GetSecond(DateTime.Now)	
	Sleep(0)
	transaction_id = principal_acronym&CExpDate&Cxmo&Cxday&Cxtime&Cxmin&Cxsecs&LOGIN_MODULE.tab_id	
	Sleep(0)
	ToastMessageShow(transaction_id, True)
End Sub
Sub BUTTON_CREATE_Click
	If BUTTON_CREATE.Text = " Edit" Then
		Msgbox2Async("Are you sure you want to update this transaction?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			cursor1 = connection.ExecQuery("SELECT * FROM inventory_ref_table WHERE inventory_date ='"& LABEL_LOAD_INVDATE.Text & _
			"' and principal_name ='"& CMB_PRINCIPAL.cmbBox.SelectedItem &"' and  area ='"& CMB_AREA.cmbBox.SelectedItem & _
			"' and  warehouse ='"& CMB_WAREHOUSE.cmbBox.SelectedItem &"'")
			If cursor1.RowCount > 0 Then
				For i = 0 To cursor1.RowCount - 1
				Next
				Sleep(0)
				cursor1.Position = i
				Msgbox2Async("The system does not allowed to process the same area , warehouse and principal on the same inventory date. To continue, you can search the existing transaction", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Else
				Dim insert_query As String = "INSERT INTO inventory_ref_table_trail SELECT transaction_id,principal_name,principal_id,inventory_date,warehouse,area,user_info,tab_id,date_registered,time_registered,transaction_status,edit_count,'EDITED' as 'edit_type',? as 'edit_by' from inventory_ref_table WHERE transaction_id = ?"
				connection.ExecNonQuery2(insert_query,Array As String(LOGIN_MODULE.username, transaction_id))
				Sleep(0)
				GET_PRINCIPALID
				Sleep(0)
				Dim query As String = "UPDATE inventory_ref_table SET principal_name = ?, principal_id = ?, inventory_date = ?, warehouse = ?, area = ?, user_info = ?, edit_count = edit_count + 1, date_updated = ?, time_updated = ?, transaction_status = ? WHERE transaction_id = ?"
				connection.ExecNonQuery2(query,Array As String(CMB_PRINCIPAL.cmbBox.SelectedItem, _
				principal_id,LABEL_LOAD_INVDATE.Text,CMB_WAREHOUSE.cmbBox.SelectedItem,CMB_AREA.cmbBox.SelectedItem, _
				LOGIN_MODULE.username, DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),"EDITED",transaction_id))
				Sleep(0)
				ToastMessageShow("Transaction Updated", False)
				LOAD_INVDATE
				Sleep(0)
				PANEL_BG_NEW.SetVisibleAnimated(300,False)
			End If
		End If
	Else
		cursor1 = connection.ExecQuery("SELECT * FROM inventory_ref_table WHERE inventory_date ='"& LABEL_LOAD_INVDATE.Text & _
		"' and principal_name ='"& CMB_PRINCIPAL.cmbBox.SelectedItem &"' and  area ='"& CMB_AREA.cmbBox.SelectedItem & _
		"' and  warehouse ='"& CMB_WAREHOUSE.cmbBox.SelectedItem &"'")
		If cursor1.RowCount > 0 Then
			For i = 0 To cursor1.RowCount - 1
			Next
			Sleep(0)
			cursor1.Position = i
			Msgbox2Async("The system does not allowed to process the same area , warehouse and principal on the same inventory date. To continue, you can search the existing transaction", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Else
			Msgbox2Async("Are you sure you want to create new transaction?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				GET_PRINCIPALID
				Sleep(0)
				GET_TRANSACTIONID
				Sleep(0)
				Dim query As String = "INSERT INTO inventory_ref_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				connection.ExecNonQuery2(query,Array As String(transaction_id,CMB_PRINCIPAL.cmbBox.SelectedItem, _
			principal_id,LABEL_LOAD_INVDATE.Text,CMB_WAREHOUSE.cmbBox.SelectedItem,CMB_AREA.cmbBox.SelectedItem, _
			LOGIN_MODULE.username,LOGIN_MODULE.tab_id, DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),"0","-","-","SAVED"))
				Sleep(0)
				ToastMessageShow("Transaction Added", False)
				LOAD_INVDATE
				Sleep(0)
				PANEL_BG_NEW.SetVisibleAnimated(300,False)
			Else
		
			End If
		End If
	End If
End Sub

Sub SetAnimation(InAnimation As String, OutAnimation As String)
	Dim r As Reflector
	Dim package As String
	Dim in As Int
	Dim out As Int
	package = r.GetStaticField("anywheresoftware.b4a.BA", "packageName")
	in = r.GetStaticField(package & ".R$anim", InAnimation)
	out = r.GetStaticField(package & ".R$anim", OutAnimation)
	r.Target = r.GetActivity
	r.RunMethod4("overridePendingTransition", Array As Object(in, out), Array As String("java.lang.int", "java.lang.int"))
End Sub
Sub BitmapToBitmapDrawable (bitmap As Bitmap) As BitmapDrawable
	Dim bd As BitmapDrawable
	bd.Initialize(bitmap)
	Return bd
End Sub
Sub ACToolBarLight1_NavigationItemClick
	Activity.Finish
	StartActivity(DASHBOARD_MODULE)
	SetAnimation("left_to_center", "center_to_right")
End Sub
Sub UpdateIcon(MenuTitle As String, Icon As Bitmap)
	Dim m As ACMenuItem = GetMenuItem(MenuTitle)
	m.Icon = BitmapToBitmapDrawable(Icon)
End Sub
Sub GetMenuItem(Title As String) As ACMenuItem
	For i = 0 To ACToolBarLight1.Menu.Size - 1
		Dim m As ACMenuItem = ACToolBarLight1.Menu.GetItem(i)
		If m.Title = Title Then
			Return m
		End If
	Next
	Return Null
End Sub
Sub ACToolBarLight1_MenuItemClick (Item As ACMenuItem)
	If Item.Title = "create" Then
		PANEL_BG_NEW.BringToFront
		PANEL_BG_NEW.SetVisibleAnimated(300,True)
		Sleep(0)
		POPULATE_PRINCIPAL
		Sleep(0)
		POPULATE_WAREHOUSE
		Sleep(0)
		POPULATE_AREA
		Sleep(0)
		LABEL_LOAD_INVDATE.Text = DateTime.Date(DateTime.Now)
		Sleep(300)
		OpenSpinner(CMB_PRINCIPAL.cmbBox)
		Sleep(0)
		CMB_PRINCIPAL.SelectedIndex = -1
		LABEL_NEW_HEADER.Text = "Create New Transaction"
		BUTTON_CREATE.Text = " Create"
	Else
		
	End If
End Sub