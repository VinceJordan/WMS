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
	Dim cursor7 As Cursor
	Dim cursor8 As Cursor
	Dim cursor9 As Cursor
	Dim cursor10 As Cursor
	
	Public purchase_order_no As String
	Public transaction_type As String
	Dim principal_acronym As String
	Dim principal_id As String
	Dim principal_name As String
	
	Private plusBitmap As Bitmap
	
	Dim re_po As Int = 0
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Dim CTRL As IME
	
	Private cvs As B4XCanvas
	Private xui As XUI
	Private NameColumn(5) As B4XTableColumn
	
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	
	Private XSelections As B4XTableSelections
	Private TABLE_RECEIVING As B4XTable
	Private CMB_PRINCIPAL As B4XComboBox
	Private PANEL_BG_PO As Panel
	Private EDITTEXT_PO As EditText
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_MSGBOX2 As Label
	Private LABEL_MSGBOX1 As Label
	Private LABEL_HEADER_TEXT As Label
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
	Activity.LoadLayout("receiving")
	
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
	
	LOAD_PURCHASE_HEADER
	Sleep(0)
	POPULATE_PRINCIPAL
	Sleep(0)
	GET_PRINCIPAL_ID
	
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "create", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("create", plusBitmap)
End Sub

Sub Activity_Resume
	Sleep(0)
	LOAD_PURCHASE_ORDER
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
		Msgbox2Async("Please choose a transaction type", "Entry", "Purchase Order", "", "Auto Shipping", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Dim bg As ColorDrawable
			bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
			Sleep(0)		
			EDITTEXT_PO.Background = bg
			PANEL_BG_PO.SetVisibleAnimated(300, True)
			Sleep(0)			
			EDITTEXT_PO.RequestFocus
			Sleep(0)
			CTRL.ShowKeyboard(EDITTEXT_PO)
			transaction_type = "PURCHASE ORDER"
		Else if Result = DialogResponse.NEGATIVE Then
			Msgbox2Async("Are you sure you want to make an AUTO SHIP transaction for this principal?", "Auto Shipping",  "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
			cursor1 = connection.ExecQuery("SELECT * FROM receiving_ref_table WHERE po_doc_no = '"&principal_acronym&"-AUTO-"&DateTime.Date(DateTime.Now).ToUpperCase&"'")
			If cursor1.RowCount > 0 Then
				For row = 0 To cursor1.RowCount - 1
					cursor1.Position = row
				Next
					Msgbox2Async("This principal have existing AUTO SHIP for today, You cannot create more than one AUTO SHIP in a day, Please search it in the table", "Warning", "OK","","",LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Else
					transaction_type = "AUTO SHIP"
					purchase_order_no = principal_acronym&"-AUTO"&DateTime.GetYear(DateTime.Now)&DateTime.GetMonth(DateTime.Now)&DateTime.GetDayOfMonth(DateTime.Now)&LOGIN_MODULE.tab_id
					principal_name = CMB_PRINCIPAL.cmbBox.SelectedItem
					Sleep(0)
					VALIDATE_PO_STATUS
				End If
			End If
		End If
	Else
		
	End If
End Sub

Sub POPULATE_PRINCIPAL
	CMB_PRINCIPAL.cmbBox.Clear
	CMB_PRINCIPAL.cmbBox.DropdownTextColor = Colors.Black
	CMB_PRINCIPAL.cmbBox.TextColor = Colors.White
	cursor1 = connection.ExecQuery("SELECT principal_name FROM principal_table ORDER BY principal_name ASC")
	For i = 0 To cursor1.RowCount - 1
		Sleep(0)
		cursor1.Position = i
		CMB_PRINCIPAL.cmbBox.Add(cursor1.GetString("principal_name"))
	Next
End Sub
Sub GET_PRINCIPAL_ID
	If CMB_PRINCIPAL.SelectedIndex <= -1 Then CMB_PRINCIPAL.SelectedIndex = 0
	cursor2 = connection.ExecQuery("SELECT * FROM principal_table WHERE principal_name = '"&CMB_PRINCIPAL.cmbBox.SelectedItem&"'")
	For i = 0 To cursor2.RowCount - 1
		Sleep(0)
		cursor2.Position = i
		principal_id = cursor2.GetString("principal_id")
		principal_acronym = cursor2.GetString("principal_acronym")
	Next
	Sleep(0)
End Sub

Sub LOAD_PURCHASE_HEADER
	NameColumn(0)=TABLE_RECEIVING.AddColumn("Purchase Order", TABLE_RECEIVING.COLUMN_TYPE_TEXT)
	NameColumn(1)=TABLE_RECEIVING.AddColumn("Receiving Date Time", TABLE_RECEIVING.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_RECEIVING.AddColumn("Received Date Time", TABLE_RECEIVING.COLUMN_TYPE_TEXT)
	NameColumn(3)=TABLE_RECEIVING.AddColumn("Status", TABLE_RECEIVING.COLUMN_TYPE_TEXT)
	NameColumn(4)=TABLE_RECEIVING.AddColumn("Receiving Checker", TABLE_RECEIVING.COLUMN_TYPE_TEXT)
End Sub
Public Sub LOAD_PURCHASE_ORDER
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	cursor10 = connection.ExecQuery("SELECT * FROM receiving_ref_table WHERE principal_id = '"&principal_id&"'")
	If cursor10.RowCount > 0 Then
		For ia = 0 To cursor10.RowCount - 1
			Sleep(10)
			cursor10.Position = ia
				Dim row(5) As Object
				row(0) = cursor10.GetString("po_doc_no")
				row(1) = cursor10.GetString("receiving_date") & " " & cursor10.GetString("receiving_time")
				row(2) = cursor10.GetString("received_date") & " " & cursor10.GetString("received_time")
				row(3) = cursor10.GetString("po_status")
				row(4) = cursor10.GetString("receiving_by")
				Data.Add(row)
		Next
		TABLE_RECEIVING.RowHeight = 50dip
		Sleep(100)
		ProgressDialogHide
	Else
		ProgressDialogHide
		ToastMessageShow("Data is empty", False)
	End If
	TABLE_RECEIVING.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(TABLE_RECEIVING)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub TABLE_RECEIVING_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0),NameColumn(1),NameColumn(2), NameColumn(3),NameColumn(4))

		Dim MaxWidth As Int
'		Dim MaxHeight As Int
		For i = 0 To TABLE_RECEIVING.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 10dip)
'			lbl.SetTextAlignment(Gravity.RIGHT,Gravity.CENTER)
'			MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.Text, lbl.Font).Height + 10dip)
		Next
		
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
		
	Next
	
	TABLE_RECEIVING.NumberOfFrozenColumns = 1
	
'	For Each Column As B4XTableColumn In Array (NameColumn(3))
'
'		Dim MaxWidth As Int
'		Dim MaxHeight As Int
'		For i = 0 To TABLE_RECEIVING.VisibleRowIds.Size
'			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
'			Dim lbl As B4XView = pnl.GetView(0)
'			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 5dip)
''			lbl.SetTextAlignment(Gravity.RIGHT,Gravity.CENTER)
'			MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.Text, lbl.Font).Height + 10dip)
'		Next
'		
'		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
'			Column.Width = MaxWidth + 10dip
'			ShouldRefresh = True
'		End If
'		
'	Next
	
'	For Each Column As B4XTableColumn In Array (NameColumn(0), NameColumn(1),NameColumn(2))
	'
'		For i = 0 To TABLE_RECEIVING.VisibleRowIds.Size
'			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
'			Dim lbl As B4XView = pnl.GetView(0)
'			lbl.Font = xui.CreateDefaultBoldFont(18)
'		Next
'	Next
	
	For i = 0 To TABLE_RECEIVING.VisibleRowIds.Size - 1
		Dim RowId As Long = TABLE_RECEIVING.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(3).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = TABLE_RECEIVING.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(3).Id)
			If OtherColumnValue = "RECEIVING" Then
				clr = xui.Color_Red
			Else If OtherColumnValue = "RECEIVED" Then
				clr = xui.Color_ARGB(255,255,157,0)
			End If
			pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Colors.RGB(215,215,215), 0)
			
		End If
	Next
'	For Each Column As B4XTableColumn In Array (NameColumn(0))
'		Column.InternalSortMode= "ASC"
'	Next
	If ShouldRefresh Then
		TABLE_RECEIVING.Refresh
		XSelections.Clear
	End If
End Sub
Sub TABLE_RECEIVING_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Dim RowData As Map = TABLE_RECEIVING.GetRow(RowId)
	
	Msgbox2Async("Press OK to proceed to this purchase order.", "Purchase Order", "OK", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		StartActivity(RECEIVING2_MODULE)
		purchase_order_no = RowData.Get("Purchase Order")
		principal_name = CMB_PRINCIPAL.cmbBox.SelectedItem
	End If
End Sub
Sub TABLE_RECEIVING_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = TABLE_RECEIVING.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)
End Sub

#Region SQL
Sub CreateRequest As DBRequestManager
	Dim req As DBRequestManager
	req.Initialize(Me, Main.rdclink)
	Return req
End Sub
Sub CreateCommand(Name As String, Parameters() As Object) As DBCommand
	Dim cmd As DBCommand
	cmd.Initialize
	cmd.Name = Name
	If Parameters <> Null Then cmd.Parameters = Parameters
	Return cmd
End Sub
Sub VALIDATE_PO_STATUS
	PANEL_BG_PO.SetVisibleAnimated(300,False)
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
	LABEL_HEADER_TEXT.Text = "Downloading Purchase Order"
	LABEL_MSGBOX2.Text = "Fetching data..."
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_receiving_ref", Array(EDITTEXT_PO.Text))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				If row(res.Columns.Get("po_status")) = "PROCESSED" Then
				
				Else if row(res.Columns.Get("po_status")) = "RECEIVING" Then
					If row(res.Columns.Get("tab_id")) = LOGIN_MODULE.tab_id And row(res.Columns.Get("receiving_by")) = LOGIN_MODULE.username Then
						re_po = 1
						LOAD_PURCHASE_ORDER_NO
					Else
					Msgbox2Async("The purchase order you inputed :" & CRLF & _
						" Purchase Order No : " & row(res.Columns.Get("po_doc_no")) & CRLF & _
						" Principal : " & row(res.Columns.Get("principal_name")) & CRLF & _
						" is NOW RECEIVING by :" & CRLF & _
						" Receiving Checker : " & row(res.Columns.Get("receiving_by")) & CRLF & _
						" Receiving Date : " & row(res.Columns.Get("receiving_date")) & CRLF & _
						" Receiving Time : " & row(res.Columns.Get("receiving_time")) & CRLF & _
						" Tablet : TABLET " & row(res.Columns.Get("tab_id")), _
			   			"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
					End If
				Else if row(res.Columns.Get("po_status")) = "RECEIVED" Then
					Msgbox2Async("The purchase order you inputed :" & CRLF & _
						" Purchase Order No : " & row(res.Columns.Get("po_doc_no")) & CRLF & _
						" Principal : " & row(res.Columns.Get("principal_name")) & CRLF & _
						" is ALREADY RECEIVED by :" & CRLF & _
						" Receiving Checker : " & row(res.Columns.Get("receiving_by")) & CRLF & _
						" Receiving Date : " & row(res.Columns.Get("received_date")) & CRLF & _
						" Receiving Time : " & row(res.Columns.Get("received_time")) & CRLF & _
						" Tablet : TABLET " & row(res.Columns.Get("tab_id")), _
			   			"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
'					End If
				End If
			Next
		Else
			re_po = 0
			If transaction_type = "PURCHASE ORDER" Then
				LOAD_PURCHASE_ORDER_NO
			Else if transaction_type = "AUTO SHIP" Then
				INSERT_PURCHASE_ORDER_REF
			End If
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub LOAD_PURCHASE_ORDER_NO
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_po_no", Array As String(purchase_order_no))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		LABEL_MSGBOX2.Text = "Getting Order..."
		Sleep(0)
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
'		'work with result
		Log(2)
		Dim get1 As String = 0
		Dim prin_id As String
		Dim count As Int = 0
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows	
				If principal_id = row(res.Columns.Get("principal_id")) Then
					If count = 0 Then
						connection.ExecNonQuery("DELETE FROM purchase_order_ref_table WHERE po_doc_no = '" & purchase_order_no & "'")
						count = count + 1
					End If
					connection.ExecNonQuery("INSERT INTO purchase_order_ref_table VALUES ('"&row(res.Columns.Get("po_doc_no"))&"','"&row(res.Columns.Get("principal_id"))&"','"&row(res.Columns.Get("product_variant"))& _
					"','"&row(res.Columns.Get("product_description"))&"','"&row(res.Columns.Get("quantity"))&"','"&row(res.Columns.Get("unit"))& _
					"','"&row(res.Columns.Get("unit_price"))&"','"&row(res.Columns.Get("po_amount"))&"','"&row(res.Columns.Get("acqui_amount"))&"','"&row(res.Columns.Get("total_pieces"))& _
					"','"&row(res.Columns.Get("date_time_registered"))&"','"&row(res.Columns.Get("date_time_modified"))&"','"&row(res.Columns.Get("modified_flag"))&"','"&row(res.Columns.Get("edit_number"))&"','"&row(res.Columns.Get("user_info"))&"')")
					Sleep(0)
					LABEL_MSGBOX2.Text = "Getting purchase order : " & row(res.Columns.Get("product_description"))
				Else
					get1 = 1
					prin_id = row(res.Columns.Get("principal_id"))
				End If
			Next
			If get1 = 0 Then
				If re_po = 1 Then
					Sleep(0)
					PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
					Sleep(0)
					LOAD_PURCHASE_ORDER
				Else
					INSERT_PURCHASE_ORDER_REF
				End If
			Else
				cursor3 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id = '"& prin_id &"'")
				For i = 0 To cursor3.RowCount - 1
					Sleep(0)
					cursor3.Position = i
					Msgbox2Async("This purchase order is belongs to principal : " & cursor3.GetString("principal_name") ,"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				Next
			End If
		Else
			Msgbox2Async("The purchase order you input is NOT EXISTING IN THE SYSTEM","Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)

			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		End If
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub INSERT_PURCHASE_ORDER_REF
	Dim cmd As DBCommand = CreateCommand("insert_receiving_ref", Array As String(purchase_order_no,principal_id,CMB_PRINCIPAL.cmbBox.SelectedItem,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now), _
	LOGIN_MODULE.username,"-","-","RECEIVING",LOGIN_MODULE.tab_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Dim query As String = "DELETE FROM receiving_ref_table WHERE po_doc_no = ?"
		connection.ExecNonQuery2(query,Array As String(purchase_order_no))
		Sleep(0)
		Dim query1 As String = "INSERT INTO receiving_ref_table VALUES (?,?,?,?,?,?,?,?,?,?)"
		connection.ExecNonQuery2(query1,Array As String(purchase_order_no,principal_id,CMB_PRINCIPAL.cmbBox.SelectedItem,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now), _
		LOGIN_MODULE.username,"-","-","RECEIVING",LOGIN_MODULE.tab_id))
		Sleep(0)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Sleep(0)
		LOAD_PURCHASE_ORDER
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Sleep(1000)
	End If
	js.Release
End Sub
#End Region

Sub BUTTON_CANCEL_Click
	EDITTEXT_PO.Text = ""
	PANEL_BG_PO.SetVisibleAnimated(300, False)
End Sub
Sub BUTTON_LOAD_Click
	purchase_order_no = EDITTEXT_PO.Text.ToUpperCase
	principal_name = CMB_PRINCIPAL.cmbBox.SelectedItem
	CTRL.HideKeyboard
	Sleep(0)
	VALIDATE_PO_STATUS
End Sub

Sub CMB_PRINCIPAL_SelectedIndexChanged (Index As Int)
	GET_PRINCIPAL_ID
	Sleep(0)
	LOAD_PURCHASE_ORDER
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean
	If KeyCode = KeyCodes.KEYCODE_BACK Then
		Return True
	End If
End Sub