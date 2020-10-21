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
	
	Dim phone As Phone
	
	Public inventory_date As String
	Public status As String
	Public date As String
	Public time As String
'	
'	calcu
	Dim ProgPath = File.DirRootExternal & "/TapeCalc" As String
	Dim ScaleAuto As Double
	Dim Texts(8) As String
	Dim LanguageID As String
'	
	Dim sVal = "" As String
	Dim Val = 0 As Double
	Dim Op0 = "" As String
	Dim Result1 = 0 As Double
	Dim Txt = "" As String
	Dim New1 = 0 As Int
	
	'bluetooth
	Dim serial1 As Serial
	Dim AStream As AsyncStreams
	Dim Ts As Timer
	
	'Product
	Dim principal_id As String
	Dim product_id As String
	Dim reason As String
	Dim input_type As String
	Dim caseper As String
	Dim pcsper As String
	Dim dozper As String
	Dim boxper As String
	Dim bagper As String
	Dim packper As String
	Dim total_pieces As String
	Dim scan_code As String
	Dim status_trigger As String
	Dim error_trigger As String
	
	Dim item_number As String
	
	Dim page As String
	
	Private cartBitmap As Bitmap
	Private controlBitmap As Bitmap
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim CTRL As IME
	
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	
'	calcu
	Dim btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9 As Button
	Dim btnBack, btnClr, btnExit As Button
	Dim lblPaperRoll As Label
	Dim scvPaperRoll As ScrollView
	Dim pnlKeyboard As Panel
	Dim stu As StringUtils
	
	'bluetooth
	Dim ScannerMacAddress As String
	Dim ScannerOnceConnected As Boolean

	Private XSelections As B4XTableSelections
	Private NameColumn(7) As B4XTableColumn

	Private Dialog As B4XDialog
	Private Base As B4XView
	Private SearchTemplate As B4XSearchTemplate
	Private SearchTemplate2 As B4XSearchTemplate
	
	Private cvs As B4XCanvas
	Private xui As XUI
	
	Private LABEL_LOAD_STATUS As Label
	Private LABEL_LOAD_DATE As Label
	Private LABEL_LOAD_INV_DATE As Label
	Private TABLE_DAILY_INVENTORY As B4XTable
	Private LABEL_LOAD_PRINCIPAL As Label
	Private LABEL_LOAD_VARIANT As Label
	Private LABEL_LOAD_DESCRIPTION As Label
	Private CMB_REASON As B4XComboBox
	Private CMB_UNIT As B4XComboBox
	Private EDITTEXT_QUANTITY As EditText
	Private PANEL_BG_INPUT As Panel
	Private LVL_LIST As ListView
	Private LABEL_LOAD_ACTUAL As Label
	Private LABEL_LOAD_ADD As Label
	Private LABEL_LOAD_DEDUCT As Label
	Private BUTTON_SAVE As Button
	Private BUTTON_CANCEL As Button
	Private LABEL_LOAD_SYSDATE As Label
	Private LABEL_LOAD_UPLDATE As Label
	Private LABEL_BLUETOOTH_STATUS As Label
	Private LABEL_PAGE As Label
	Private BUTTON_NEXT As Button
	Private BUTTON_PREV As Button
	Private LABEL_HEADER_TEXT As Label
	Private LABEL_MSGBOX1 As Label
	Private LABEL_MSGBOX2 As Label
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_LOAD_ANSWER As Label
	Private PANEL_BG_CALCU As Panel
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
	Activity.LoadLayout("daily_count")
	
	controlBitmap = LoadBitmap(File.DirAssets, "check.png")
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	
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
	
	'blueetooth
	serial1.Initialize("Serial")
	Ts.Initialize("Timer", 2000)
	
	phone.SetScreenOrientation(0)

	Base = Activity
	Dialog.Initialize(Base)
	Dialog.BorderColor = Colors.Transparent
	Dialog.BorderCornersRadius = 5
	Dialog.TitleBarColor = Colors.RGB(82,169,255)
	Dialog.TitleBarTextColor = Colors.White
	Dialog.BackgroundColor = Colors.White
	Dialog.ButtonsColor = Colors.White
	Dialog.ButtonsTextColor = Colors.Black
	Dialog.PutAtTop = True
	
	SearchTemplate.Initialize
	SearchTemplate.CustomListView1.DefaultTextBackgroundColor = Colors.White
	SearchTemplate.CustomListView1.DefaultTextColor = Colors.Black
	SearchTemplate.SearchField.TextField.TextColor = Colors.Black
	SearchTemplate.ItemHightlightColor = Colors.White
	SearchTemplate.TextHighlightColor = Colors.RGB(82,169,255)
	
	
	SearchTemplate2.Initialize
	SearchTemplate2.CustomListView1.DefaultTextBackgroundColor = Colors.White
	SearchTemplate2.CustomListView1.DefaultTextColor = Colors.Black
	SearchTemplate2.SearchField.TextField.TextColor = Colors.Black
	SearchTemplate2.ItemHightlightColor = Colors.White
	SearchTemplate2.TextHighlightColor = Colors.RGB(82,169,255)
	
	DateTime.DateFormat = "yyyy-MM-dd"
	DateTime.TimeFormat = "hh:mm a"
	Sleep(0)
	LOAD_TABLE_HEADER
	
	GET_DAILY
	
	page = 1
	
	LABEL_PAGE.Text = "PAGE : " & page
	
	Sleep(0)
	Dim Ref As Reflector
	Ref.Target = EDITTEXT_QUANTITY ' The text field being referenced
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Log("start")
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "cart", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
	Sleep(0)
	Dim item2 As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 0, "check", Null)
	item2.ShowAsAction = item2.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("check", controlBitmap)
	Sleep(100)
	GET_STATUS
End Sub

Sub Activity_Resume
	Log("Resuming...")
	ShowPairedDevices
	If ScannerOnceConnected=True Then
		Ts.Enabled=True
	End If
End Sub
Sub Activity_Pause (UserClosed As Boolean)
	Log ("Activity paused. Disconnecting...")
	AStream.Close
	serial1.Disconnect
	Ts.Enabled=False
End Sub

Sub SetAnimation(InAnimation As String, OutAnimation As String)
	Dim r As Reflector
	Dim package As String
	Dim IN As Int
	Dim out As Int
	package = r.GetStaticField("anywheresoftware.b4a.BA", "packageName")
	IN = r.GetStaticField(package & ".R$anim", InAnimation)
	out = r.GetStaticField(package & ".R$anim", OutAnimation)
	r.Target = r.GetActivity
	r.RunMethod4("overridePendingTransition", Array As Object(IN, out), Array As String("java.lang.int", "java.lang.int"))
End Sub
Sub BitmapToBitmapDrawable (bitmap As Bitmap) As BitmapDrawable
	Dim bd As BitmapDrawable
	bd.Initialize(bitmap)
	Return bd
End Sub
Sub ACToolBarLight1_NavigationItemClick
	Activity.Finish
	StartActivity(DAILY_INVENTORY_MODULE)
'	SetAnimation("left_to_center", "center_to_right")
	DAILY_INVENTORY_MODULE.clear_trigger = 0
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
	If Item.Title = "cart" Then
		Log("Resuming...")
		LABEL_BLUETOOTH_STATUS.Text = " Connecting..."
		LABEL_BLUETOOTH_STATUS.Color = Colors.Blue
		cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
		UpdateIcon("cart", cartBitmap)
		ShowPairedDevices
		If ScannerOnceConnected=True Then
			Ts.Enabled=True
			LABEL_BLUETOOTH_STATUS.Text = " Connected"
			LABEL_BLUETOOTH_STATUS.Color = Colors.Green
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connected.png")
			UpdateIcon("cart", cartBitmap)
		Else
			LABEL_BLUETOOTH_STATUS.Text = " Disconnected"
			LABEL_BLUETOOTH_STATUS.Color = Colors.Red
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
			UpdateIcon("cart", cartBitmap)
		End If
	Else if Item.Title = "check" Then
		If status_trigger = 0 Then
			Msgbox2Async("Are you sure you want to finalize this actual counting?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				UPDATE_PRODUCTOFFTAKE
			End If
		Else 
			Msgbox2Async("Are you sure you want to upload this inventory date? Once you upload, you cannot configure this data.", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				DELETE_DAILY_DISC
			End If
		End If
		End If
End Sub

Sub OpenSpinner(se As Spinner)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
End Sub

Sub LOAD_TABLE_HEADER
	NameColumn(0)=TABLE_DAILY_INVENTORY.AddColumn("Product Variant", TABLE_DAILY_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(1)=TABLE_DAILY_INVENTORY.AddColumn("Product Description", TABLE_DAILY_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_DAILY_INVENTORY.AddColumn("System Count", TABLE_DAILY_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(3)=TABLE_DAILY_INVENTORY.AddColumn("Add Count", TABLE_DAILY_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(4)=TABLE_DAILY_INVENTORY.AddColumn("Deduct Count", TABLE_DAILY_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(5)=TABLE_DAILY_INVENTORY.AddColumn("Actual Count", TABLE_DAILY_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(6)=TABLE_DAILY_INVENTORY.AddColumn("Variance", TABLE_DAILY_INVENTORY.COLUMN_TYPE_TEXT)
End Sub
Sub GET_DAILY
	Sleep(0)
	Dim Data As List
	Data.Initialize
	cursor7 = connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' ORDER by product_variant ASC")
	If cursor7.RowCount > 0 Then
		For i = 0 To cursor7.RowCount - 1
			cursor7.Position = i
			cursor8 = connection.ExecQuery("SELECT (Select sum(total_pieces) FROM daily_inventory_disc_table WHERE (input_reason = 'Actual Count' or input_reason = 'Wrong Count') AND group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' and inventory_date = '"&inventory_date&"' and product_id = '"&cursor7.GetString("product_id")&"') as 'actual_count'," & _
			"(Select sum(total_pieces) FROM daily_inventory_disc_table WHERE input_reason LIKE '%Add' AND group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' and inventory_date = '"&inventory_date&"' and product_id = '"&cursor7.GetString("product_id")&"') as 'add_count'," & _
			"(Select sum(total_pieces) FROM daily_inventory_disc_table WHERE input_reason LIKE '%Deduct' AND group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' and inventory_date = '"&inventory_date&"' and product_id = '"&cursor7.GetString("product_id")&"') as 'deduct_count'," & _
			"system_count FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' and inventory_date = '"&inventory_date&"' and product_id = '"&cursor7.GetString("product_id")&"' GROUP BY product_id")
			If cursor8.RowCount > 0 Then
				Dim actual_count As String
				Dim add_count As String
				Dim deduct_count As String
				Dim system_count As String
				For ia = 0 To cursor8.RowCount - 1
					Sleep(0)
					cursor8.Position = ia
					If cursor8.GetString("actual_count") = Null Or cursor8.GetString("actual_count") = "" Then
						actual_count = "0"
					Else
						actual_count = cursor8.GetString("actual_count")
					End If
					Sleep(0)
					If cursor8.GetString("add_count") = Null Or cursor8.GetString("add_count") = "" Then
						add_count = "0"
					Else
						add_count = cursor8.GetString("add_count")
					End If
					Sleep(0)
					If cursor8.GetString("deduct_count") = Null Or cursor8.GetString("deduct_count") = "" Then
						deduct_count = "0"
					Else
						deduct_count = cursor8.GetString("deduct_count")
					End If
					Sleep(0)
					If cursor8.GetString("system_count") = Null Or cursor8.GetString("system_count") = "" Then
						system_count = "0"
					Else
						system_count = cursor8.GetString("system_count")
					End If
				Next
				Dim row(7) As Object
				row(0) = cursor7.GetString("product_variant")
				row(1) = cursor7.GetString("product_description")
				row(2) = Number.Format3((system_count),0,0,0,".",",","",0,15)
				row(3) = Number.Format3((add_count),0,0,0,".",",","",0,15)
				row(4) = Number.Format3((deduct_count),0,0,0,".",",","",0,15)
				row(5) = Number.Format3((actual_count),0,0,0,".",",","",0,15)
				row(6) = Number.Format3((actual_count - ((system_count + add_count) - deduct_count)),0,0,0,".",",","",0,15)
				Data.Add(row)
			Else
				Dim row(7) As Object
				row(0) = cursor7.GetString("product_variant")
				row(1) = cursor7.GetString("product_description")
				row(2) = "0"
				row(3) = "0"
				row(4) = "0"
				row(5) = "0"
				row(6) = "0"
				Data.Add(row)
			End If
		Next
	End If
	TABLE_DAILY_INVENTORY.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(TABLE_DAILY_INVENTORY)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
	TABLE_DAILY_INVENTORY.pnlHeader.Visible = False
End Sub
Sub TABLE_DAILY_INVENTORY_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0), NameColumn(1), NameColumn(2),NameColumn(3), NameColumn(4), NameColumn(5), NameColumn(6))
		Dim MaxWidth As Int
		For i = 0 To TABLE_DAILY_INVENTORY.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 10dip)
		Next
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
	Next
	For i = 0 To TABLE_DAILY_INVENTORY.VisibleRowIds.Size - 1
		Dim RowId As Long = TABLE_DAILY_INVENTORY.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(6).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = TABLE_DAILY_INVENTORY.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(6).Id)
			Log(row.Get(NameColumn(6).Id))
			If OtherColumnValue.SubString2(0,1) = "-" Then
				clr = xui.Color_Red
			Else If OtherColumnValue.SubString2(0,1) = 0 Then
				clr = xui.Color_Green
			Else
				clr = xui.Color_Blue
			End If
			pnl1.GetView(0).Color = clr
			
		End If
	Next
	If ShouldRefresh Then
		TABLE_DAILY_INVENTORY.Refresh
		XSelections.Clear
		XSelections.Refresh
	End If
	Sleep(0)
	BUTTON_NEXT.Enabled = TABLE_DAILY_INVENTORY.lblNext.Tag
	BUTTON_PREV.Enabled = TABLE_DAILY_INVENTORY.lblBack.Tag
End Sub
Sub TABLE_DAILY_INVENTORY_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Log(ColumnId & RowId)
End Sub
Sub TABLE_DAILY_INVENTORY_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Log(ColumnId & RowId)
	
	Dim RowData As Map = TABLE_DAILY_INVENTORY.GetRow(RowId)
	Dim ls As List
	ls.Initialize

	If LABEL_LOAD_STATUS.Text = "SAVED" Then
		ls.Add("BARCODE NOT REGISTERED")
		ls.Add("NO ACTUAL BARCODE")
		ls.Add("NO SCANNER")
		ls.Add("DAMAGE BARCODE")
		ls.Add("SCANNER CAN'T READ BARCODE")
	Else If LABEL_LOAD_STATUS.Text = "FINAL" Then
		ls.Add("RECONCIALATION")
	Else
		ls.Add("VIEWING")
	End If
	InputListAsync(ls, "Choose reason", -1, True) 'show list with paired devices
	Wait For InputList_Result (Result2 As Int)
	If Result2 <> DialogResponse.CANCEL Then
#Region Find Product
		cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&RowData.Get("Product Description")&"'")
		If cursor3.RowCount > 0 Then
			For qrow = 0 To cursor3.RowCount - 1
				cursor3.Position = qrow
				LABEL_LOAD_VARIANT.Text = cursor3.GetString("product_variant")
				LABEL_LOAD_DESCRIPTION.Text = cursor3.GetString("product_desc")
				principal_id = cursor3.GetString("principal_id")
				product_id = cursor3.GetString("product_id")
			
				CMB_UNIT.cmbBox.Clear
				If cursor3.GetString("CASE_UNIT_PER_PCS") > 0 Then
					CMB_UNIT.cmbBox.Add("CASE")
				End If
				If cursor3.GetString("PCS_UNIT_PER_PCS") > 0 Then
					CMB_UNIT.cmbBox.Add("PCS")
				End If
				If cursor3.GetString("DOZ_UNIT_PER_PCS") > 0 Then
					CMB_UNIT.cmbBox.Add("DOZ")
				End If
				If cursor3.GetString("BOX_UNIT_PER_PCS") > 0 Then
					CMB_UNIT.cmbBox.Add("BOX")
				End If
				If cursor3.GetString("BAG_UNIT_PER_PCS") > 0 Then
					CMB_UNIT.cmbBox.Add("BAG")
				End If
				If cursor3.GetString("PACK_UNIT_PER_PCS") > 0 Then
					CMB_UNIT.cmbBox.Add("PACK")
				End If
			
				caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
				pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
				dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
				boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
				bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
				packper = cursor3.GetString("PACK_UNIT_PER_PCS")
		
			Next
		End If
		cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
		If cursor6.RowCount > 0 Then
			For row = 0 To cursor6.RowCount - 1
				cursor6.Position = row
				LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring("principal_name")
			Next
		End If
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Black)
		EDITTEXT_QUANTITY.Background = bg
		reason = ls.Get(Result2)
		input_type = "MANUAL"
		scan_code = "N/A"
		PANEL_BG_INPUT.SetVisibleAnimated(300,True)
		Sleep(0)
		If LABEL_LOAD_STATUS.Text = "SAVED" Then
			LOAD_REASON
			Sleep(10)
			OpenSpinner(CMB_UNIT.cmbBox)
			Sleep(10)
			CMB_UNIT.SelectedIndex = -1
			BUTTON_SAVE.Visible = True
			CMB_REASON.cmbBox.Enabled = True
			CMB_UNIT.cmbBox.Enabled = True
			EDITTEXT_QUANTITY.Enabled = True
			CMB_UNIT.SelectedIndex = 0
			CMB_REASON.SelectedIndex = 0
		Else If LABEL_LOAD_STATUS.Text = "FINAL" Then
			LOAD_REASON
			OpenSpinner(CMB_REASON.cmbBox)
			Sleep(0)
			CMB_REASON.SelectedIndex = -1
			Sleep(0)
			BUTTON_SAVE.Visible = True
			CMB_REASON.cmbBox.Enabled = True
			CMB_UNIT.cmbBox.Enabled = True
			EDITTEXT_QUANTITY.Enabled = True
			CMB_UNIT.SelectedIndex = 0
		Else
			BUTTON_SAVE.Visible = False
			CMB_REASON.cmbBox.Enabled = False
			CMB_UNIT.cmbBox.Enabled = False
			EDITTEXT_QUANTITY.Enabled = False
		End If
		LOAD_LIST
		CLEAR
#End Region		
	End If
End Sub

Sub GET_STATUS
	cursor1 = connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' and inventory_date = '"&inventory_date&"' GROUP BY group_id ORDER BY date_registered, time_registered ASC LIMIT 1")
	If cursor1.RowCount > 0 Then
		For i = 0 To cursor1.RowCount - 1
			Sleep(0)
			cursor1.Position = i
			LABEL_LOAD_INV_DATE.Text = cursor1.GetString("inventory_date")
			LABEL_LOAD_STATUS.Text = cursor1.GetString("status")
			Sleep(10)
			If LABEL_LOAD_STATUS.Text = "FINAL" Then
				LABEL_LOAD_DATE.Text = cursor1.GetString("date_registered") & " " & cursor1.GetString("time_registered")
				LABEL_LOAD_SYSDATE.Text = cursor1.GetString("date_final") & " " & cursor1.GetString("time_final")
				LABEL_LOAD_UPLDATE.Text = "----"
				Dim bg As ColorDrawable
				bg.Initialize2(Colors.Green,5,0,Colors.Black)
				LABEL_LOAD_STATUS.Background = bg
				Dim bg2 As ColorDrawable
				bg2.Initialize2(Colors.ARGB(123,82,169,255),360,0,Colors.Black)
				status_trigger = 1
				controlBitmap = LoadBitmap(File.DirAssets, "upload.png")
				UpdateIcon("check", controlBitmap)
			Else If LABEL_LOAD_STATUS.Text = "UPLOADED" Then
				LABEL_LOAD_DATE.Text = cursor1.GetString("date_registered") & " " & cursor1.GetString("time_registered")
				LABEL_LOAD_SYSDATE.Text = cursor1.GetString("date_final") & " " & cursor1.GetString("time_final")
				LABEL_LOAD_UPLDATE.Text = cursor1.GetString("date_upload") & " " & cursor1.GetString("time_upload")
				Dim bg As ColorDrawable
				bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Black)
				LABEL_LOAD_STATUS.Background = bg
				controlBitmap = LoadBitmap(File.DirAssets, "upload.png")
				UpdateIcon("check", controlBitmap)
				status_trigger = 2
			Else
				status_trigger = 0
				LABEL_LOAD_STATUS.Text = "SAVED"
				LABEL_LOAD_DATE.Text = cursor1.GetString("date_registered") & " " & cursor1.GetString("time_registered")
				LABEL_LOAD_SYSDATE.Text = "----"
				LABEL_LOAD_UPLDATE.Text = "----"
				Dim bg As ColorDrawable
				bg.Initialize2(Colors.Red,5,0,Colors.Black)
				LABEL_LOAD_STATUS.Background = bg
				Dim bg2 As ColorDrawable
				bg2.Initialize2(Colors.ARGB(123,0,255,0),360,0,Colors.Black)
				controlBitmap = LoadBitmap(File.DirAssets, "check.png")
				UpdateIcon("check", controlBitmap)
			End If
		Next
	Else
		status_trigger = 0
		LABEL_LOAD_INV_DATE.Text = inventory_date
		LABEL_LOAD_STATUS.Text = "SAVED"
		LABEL_LOAD_DATE.Text = "----"
		LABEL_LOAD_SYSDATE.Text = "----"
		LABEL_LOAD_UPLDATE.Text = "----"
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.Red,5,0,Colors.Black)
		LABEL_LOAD_STATUS.Background = bg
		Dim bg2 As ColorDrawable
		bg2.Initialize2(Colors.ARGB(123,0,255,0),360,0,Colors.Black)
		controlBitmap = LoadBitmap(File.DirAssets, "check.png")
		UpdateIcon("check", controlBitmap)
	End If
End Sub

#Region BLUETOOTH
Sub ShowPairedDevices
	Dim mac As String
	Dim PairedDevices As Map
	PairedDevices = serial1.GetPairedDevices
	Dim ls As List
	ls.Initialize
	For Iq = 0 To PairedDevices.Size - 1
		mac = PairedDevices.Get(PairedDevices.GetKeyAt(Iq))
		ls.add("Scanner " & mac.SubString2 (mac.Length - 4, mac.Length ))
	Next
	If ls.Size=0 Then
		ls.Add("No device(s) found...")
	End If
	InputListAsync(ls, "Choose scanner", -1, True) 'show list with paired devices
	Wait For InputList_Result (Result As Int)
	If Result <> DialogResponse.CANCEL Then
		If ls.Get(Result)="No device(s) found..." Then
			Return
		Else
			ScannerMacAddress=PairedDevices.Get(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get(Result))))
			'convert the name to mac address and connect
			Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (Result))))
			serial1.Connect(ScannerMacAddress)
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
			UpdateIcon("cart", cartBitmap)
		End If
	End If
End Sub
Sub Serial_Connected (success As Boolean)
	If success = True Then
		Log("Scanner is now connected. Waiting for data...")
		LABEL_BLUETOOTH_STATUS.Text = " Connected"
		LABEL_BLUETOOTH_STATUS.Color = Colors.Green
		cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connected.png")
		UpdateIcon("cart", cartBitmap)
		ToastMessageShow("Scanner Connected", True)
		AStream.Initialize(serial1.InputStream, serial1.OutputStream, "AStream")
		ScannerOnceConnected=True
		Ts.Enabled=False
	Else
		If ScannerOnceConnected=False Then
			LABEL_BLUETOOTH_STATUS.Text = " Disconnected"
			LABEL_BLUETOOTH_STATUS.Color = Colors.Red
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
			UpdateIcon("cart", cartBitmap)
			ToastMessageShow("Scanner is off, please turn on", False)
			ShowPairedDevices
		Else
			Log("Still waiting for the scanner to reconnect: " & ScannerMacAddress)
			Ts.Enabled=True
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
			UpdateIcon("cart", cartBitmap)
			LABEL_BLUETOOTH_STATUS.Text = " Connecting..."
			LABEL_BLUETOOTH_STATUS.Color = Colors.Blue
		End If
	End If
End Sub
Sub AStream_NewData (Buffer() As Byte)
	Log("Received: " & BytesToString(Buffer, 0, Buffer.Length, "UTF8"))
'	LABEL_LOAD_BARCODE.Text = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
#Region Scan Barcode
	Dim trigger As Int = 0
	cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") &"' as INTEGER) or case_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or box_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or bag_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or pack_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER)) and prod_status = '0' ORDER BY product_id")
	If cursor2.RowCount >= 2 Then
		Dim ls As List
		ls.Initialize
		For row = 0 To cursor2.RowCount - 1
			cursor2.Position = row
			ls.Add(cursor2.GetString("product_desc"))
			DateTime.DateFormat = "yyyy-MM-dd"
		Next
		InputListAsync(ls, "Choose scanner", -1, True) 'show list with paired devices
		Wait For InputList_Result (Result As Int)
		If Result <> DialogResponse.CANCEL Then
			LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)
			trigger = 0
		Else
			trigger = 1
		End If
					
		'SINGLE SKU
	Else if cursor2.RowCount = 1 Then
		For row = 0 To cursor2.RowCount - 1
			cursor2.Position = row
			Log(1)
			LABEL_LOAD_DESCRIPTION.Text = cursor2.GetString("product_desc")
			trigger = 0
		Next
	Else
		PANEL_BG_INPUT.SetVisibleAnimated(300,False)
		Msgbox2Async("The barcode you scanned is not REGISTERED IN THE SYSTEM.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		trigger = 1
'		CLEAR_INPUT
	End If
	Sleep(0)
	cursor4 = connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE product_description ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
	If cursor4.RowCount > 0 Then
		For row = 0 To cursor4.RowCount - 1
			cursor4.Position = row
		Next
	Else
		PANEL_BG_INPUT.SetVisibleAnimated(300,False)
		Msgbox2Async("The product you scanned :"& CRLF &""&LABEL_LOAD_DESCRIPTION.Text&" "& CRLF &"is not added to this template.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		trigger = 1
	End If
	Sleep(0)
	If trigger = 0 Then
		cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
		For qrow = 0 To cursor3.RowCount - 1
			cursor3.Position = qrow
			LABEL_LOAD_VARIANT.Text = cursor3.GetString("product_variant")
			principal_id = cursor3.GetString("principal_id")
			product_id = cursor3.GetString("product_id")
			
			CMB_UNIT.cmbBox.Clear
			If cursor3.GetString("CASE_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("CASE")
			End If
			If cursor3.GetString("PCS_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("PCS")
			End If
			If cursor3.GetString("DOZ_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("DOZ")
			End If
			If cursor3.GetString("BOX_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("BOX")
			End If
			If cursor3.GetString("BAG_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("BAG")
			End If
			If cursor3.GetString("PACK_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("PACK")
			End If
			
			caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
			pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
			dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
			boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
			bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
			packper = cursor3.GetString("PACK_UNIT_PER_PCS")
		
		Next
		cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
		If cursor6.RowCount > 0 Then
			For row = 0 To cursor6.RowCount - 1
				cursor6.Position = row
				LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring("principal_name")
			Next
		End If
			Sleep(0)
			Dim bg As ColorDrawable
			bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Black)
			EDITTEXT_QUANTITY.Background = bg
			reason = "N/A"
			input_type = "BARCODE"
			scan_code = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
		PANEL_BG_INPUT.SetVisibleAnimated(300,True)
		Sleep(0)
		If LABEL_LOAD_STATUS.Text = "SAVED" Then
			LOAD_REASON
			Sleep(10)
			OpenSpinner(CMB_UNIT.cmbBox)
			Sleep(10)
			CMB_UNIT.SelectedIndex = -1
			BUTTON_SAVE.Visible = True
			CMB_REASON.cmbBox.Enabled = True
			CMB_UNIT.cmbBox.Enabled = True
			EDITTEXT_QUANTITY.Enabled = True
			CMB_UNIT.SelectedIndex = 0
			CMB_REASON.SelectedIndex = 0
		Else If LABEL_LOAD_STATUS.Text = "FINAL" Then
			LOAD_REASON
			OpenSpinner(CMB_REASON.cmbBox)
			Sleep(0)
			CMB_REASON.SelectedIndex = -1
			BUTTON_SAVE.Visible = True
			CMB_REASON.cmbBox.Enabled = True
			CMB_UNIT.cmbBox.Enabled = True
			EDITTEXT_QUANTITY.Enabled = True
			CMB_UNIT.SelectedIndex = 0
		Else
			BUTTON_SAVE.Visible = False
			CMB_REASON.cmbBox.Enabled = False
			CMB_UNIT.cmbBox.Enabled = False
			EDITTEXT_QUANTITY.Enabled = False
		End If
			LOAD_LIST
			CLEAR
	End If
#end Region
End Sub
Sub AStream_Error
	Log("Connection broken...")
	AStream.Close
	serial1.Disconnect
	LABEL_BLUETOOTH_STATUS.Text = " Disconnected"
	LABEL_BLUETOOTH_STATUS.Color = Colors.Red
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	UpdateIcon("cart", cartBitmap)
	If ScannerOnceConnected=True Then
		Ts.Enabled=True
		LABEL_BLUETOOTH_STATUS.Text = " Connected"
		LABEL_BLUETOOTH_STATUS.Color = Colors.Green
		cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connected.png")
		UpdateIcon("cart", cartBitmap)
	Else
		ShowPairedDevices
	End If
End Sub
Sub AStream_Terminated
	Log("Connection terminated...")
	AStream_Error
End Sub
Sub Timer_Tick
	Ts.Enabled=False
	serial1.Connect(ScannerMacAddress)
	Log ("Trying to connect...")
	LABEL_BLUETOOTH_STATUS.Text = " Connecting..."
	LABEL_BLUETOOTH_STATUS.Color = Colors.Blue
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
	UpdateIcon("cart", cartBitmap)
End Sub
#End Region

#Region INPUT
Sub BUTTON_EXIT_Click
	PANEL_BG_INPUT.SetVisibleAnimated(300,False)
End Sub
Sub LOAD_REASON
	If LABEL_LOAD_STATUS.Text = "SAVED" Then
		CMB_REASON.cmbBox.Clear
		CMB_REASON.cmbBox.Add("Actual Count")
	Else
		CMB_REASON.cmbBox.Clear
		CMB_REASON.cmbBox.Add("Not Prepared - Add")
		CMB_REASON.cmbBox.Add("Stock Transfer In Not Encoded - Add")
		CMB_REASON.cmbBox.Add("Cancelled Not Updated - Add")
		CMB_REASON.cmbBox.Add("Already Returned Before Count - Add")
		CMB_REASON.cmbBox.Add("Reserved Encoded - Add")
		CMB_REASON.cmbBox.Add("Short Prepared - Add")
		CMB_REASON.cmbBox.Add("Advance Prepared - Deduct")
		CMB_REASON.cmbBox.Add("Stock Transfer Out Not Encoded - Deduct")
		CMB_REASON.cmbBox.Add("Over Delivery - Deduct")
		CMB_REASON.cmbBox.Add("Cancelled Not Return - Deduct")
		CMB_REASON.cmbBox.Add("Over Prepared - Deduct")
	End If
End Sub
Sub CMB_REASON_SelectedIndexChanged (Index As Int)
	OpenSpinner(CMB_UNIT.cmbBox)
	Sleep(10)
	CMB_UNIT.SelectedIndex = -1
End Sub
Sub CMB_UNIT_SelectedIndexChanged (Index As Int)
	EDITTEXT_QUANTITY.RequestFocus
	EDITTEXT_QUANTITY.SelectAll
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
End Sub
Sub BUTTON_SAVE_Click
	If LOGIN_MODULE.username <> "" Or LOGIN_MODULE.tab_id <> "" Then
	Dim system_count As String
	Dim transaction_number As String
	Dim date1 As String
	Dim time1 As String
	If EDITTEXT_QUANTITY.Text = "" Or EDITTEXT_QUANTITY.Text <= 0  Then
		Msgbox2Async("You cannot input a zero value quantity.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		EDITTEXT_QUANTITY.RequestFocus
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
		Sleep(0)
		EDITTEXT_QUANTITY.SelectAll
	Else
		If CMB_UNIT.cmbBox.SelectedIndex = -1 Then
			CMB_UNIT.cmbBox.SelectedIndex = 0
		End If
					
		If CMB_REASON.cmbBox.SelectedIndex = -1 Then
			CMB_REASON.cmbBox.SelectedIndex = 0
		End If
					
		If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
			total_pieces = caseper * EDITTEXT_QUANTITY.text
		Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
			total_pieces = pcsper * EDITTEXT_QUANTITY.text
		Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
			total_pieces = dozper * EDITTEXT_QUANTITY.text
		Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
			total_pieces = boxper * EDITTEXT_QUANTITY.text
		Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
			total_pieces = bagper * EDITTEXT_QUANTITY.text
		Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
			total_pieces = packper * EDITTEXT_QUANTITY.text
		End If
		If BUTTON_SAVE.Text = " SAVE" Then
			cursor7 = connection.ExecQuery("SELECT MAX(CAST(item_number as INT)) as item_number from daily_inventory_disc_table WHERE group_id = '" & DAILY_INVENTORY_MODULE.group_id & "' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'")
			If cursor7.RowCount > 0 Then
			For i = 0 To cursor7.RowCount - 1
				cursor7.Position = i
				If cursor7.GetString("item_number") = Null Or cursor7.GetString("item_number") = "" Then
					transaction_number = 1
				Else
					transaction_number = cursor7.GetString("item_number") + 1
				End If
				Next
				If LABEL_LOAD_STATUS.Text = "SAVED" Then
					date1 = "-"
					time1 = "-"
				Else
					date1 = LABEL_LOAD_DATE.Text.SubString2(0,LABEL_LOAD_DATE.Text.IndexOf(" "))
					time1 = LABEL_LOAD_DATE.Text.SubString2(LABEL_LOAD_DATE.Text.IndexOf(" ")+1,LABEL_LOAD_DATE.Text.Length)
				End If
			End If
			cursor8 = connection.ExecQuery("SELECT system_count from daily_inventory_disc_table WHERE group_id = '" & DAILY_INVENTORY_MODULE.group_id & "' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"' and product_id = '"&product_id&"' GROUP BY product_id")
			If cursor8.RowCount > 0 Then
				For i = 0 To cursor8.RowCount - 1
					cursor8.Position = i
					If cursor8.GetString("system_count") = Null Or cursor8.GetString("system_count") = "" Or cursor8.GetString("system_count") = "0" Then
						system_count = "0"
					Else
						system_count = cursor8.GetString("system_count")
					End If
				Next
			Else
				system_count = "0"
			End If
			
			Dim query As String = "INSERT INTO daily_inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			connection.ExecNonQuery2(query,Array As String(DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text,transaction_number, _
			principal_id, LABEL_LOAD_PRINCIPAL.Text, product_id, LABEL_LOAD_VARIANT.Text, LABEL_LOAD_DESCRIPTION.Text, CMB_UNIT.cmbBox.SelectedItem, _
			EDITTEXT_QUANTITY.Text, total_pieces, system_count, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), LABEL_LOAD_STATUS.Text, date1, time1, _
			scan_code, reason, CMB_REASON.cmbBox.SelectedItem, 0, "-", "-", LOGIN_MODULE.tab_id, LOGIN_MODULE.username))
			Sleep(0)
			Dim unit As String = CMB_UNIT.cmbBox.SelectedItem
			If unit = "PCS" Then
				If EDITTEXT_QUANTITY.Text > 1 Then
					unit = "PIECES"
				Else
					unit = "PIECE"
				End If
			Else If unit = "DOZ" Then
				unit = "DOZEN"
			End If
			Sleep(0)
'			TTS1.Speak(EDITTEXT_QUANTITY.Text & " " & unit & " ADDED." , True)
			Sleep(0)
			ToastMessageShow("Product Added", False)
		Else
			Msgbox2Async("Are you sure you want to update this item?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				If Result = DialogResponse.POSITIVE Then
					If LABEL_LOAD_STATUS.Text = "FINAL" Then
						Dim insert_query As String = "INSERT INTO daily_inventory_disc_table_trail SELECT *,'EDITED' as 'edit_type',? as 'edit_by',? as 'date',? as 'time' from daily_inventory_disc_table WHERE group_id = ? AND inventory_date = ? AND item_number = ?"
						connection.ExecNonQuery2(insert_query, Array As String(LOGIN_MODULE.username, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text,item_number))
					End If
					Sleep(100)
					Dim query As String = "UPDATE daily_inventory_disc_table SET unit = ?, quantity = ?, total_pieces = ? , edit_count = edit_count + 1, input_reason = ? , user_info = ? WHERE group_id = ? AND inventory_date = ? AND item_number = ?"
					connection.ExecNonQuery2(query,Array As String(CMB_UNIT.cmbBox.SelectedItem, EDITTEXT_QUANTITY.Text, total_pieces, CMB_REASON.cmbBox.SelectedItem,LOGIN_MODULE.username, DAILY_INVENTORY_MODULE.group_id, LABEL_LOAD_INV_DATE.Text, item_number))
					Sleep(0)
					ToastMessageShow("Transaction Updated", False)
					LOAD_REASON
				End If
			End If
		End If
		GET_DAILY
		Sleep(0)
		LOAD_LIST
		Sleep(10)
		CLEAR
	End If
	Else
		Msgbox2Async("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
End Sub
Sub LOAD_LIST
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LVL_LIST.Background = bg
	LVL_LIST.Clear
	Sleep(0)
	LVL_LIST.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LVL_LIST.TwoLinesLayout.Label.TextSize = 20
	LVL_LIST.TwoLinesLayout.label.Height = 8%y	
	LVL_LIST.TwoLinesLayout.Label.TextColor = Colors.Black
	LVL_LIST.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LVL_LIST.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LVL_LIST.TwoLinesLayout.SecondLabel.Top = 6%y
	LVL_LIST.TwoLinesLayout.SecondLabel.TextSize = 14
	LVL_LIST.TwoLinesLayout.SecondLabel.Height = 4%y
	LVL_LIST.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LVL_LIST.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LVL_LIST.TwoLinesLayout.ItemHeight = 12%y
	Dim numbercount As Int
	cursor2 = connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.text&"' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"' ORDER BY item_number ASC")
	If cursor2.RowCount > 0 Then
		For row = 0 To cursor2.RowCount - 1
			cursor2.Position = row
			numbercount = numbercount + 1
			LVL_LIST.AddTwoLines2(cursor2.GetString("quantity") & " " & cursor2.GetString("unit"),cursor2.GetString("input_reason"),cursor2.GetString("item_number"))
		Next
	End If
	Sleep(0)
	cursor3 = connection.ExecQuery("SELECT sum(total_pieces) as 'act' FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.text& _
	"' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text& "' and input_reason IN ('Actual Count','Wrong Count') ORDER BY item_number ASC")
	If cursor3.RowCount > 0 Then
		For row = 0 To cursor3.RowCount - 1
			cursor3.Position = row
			numbercount = numbercount + 1
			If cursor3.GetString("act") = Null Or cursor3.GetString("act") = "" Then
				LABEL_LOAD_ACTUAL.Text = 0
			Else	
				LABEL_LOAD_ACTUAL.Text = Number.Format3(cursor3.GetString("act"),0,0,0,".",",","",0,15)
			End If
		Next
	End If
	Sleep(0)
	cursor4 = connection.ExecQuery("SELECT sum(total_pieces) as 'add' FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id& _
	"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.text& "' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text& _
	"' and input_reason IN ('Not Prepared - Add','Stock Transfer Not Encoded - Add','Cancelled Not Updated - Add','Already Returned Before Count - Add') ORDER BY item_number ASC")
	If cursor4.RowCount > 0 Then
		For row = 0 To cursor4.RowCount - 1
			cursor4.Position = row
			numbercount = numbercount + 1
			If cursor4.GetString("add") =  Null Or cursor4.GetString("add") = "" Then
				LABEL_LOAD_ADD.Text = 0
			Else
				LABEL_LOAD_ADD.Text = Number.Format3(cursor4.GetString("add"),0,0,0,".",",","",0,15)
			End If
		Next
	End If
	Sleep(0)
	cursor5 = connection.ExecQuery("SELECT sum(total_pieces) as 'deduct' FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id& _
	"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.text& "' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text& _
	"' and input_reason IN ('Advance Prepared - Deduct','Stock Transfer Out Not Encoded - Deduct','Over Delivery - Deduct','Cancelled Not Return - Deduct') ORDER BY item_number ASC")
	If cursor5.RowCount > 0 Then
		For row = 0 To cursor5.RowCount - 1
			cursor5.Position = row
			numbercount = numbercount + 1
			If cursor5.GetString("deduct") = Null Or cursor5.GetString("deduct") = "" Then
				LABEL_LOAD_DEDUCT.Text = 0
			Else
				LABEL_LOAD_DEDUCT.Text = Number.Format3(cursor5.GetString("deduct"),0,0,0,".",",","",0,15)
			End If
		Next
	End If
	EDITTEXT_QUANTITY.Text = ""
End Sub
Sub LVL_LIST_ItemClick (Position As Int, Value As Object)
	If LABEL_LOAD_STATUS.Text <> "UPLOADED" Then
	cursor3 = connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.text&"' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"' and item_number = '"&Value&"' ORDER BY item_number ASC")
	If cursor3.RowCount > 0 Then
		For row = 0 To cursor3.RowCount - 1
			cursor3.Position = row
			If LABEL_LOAD_STATUS.Text = "SAVED" Or LABEL_LOAD_STATUS.Text = "FINAL" Then
				Msgbox2Async("Item Number : " & cursor3.GetString("item_number") & CRLF _
				& "Unit : " & cursor3.GetString("unit") & CRLF _
				& "Quantity : " & cursor3.GetString("quantity") & CRLF _
				& "Total Pieces : " & cursor3.GetString("total_pieces") & CRLF _
				, "Option", "EDIT", "CANCEL", "DELETE", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					
						If cursor3.GetString("input_reason") = "Actual Count" Or cursor3.GetString("input_reason") = "Wrong Count" Then
						CMB_REASON.cmbBox.Clear
						CMB_REASON.cmbBox.Add("Wrong Count")
					Else
						CMB_REASON.SelectedIndex = CMB_REASON.cmbBox.IndexOf(cursor3.GetString("input_reason"))
					End If
					Sleep(0)
					EDITTEXT_QUANTITY.Text = cursor3.GetString("quantity")
					Sleep(0)
					CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf(cursor3.GetString("unit"))
					item_number = cursor3.GetString("item_number")
					Dim bg As ColorDrawable
					bg.Initialize2(Colors.RGB(0,167,255), 5, 0, Colors.LightGray)
					BUTTON_SAVE.Background = bg
					BUTTON_SAVE.Text = " Edit"
					BUTTON_CANCEL.Visible = True		
				Else if Result = DialogResponse.NEGATIVE Then
					Msgbox2Async("Are you sure you want to delete this item?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					Wait For Msgbox_Result (Result As Int)
					If Result = DialogResponse.POSITIVE Then
						If LABEL_LOAD_STATUS.Text = "FINAL" Then
						Dim insert_query As String = "INSERT INTO daily_inventory_disc_table_trail SELECT *,'DELETED' as 'edit_type',? as 'edit_by',? as 'date',? as 'time' from daily_inventory_disc_table WHERE group_id = ? AND inventory_date = ? AND item_number = ?"
						connection.ExecNonQuery2(insert_query, Array As String(LOGIN_MODULE.username, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text,Value))
						End If
						Dim query As String = "DELETE from daily_inventory_disc_table WHERE group_id = ? AND inventory_date = ? AND item_number = ?"
						connection.ExecNonQuery2(query,Array As String(DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text,Value))
						ToastMessageShow("Deleted Successfully", True)
						GET_DAILY
						Sleep(0)
						LOAD_LIST
					End If
				End If
			End If

		Next
	End If
	End If
End Sub
Sub BUTTON_CANCEL_Click
	CLEAR
End Sub
Sub CLEAR
	item_number = 0
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(0,255,70), 5, 0, Colors.LightGray)
	BUTTON_SAVE.Background = bg
	BUTTON_SAVE.Text = " SAVE"
	EDITTEXT_QUANTITY.Text = ""
	BUTTON_CANCEL.Visible = False
End Sub
#End Region

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
Sub UPDATE_PRODUCTOFFTAKE
	Dim cmd As DBCommand = CreateCommand("update_offtake_date", Array(DateTime.Date(DateTime.Now)))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		UPDATE_FINAL
	Else
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
	End If
	js.Release
End Sub
Sub UPDATE_FINAL
	Dim trigger As Int = 0
	PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)
	PANEL_BG_MSGBOX.BringToFront
	LABEL_HEADER_TEXT.Text = "Downloading Data"
	LABEL_MSGBOX2.Text = "Fetching Data..."
	LABEL_MSGBOX1.Text = "Finalizing system count, Please wait..."
	cursor3 = connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"'")
	If cursor3.RowCount > 0 Then
		For rows = 0 To cursor3.RowCount - 1
			cursor3.Position = rows
			Dim req As DBRequestManager = CreateRequest
			Dim cmd As DBCommand = CreateCommand("select_system_count", Array As String(cursor3.GetString("product_id")))
			Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
			Sleep(100)
			If jr.Success Then
				req.HandleJobAsync(jr, "req")
				Wait For (req) req_Result(res As DBResult)
				If res.Rows.Size > 0 Then
					For Each row() As Object In res.Rows
						LABEL_MSGBOX2.textColor = Colors.Black
						LABEL_MSGBOX2.Text = "Updating: " & cursor3.GetString("product_description")
						Sleep(200)
						cursor2 = connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND product_id = '"&cursor3.GetString("product_id")&"' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"' GROUP BY product_id")
						If cursor2.RowCount > 0 Then
							For rowq = 0 To cursor2.RowCount - 1
								cursor2.Position = rowq
								Dim query As String = "UPDATE daily_inventory_disc_table SET system_count = ?, date_final = ?, time_final = ? WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND product_id = '"&cursor3.GetString("product_id")&"' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'"
								connection.ExecNonQuery2(query,Array As String(row(res.Columns.Get("total_pcs")), DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now)))
								Sleep(0)
							Next
						Else
							Dim transaction_number As String
							cursor5 = connection.ExecQuery("SELECT MAX(CAST(item_number as INT)) as item_number from daily_inventory_disc_table WHERE group_id = '" & DAILY_INVENTORY_MODULE.group_id & "' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'")
							For i = 0 To cursor5.RowCount - 1
								cursor5.Position = i
								If cursor5.GetString("item_number") = Null Or cursor5.GetString("item_number") = "" Then
									transaction_number = 1
								Else
									transaction_number = cursor5.GetString("item_number") + 1
								End If
							Next
							Dim query As String = "INSERT INTO daily_inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
							connection.ExecNonQuery2(query,Array As String(DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text,transaction_number, _
							cursor3.GetString("principal_id"), cursor3.GetString("principal_name"), cursor3.GetString("product_id"), cursor3.GetString("product_variant"), cursor3.GetString("product_description"), "PCS", _
							"0", "0", row(res.Columns.Get("total_pcs")), DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), "FINAL",DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), _
							"N/A", "N/A", "Actual Count", 0, "-", "-", LOGIN_MODULE.tab_id, LOGIN_MODULE.username))
						End If
					Next
				Else
					Sleep(200)
					LABEL_MSGBOX2.textColor = Colors.Black
					LABEL_MSGBOX2.Text = "Updating: " & cursor3.GetString("product_description")
					cursor2 = connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND product_id = '"&cursor3.GetString("product_id")&"' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"' GROUP BY product_id")
					If cursor2.RowCount > 0 Then
						For rowq = 0 To cursor2.RowCount - 1
							cursor2.Position = rowq
							Dim query As String = "UPDATE daily_inventory_disc_table SET system_count = ?, status = ?, date_final = ?, time_final = ? WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND product_id = '"&cursor3.GetString("product_id")&"' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'"
							connection.ExecNonQuery2(query,Array As String("0", "FINAL", DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now)))
							Sleep(0)
						Next
					Else
						Dim transaction_number As String
						cursor5 = connection.ExecQuery("SELECT MAX(CAST(item_number as INT)) as item_number from daily_inventory_disc_table WHERE group_id = '" & DAILY_INVENTORY_MODULE.group_id & "' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'")
						For i = 0 To cursor5.RowCount - 1
							cursor5.Position = i
							If cursor5.GetString("item_number") = Null Or cursor5.GetString("item_number") = "" Then
								transaction_number = 1
							Else
								transaction_number = cursor5.GetString("item_number") + 1
							End If
						Next
						Dim query As String = "INSERT INTO daily_inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
						connection.ExecNonQuery2(query,Array As String(DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text,transaction_number, _
							cursor3.GetString("principal_id"), cursor3.GetString("principal_name"), cursor3.GetString("product_id"), cursor3.GetString("product_variant"), cursor3.GetString("product_description"), "PCS", _
							"0", "0", "0", DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), "FINAL",DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), _
							"N/A", "N/A", "Actual Count", 0, "-", "-", LOGIN_MODULE.tab_id, LOGIN_MODULE.username))
					End If
				End If
			Else
				trigger = 1
				Log("error")
				Log(cursor3.GetString("product_description"))
				LABEL_MSGBOX2.TextColor = Colors.red
				LABEL_MSGBOX2.Text = "ERROR UPDATING: " & cursor3.GetString("product_description")
				rows = cursor3.RowCount
			End If
			jr.Release
		Next
		If trigger = 0 Then
		Dim query As String = "UPDATE daily_inventory_disc_table SET  status = ? WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' and inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'"
		connection.ExecNonQuery2(query,Array As String("FINAL"))
		Sleep(0)
		LABEL_MSGBOX2.Text = "Updated Successfully"
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Transaction Updated", False)
		Sleep(0)
		GET_STATUS
		Sleep(0)
		GET_DAILY
		Else
			Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				UPDATE_FINAL
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Updating Failed", False)
			End If
		End If
	End If
End Sub
Sub DELETE_DAILY_DISC
	error_trigger = 0
	PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)
	PANEL_BG_MSGBOX.BringToFront
	LABEL_HEADER_TEXT.Text = "Uploading Data"
	LABEL_MSGBOX2.Text = "Fetching Data..."
	LABEL_MSGBOX1.Text = "Loading, Please wait..."
	Dim cmd As DBCommand = CreateCommand("delete_daily_disc", Array(DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_DAILY_DISC
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_DAILY_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_DAILY_DISC
	Dim date As String = DateTime.Date(DateTime.Now)
	Dim time As String = DateTime.Time(DateTime.Now)
	cursor7 = connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'")
	If cursor7.RowCount > 0 Then
		For i = 0 To cursor7.RowCount - 1
			cursor7.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_daily_disc", Array As String(cursor7.GetString("group_id"),cursor7.GetString("inventory_date"),cursor7.GetString("item_number"),cursor7.GetString("principal_id"),cursor7.GetString("principal_name"), _
			cursor7.GetString("product_id"),cursor7.GetString("product_variant"),cursor7.GetString("product_description"),cursor7.GetString("unit"),cursor7.GetString("quantity"),cursor7.GetString("total_pieces"), _
			cursor7.GetString("system_count"),cursor7.GetString("date_registered"),cursor7.GetString("time_registered"),cursor7.GetString("status"),cursor7.GetString("date_final"),cursor7.GetString("time_final"), _
			cursor7.GetString("scan_code"),cursor7.GetString("reason"),cursor7.GetString("input_reason"),cursor7.GetString("edit_count"),cursor7.GetString("date_upload"),cursor7.GetString("time_upload"), _
			cursor7.GetString("tab_id"),cursor7.GetString("user_info")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			LABEL_MSGBOX2.Text = "Uploading : " & cursor7.GetString("product_description")
			Wait For(js) JobDone(js As HttpJob)
		If js.Success Then

		Else
			error_trigger = 1
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Uploading Failed", False)
			Log("ERROR: " & js.ErrorMessage)
			Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			ToastMessageShow("Uploading Error", False)
			Sleep(1000)
		End If
		js.Release
		Next
	End If
	Sleep(1000)
	If error_trigger = 0 Then
	DELETE_DAILY_DISC_TRAIL
	Else
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_DAILY_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_DAILY_DISC_TRAIL
	Dim cmd As DBCommand = CreateCommand("delete_daily_disc_trail", Array(DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		INSERT_DAILY_DISC_TRAIL
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_DAILY_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_DAILY_DISC_TRAIL
	Sleep(0)
	cursor2 = connection.ExecQuery("SELECT * FROM daily_inventory_disc_table_trail WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'")
	If cursor2.RowCount > 0 Then
		For i2 = 0 To cursor2.RowCount - 1
			cursor2.Position = i2
			Dim cmd As DBCommand = CreateCommand("insert_daily_disc_trail", Array As String(cursor2.GetString("group_id"),cursor2.GetString("inventory_date"),cursor2.GetString("item_number"),cursor2.GetString("principal_name"),cursor2.GetString("principal_id"), _
			cursor2.GetString("product_id"),cursor2.GetString("product_variant"),cursor2.GetString("product_description"),cursor2.GetString("unit"),cursor2.GetString("quantity"),cursor2.GetString("total_pieces"), _
			cursor2.GetString("system_count"),cursor2.GetString("date_registered"),cursor2.GetString("time_registered"),cursor2.GetString("status"),cursor2.GetString("date_final"),cursor2.GetString("time_final"), _
			cursor2.GetString("scan_code"),cursor2.GetString("reason"),cursor2.GetString("input_reason"),cursor2.GetString("edit_count"),cursor2.GetString("date_upload"),cursor2.GetString("time_upload"), _
			cursor2.GetString("tab_id"),cursor2.GetString("user_info"),cursor2.GetString("edit_type"),cursor2.GetString("edit_by"),cursor2.GetString("edit_date"),cursor2.GetString("edit_time")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
		If js.Success Then
			
		Else
			error_trigger = 1
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Uploading Failed", False)
			ProgressDialogHide()
			Log("ERROR: " & js.ErrorMessage)
			Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			ToastMessageShow("Uploading Error", False)
			Sleep(1000)
		End If
		js.Release
		Next
	End If
	Sleep(1000)
	If error_trigger = 0 Then
		DELETE_DAILY_REF
	Else
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_DAILY_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_DAILY_REF
	Dim cmd As DBCommand = CreateCommand("delete_daily_ref", Array(DAILY_INVENTORY_MODULE.group_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_DAILY_REF
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_DAILY_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_DAILY_REF
	Sleep(0)
	cursor3 = connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"'")
	If cursor3.RowCount > 0 Then
		For i3 = 0 To cursor3.RowCount - 1
			cursor3.Position = i3
			Dim cmd As DBCommand = CreateCommand("insert_daily_ref", Array As String(cursor3.GetString("group_id"),cursor3.GetString("principal_id"),cursor3.GetString("principal_name"), _
			cursor3.GetString("product_id"),cursor3.GetString("product_variant"),cursor3.GetString("product_description"), _
			cursor3.GetString("date_registered"),cursor3.GetString("time_registered"), _
			cursor3.GetString("tab_id"),cursor3.GetString("user_info")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
		If js.Success Then

		Else
			error_trigger = 1
			Log("ERROR: " & js.ErrorMessage)
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Uploading Failed", False)
			Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			ToastMessageShow("Uploading Error", False)
			Sleep(1000)
		End If
		js.Release
		Next
	End If
	Sleep(1000)
	If error_trigger = 0 Then
		DELETE_DAILY_TEMPLATE
	Else
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_DAILY_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_DAILY_TEMPLATE
	Dim cmd As DBCommand = CreateCommand("delete_daily_template", Array(DAILY_INVENTORY_MODULE.group_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_DAILY_TEMPLATE
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_DAILY_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_DAILY_TEMPLATE
	Sleep(0)
	cursor4 = connection.ExecQuery("SELECT * FROM daily_template_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"'")
	If cursor4.RowCount > 0 Then
		For i4 = 0 To cursor4.RowCount - 1
			cursor4.Position = i4
			Dim cmd As DBCommand = CreateCommand("insert_daily_template", Array As String(cursor4.GetString("group_id"),cursor4.GetString("group_name"),cursor4.GetString("month"),cursor4.GetString("year"), _
			cursor4.GetString("date_registered"),cursor4.GetString("time_registered"),cursor4.GetString("edit_count"), _
			cursor4.GetString("date_updated"),cursor4.GetString("time_updated"), _
			cursor4.GetString("user_info"),cursor4.GetString("tab_id")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
		If js.Success Then
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Uploading Failed", False)
			Log("ERROR: " & js.ErrorMessage)
			Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			ToastMessageShow("Uploading Error", False)
			Sleep(1000)
			error_trigger = 1
		End If
		js.Release
		Next
		LABEL_MSGBOX2.Text = "Uploaded Successfully"
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Transaction Uploaded", False)
		If error_trigger = 0 Then
			Dim query As String = "UPDATE daily_inventory_disc_table SET status = ?, date_upload = ? , time_upload = ? WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' AND inventory_date = '"&LABEL_LOAD_INV_DATE.Text&"'"
			connection.ExecNonQuery2(query,Array As String("UPLOADED",date, time))
			Sleep(1000)
		Else
			Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				DELETE_DAILY_DISC
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Updating Failed", False)
			End If
		End If
		Sleep(0)
		GET_STATUS
	End If
End Sub
#End Region

Sub BUTTON_PREV_Click
	TABLE_DAILY_INVENTORY.CurrentPage = TABLE_DAILY_INVENTORY.CurrentPage - 1
	Sleep(0)
	page = page - 1
	Sleep(0)
	LABEL_PAGE.Text = "PAGE : " & page
End Sub
Sub BUTTON_NEXT_Click
	TABLE_DAILY_INVENTORY.CurrentPage = TABLE_DAILY_INVENTORY.CurrentPage + 1
	Sleep(0)
	page = page + 1
	Sleep(0)
	LABEL_PAGE.Text = "PAGE : " & page
End Sub

#Region Calculator
Sub oncal
	LoadTexts
	
	Scale_Calc.SetRate(0.6)
	ScaleAuto = Scale_Calc.GetScaleDS

	
'	If 100%x < 100%y Then
'		Scale.HorizontalCenter(pnlKeyboard)
'		Scale.SetBottom(pnlKeyboard, 100%y - 8dip * ScaleAuto)
'		
'		Scale.HorizontalCenter(scvPaperRoll)
'		Scale.SetTopAndBottom2(scvPaperRoll, Activity, 8, pnlKeyboard, 8)
'	Else
'		Scale.VerticalCenter(pnlKeyboard)
'		Scale.SetRight(pnlKeyboard, 100%x)
'		
'		Scale.SetLeftAndRight(scvPaperRoll, 8dip * ScaleAuto, pnlKeyboard.Left - 5dip * ScaleAuto)
'		Scale.SetTopAndBottom(scvPaperRoll, 5dip * ScaleAuto, 100%y - 5dip * ScaleAuto)
'	End If
	
	lblPaperRoll.Initialize("lblPaperRoll")
	scvPaperRoll.Panel.AddView(lblPaperRoll, 0, 0, 100%x, scvPaperRoll.Height)
	scvPaperRoll.Panel.Height = scvPaperRoll.Height
	lblPaperRoll.TextSize = 22 * ScaleAuto
	lblPaperRoll.Color = Colors.White
	lblPaperRoll.TextColor = Colors.Black
	
'	imgCharSizeDown.Initialize(File.DirAssets, "btnCharSizeDown.bmp")
'	imgCharSizeUp.Initialize(File.DirAssets, "btnCharSizeUp.bmp")
	'
'	bd1.Initialize(imgCharSizeUp)
'	bd2.Initialize(imgCharSizeDown)
'	sd.Initialize
'	Dim states(2) As Int
'	states(0) = sd.state_unchecked
'	states(1) = -sd.state_checked
'	sd.addState2(states, bd1)
'	Dim states(1) As Int
'	states(0) = sd.state_checked
'	sd.addState2(states, bd2)
'	btnCharSize.Background = sd
End Sub
Sub btnDigit_Click
	Dim bs As String, Send As View
	
	If New1 = 0 Then
		New1 = 1
	End If
	
	Send = Sender
	bs = Send.Tag
	
	Select bs
		Case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "3.1415926535897932", "."
			Select Op0
				Case "g", "s", "m", "x"
					Return
			End Select

			If Op0 = "e" Then
				Txt = Txt & CRLF & CRLF
				sVal = ""
				Op0 = ""
				Result1 = 0
				New1 = 0
			End If

			If bs = "3.1415926535897932" Then
				If sVal <> "" Then
					Return
				End If
				Txt = Txt & cPI
				sVal = cPI
			Else If bs = "." Then
				If sVal.IndexOf(".") < 0 Then
					Txt = Txt & bs
					sVal = sVal & bs
				End If
			Else
				Txt = Txt & bs
				sVal = sVal & bs
			End If
		Case "a", "b", "c", "d", "e", "y"
			If sVal ="" Then
				Select Op0
					Case "a", "b", "c", "d", "y", ""
						Return
				End Select
				sVal = Result1
			End If
			GetValue(bs)
		Case "g", "s", "x"
			If sVal = "" Then
				Select Op0
					Case "a", "b", "c", "d", "y", ""
						Return
				End Select
				sVal = Result1
			End If
			If Op0 = "" Then
				Result1 = sVal
			End If
			GetValue(bs)
		Case "f"
			If Op0 = "e" Then
				Txt = Txt & CRLF & CRLF
				New1 = 0
				sVal = ""
				Op0 = ""
			End If
	End Select

	UpdateTape
End Sub
Sub GetValue(Op As String)
	If Op0 = "e" And (Op = "s" Or Op = "g" Or Op = "x") Then
		Val = Result1
	Else
		Val = sVal
	End If
	
	sVal = ""
	
	If Op = "g" Or Op = "s" Or Op = "x" Then
		If Op0 = "a" Or Op0 = "b" Or Op0 = "c" Or Op0 = "d" Or Op0 = "y" Then
			CalcResult1(Op0)
			Txt = Txt & "  = " & Result1
		End If
		Operation(Op)
		CalcResult1(Op)
		Txt = Txt & "  = " & Result1
		Op0 = Op
		Op0 = "e"
		Op = "e"
		Return
	End If
	
	If New1 = 1 Then
		Result1 = Val
		Operation(Op)
		If Op = "g" Or Op = "s" Or Op = "x" Then
			CalcResult1(Op)
			Txt = Txt & "  = " & Result1
			Op = "e"
		End If
		UpdateTape
		New1 = 2
		Op0 = Op
		Return
	End If
	
	If Op = "e" Then
		If Op0 = "e" Then
			Return
		End If
		Txt = Txt & CRLF & " =  "
		CalcResult1(Op0)
		Txt = Txt & Result1
	Else
		If Op0 = "g" Or Op0 = "s" Or Op0 = "x" Then
			Operation(Op)
			Op0 = Op
			Return
		End If
		
		CalcResult1(Op0)
		If Op0<>"e" Then
			Txt = Txt & "  = " & Result1
		End If
		Operation(Op)
		If Op = "g" Or Op = "s" Or Op = "x" Then
			CalcResult1(Op)
			Txt = Txt & "  = " & Result1
			Op = "e"
		End If
	End If
	Op0 = Op
End Sub
Sub Operation(Op As String)
	Select Op
		Case "a"
			Txt = Txt & CRLF & "+ "
		Case "b"
			Txt = Txt & CRLF & "- "
		Case "c"
			Txt = Txt & CRLF & "× "
		Case "d"
			Txt = Txt & CRLF & "/ "
		Case "g"
			Txt = Txt & CRLF & "x2 "
		Case "s"
			Txt = Txt & CRLF & "√ "
		Case "x"
			Txt = Txt & CRLF & "1/x "
		Case "y"
			Txt = Txt & CRLF & "% "
	End Select
End Sub
Sub CalcResult1(Op As String)
	Select Op
		Case "a"
			Result1 = Result1 + Val
		Case "b"
			Result1 = Result1 - Val
		Case "c"
			Result1 = Result1 * Val
		Case "d"
			Result1 = Result1 / Val
		Case "g"
			Result1 = Result1 * Result1
		Case "s"
			Result1 = Sqrt(Result1)
		Case "x"
			If Result1 <> 0 Then
				Result1 = 1 / Result1
			End If
		Case "y"
			Result1 = Result1 * Val / 100
	End Select
	Dim res As String = Result1
	LABEL_LOAD_ANSWER.Text = res
End Sub
Sub UpdateTape
	Dim hr As Float
	
	lblPaperRoll.Text = Txt

	hr = stu.MeasureMultilineTextHeight(lblPaperRoll, Txt)
	If hr > scvPaperRoll.Height Then
		lblPaperRoll.Height = hr
		scvPaperRoll.Panel.Height = hr
		Sleep(0)
		scvPaperRoll.ScrollPosition = hr
	End If
End Sub
Sub btnClr_Click
'	Dim Answer As Int

	''	Answer = Msgbox2("Do you really want to clear the calculation?","A T T E N T I O N","Yes","","No",Null)
'	Answer = Msgbox2(Texts(4),Texts(5),Texts(2),"",Texts(3),Null)
	'
'	If Answer = -1 Then
	Val = 0
	sVal = ""
	Result1 = 0
	New1 = 0
	Txt = ""
	Op0 = ""
	lblPaperRoll.Text = ""
	lblPaperRoll.Height = scvPaperRoll.Height
	scvPaperRoll.Panel.Height = scvPaperRoll.Height
	LABEL_LOAD_ANSWER.Text = "0"
	'End If
End Sub
Sub btnBack_Click
	If sVal.Length > 0 Then
		Txt = sVal.SubString2(0, sVal.Length - 1)
		sVal = sVal.SubString2(0, sVal.Length - 1)
		UpdateTape
	End If
End Sub
Sub btnExit_Click
	Val = 0
	sVal = ""
	Result1 = 0
	New1 = 0
	Txt = ""
	Op0 = ""
	lblPaperRoll.Text = ""
	lblPaperRoll.Height = scvPaperRoll.Height
	scvPaperRoll.Panel.Height = scvPaperRoll.Height
	EDITTEXT_QUANTITY.Text = LABEL_LOAD_ANSWER.Text
	PANEL_BG_CALCU.SetVisibleAnimated(300,False)
End Sub
Sub btnCharSize_CheckedChange(Checked As Boolean)
	If Checked = False Then
		lblPaperRoll.TextSize = 16 * ScaleAuto
	Else
		lblPaperRoll.TextSize = 22 * ScaleAuto
	End If
End Sub
Sub LoadTexts
	Dim FileName As String
	Dim iq As Int

'	Locale.Initialize
'	LanguageID = Locale.Language
	FileName = "tapecalc_" & LanguageID & ".txt"
	If File.Exists(ProgPath, FileName) = False Then
		FileName = "tapecalc_en.txt"
	End If
	
	Dim Reader As TextReader
	Reader.Initialize(File.OpenInput(ProgPath, FileName))
	iq = 0
	Texts(iq) = Reader.ReadLine
	Do While Texts(iq) <> Null
		iq = iq + 1
		Texts(iq) = Reader.ReadLine
	Loop
	Reader.Close
End Sub
#End Region

Sub PANEL_BG_INPUT_Click
	Return True
End Sub

Sub PANEL_BG_MSGBOX_Click
	Return True
End Sub

Sub BUTTON_EXIT_CALCU_Click
	PANEL_BG_CALCU.SetVisibleAnimated(300,False)
End Sub

Sub BUTTON_CALCU_Click
	oncal
	PANEL_BG_CALCU.SetVisibleAnimated(300,True)
	PANEL_BG_CALCU.BringToFront
End Sub

Sub PANEL_BG_CALCU_Click
	Return True
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean
	If KeyCode = KeyCodes.KEYCODE_BACK Then
		Return True
	End If
End Sub