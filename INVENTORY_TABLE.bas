B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=9.801
@EndOfDesignText@
#Extends: android.support.v7.app.AppCompatActivity

#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

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
	
	Dim phone As Phone
	
	'tss
	Dim TTS1 As TTS
	
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
	
	Dim orig_unit As String
	Dim orig_quantity As String
	Dim edit_count As String
	
	Dim caseper As String
	Dim pcsper As String
	Dim dozper As String
	Dim boxper As String
	Dim bagper As String
	Dim packper As String
	Dim total_pieces As String
	Dim error_trigger As String
	
	Private cartBitmap As Bitmap

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	'ime
	Dim CTRL As IME

	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	
	Private TABLE_INVENTORY As B4XTable
	Private cvs As B4XCanvas
	Private xui As XUI
	Private NameColumn(9) As B4XTableColumn
	
	Private XSelections As B4XTableSelections
	
	'	calcu
	Dim btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9 As Button
	Dim btnBack, btnClr, btnExit As Button
	Dim lblPaperRoll As Label
	Dim scvPaperRoll As ScrollView
	Dim pnlKeyboard As Panel
	Dim stu As StringUtils
	
	Private LABEL_LOAD_NUMBER As Label
	Private LABEL_LOAD_STATUS As Label
	Private LABEL_LOAD_PRINCIPAL As Label
	Private LABEL_LOAD_VARIANT As Label
	Private LABEL_LOAD_DESCRIPTION As Label
	Private CMB_UNIT As B4XComboBox
	Private EDITTEXT_QUANTITY As EditText
	Private PANEL_BG_RECOUNT As Panel
	Private BUTTON_EDIT As Button
	Private BUTTON_DELETE As Button
	Private BUTTON_RECOUNT As Button
	Private LABEL_MSGBOX2 As Label
	Private PANEL_BG_MSGBOX As Panel
	Private PANEL_BG_CALCU As Panel
	Private BUTTON_CALCU As Button
	Private LABEL_LOAD_ANSWER As Label
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
	Activity.LoadLayout("table_inventory")
	
	If FirstTime Then
		cartBitmap = LoadBitmap(File.DirAssets, "upload.png")
	End If
	
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
	
	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	phone.SetScreenOrientation(0)
	
	Sleep(0)
	Dim Ref As Reflector
	Ref.Target = EDITTEXT_QUANTITY ' The text field being referenced
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	
	LOAD_TABLE_HEADER
	Sleep(0)
	LOAD_INVENTORY_TABLE
	
	If MONTLHY_INVENTORY2_MODULE.inventory_type = "COUNT" Then
		BUTTON_RECOUNT.Visible = False
	Else
		BUTTON_RECOUNT.Visible = True
	End If
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "cart", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
End Sub

Sub Activity_Resume
	If TTS1.IsInitialized = False Then
		TTS1.Initialize("TTS1")
	End If
End Sub
Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub OpenSpinner(se As Spinner)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
End Sub

Sub LOAD_TABLE_HEADER
	NameColumn(0)=TABLE_INVENTORY.AddColumn("#", TABLE_INVENTORY.COLUMN_TYPE_NUMBERS)
	NameColumn(1)=TABLE_INVENTORY.AddColumn("Position", TABLE_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_INVENTORY.AddColumn("Principal", TABLE_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(3)=TABLE_INVENTORY.AddColumn("Product Variant", TABLE_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(4)=TABLE_INVENTORY.AddColumn("Product Description", TABLE_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(5)=TABLE_INVENTORY.AddColumn("Unit", TABLE_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(6)=TABLE_INVENTORY.AddColumn("Qty", TABLE_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(7)=TABLE_INVENTORY.AddColumn("Total Pieces", TABLE_INVENTORY.COLUMN_TYPE_TEXT)
	NameColumn(8)=TABLE_INVENTORY.AddColumn("Status", TABLE_INVENTORY.COLUMN_TYPE_TEXT)
	
	TABLE_INVENTORY.NumberOfFrozenColumns = 2
	
End Sub
Sub LOAD_INVENTORY_TABLE
	ProgressDialogShow2("Loading...",False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	Dim rs As ResultSet = connection.ExecQuery("SELECT * FROM inventory_disc_table WHERE transaction_id = '"&MONTHLY_INVENTORY_MODULE.transaction_id&"' ORDER BY inventory_status ASC")
	Do While rs.NextRow
		Dim row(9) As Object
		row(0) = rs.GetString("transaction_number")
		row(1) = rs.GetString("position")
		row(2) = rs.GetString("principal_name")
		row(3) = rs.GetString("product_variant")
'		'Some of the values are Null. We need to convert them to empty strings:
'		If row(2) = Null Then row(2) = ""
		row(4) = rs.GetString("product_description")
		row(5) = rs.GetString("unit")
		row(6) = rs.GetString("quantity")
		row(7) = rs.GetString("total_pieces")
		row(8) = rs.GetString("inventory_status")
		Data.Add(row)
	Loop
	rs.Close
	TABLE_INVENTORY.SetData(Data)
	If XSelections.IsInitialized = False Then
	XSelections.Initialize(TABLE_INVENTORY)
	XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	ProgressDialogHide
End Sub
Sub TABLE_INVENTORY_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0), NameColumn(1), NameColumn(2), NameColumn(3), NameColumn(4), NameColumn(5), NameColumn(6),NameColumn(7),NameColumn(8))
		Dim MaxWidth As Int
		For i = 0 To TABLE_INVENTORY.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 10dip)
		Next
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
	Next
	For i = 0 To TABLE_INVENTORY.VisibleRowIds.Size - 1
		Dim RowId As Long = TABLE_INVENTORY.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(8).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = TABLE_INVENTORY.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(8).Id)
			Log(row.Get(NameColumn(8).Id))
			If OtherColumnValue = ("COUNT") Then 
				clr = xui.Color_Red 
			Else If OtherColumnValue = ("RECOUNT") Then
				clr = xui.Color_Green
			Else
				clr = xui.Color_Transparent
			End If
			pnl1.GetView(0).Color = clr
		End If
	Next
	If ShouldRefresh Then
		TABLE_INVENTORY.Refresh
		XSelections.Clear
	End If
End Sub
Sub TABLE_INVENTORY_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Log(ColumnId & RowId)
	
	Dim RowData As Map = TABLE_INVENTORY.GetRow(RowId)
	Dim num As String = RowData.Get("#")
	Dim num1 As String = num.SubString2(0,num.IndexOf("."))
	Log(num.SubString2(0,num.IndexOf(".")))
	
	Dim query As String = "SELECT * FROM inventory_disc_table WHERE transaction_number = ? and transaction_id = ?"
	cursor1 = connection.ExecQuery2(query,Array As String(num1,MONTHLY_INVENTORY_MODULE.transaction_id))
	If cursor1.RowCount = 1 Then
		For i = 0 To cursor1.RowCount - 1
			Sleep(0)
			cursor1.Position = i
			LABEL_LOAD_PRINCIPAL.Text = cursor1.GetString("principal_name")
			LABEL_LOAD_VARIANT.Text = cursor1.GetString("product_variant")
			LABEL_LOAD_DESCRIPTION.Text = cursor1.GetString("product_description")
			CMB_UNIT.cmbBox.Clear
			CMB_UNIT.cmbBox.Add(cursor1.GetString("unit"))
			EDITTEXT_QUANTITY.Text = cursor1.GetString("quantity")
			LABEL_LOAD_NUMBER.Text = cursor1.GetString("transaction_number")
			edit_count = cursor1.GetString("edit_count")
		
			If cursor1.GetString("inventory_status") = "COUNT" Then
				LABEL_LOAD_STATUS.Text = " COUNTED"
				LABEL_LOAD_STATUS.Color = Colors.Red
			Else
				LABEL_LOAD_STATUS.Text = " RECOUNT"
				LABEL_LOAD_STATUS.Color = Colors.Green
			End If
		Next
		CMB_UNIT.cmbBox.Enabled = False
		EDITTEXT_QUANTITY.Enabled = False
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Black)
		Sleep(0)
		CMB_UNIT.cmbBox.DropdownTextColor = Colors.Black
		CMB_UNIT.cmbBox.TextColor = Colors.Black
		EDITTEXT_QUANTITY.Background = bg
		PANEL_BG_RECOUNT.SetVisibleAnimated(300,True)
		orig_unit = CMB_UNIT.cmbBox.SelectedItem
		orig_quantity  = EDITTEXT_QUANTITY.Text

		
		If MONTLHY_INVENTORY2_MODULE.inventory_type = "COUNT" And LABEL_LOAD_STATUS.Text = " RECOUNT" Then
			BUTTON_EDIT.Visible = False
			BUTTON_DELETE.Visible = False
		Else
			BUTTON_EDIT.Visible = True
			BUTTON_DELETE.Visible = True
			BUTTON_EDIT.Text = " Edit"
			BUTTON_EDIT.TextColor = Colors.Blue
			Sleep(0)
			BUTTON_DELETE.Text = " Delete"
			BUTTON_DELETE.TextColor = Colors.Red
		End If
	End If
End Sub
Sub TABLE_INVENTORY_CellLongClicked (ColumnId As String, RowId As Long)
	
End Sub

Sub PANEL_BG_RECOUNT_Click
	PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)
	TABLE_INVENTORY.Refresh
	XSelections.Clear
End Sub

Sub BUTTON_RECOUNT_Click
	If LOGIN_MODULE.username <> "" Or LOGIN_MODULE.tab_id <> "" Then
		If MONTLHY_INVENTORY2_MODULE.inventory_type = "COUNT" Then
			Msgbox2Async("You cannot recount an item because your inventory type is COUNT.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Else
			If BUTTON_EDIT.Text = " Edit" Then
				Dim query As String = "UPDATE inventory_disc_table SET inventory_status = ? , recount_by = ? , recount_person1 = ? , recount_person2 = ?, recount_date = ?, recount_time = ? WHERE transaction_number = ? AND transaction_id = ?"
				connection.ExecNonQuery2(query,Array As String("RECOUNT",LOGIN_MODULE.username,MONTLHY_INVENTORY2_MODULE.assign1,MONTLHY_INVENTORY2_MODULE.assign2,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),LABEL_LOAD_NUMBER.Text,MONTHLY_INVENTORY_MODULE.transaction_id))
				ProgressDialogShow2("Recounting...", False)
				Sleep(500)
				ProgressDialogHide()
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
				TTS1.Speak(EDITTEXT_QUANTITY.Text & " " & unit & " RECOUNTED." , True)
				Sleep(0)
				LOAD_INVENTORY_TABLE
				Sleep(0)
				NEXT_ITEM
			Else
				Dim insert_query As String = "INSERT INTO inventory_disc_table_trail SELECT *,? as 'edit_by','MODIFIED' as 'edit_type',? as edit_date,? as edit_time from inventory_disc_table WHERE transaction_number = ? AND transaction_id = ?"
				connection.ExecNonQuery2(insert_query,Array As String(LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),LABEL_LOAD_NUMBER.Text,MONTHLY_INVENTORY_MODULE.transaction_id))
				Sleep(0)
				If CMB_UNIT.cmbBox.SelectedIndex = -1 Then CMB_UNIT.cmbBox.SelectedIndex = 0
				Sleep(0)
			
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
		
				Dim query As String = "UPDATE inventory_disc_table SET unit = ? , quantity = ?, total_pieces = ?, edit_count = ?, inventory_status = ? , recount_by = ? , recount_person1 = ? , recount_person2 = ?, recount_date = ?, recount_time = ? WHERE transaction_number = ? AND transaction_id = ?"
				connection.ExecNonQuery2(query,Array As String(CMB_UNIT.cmbBox.SelectedItem,EDITTEXT_QUANTITY.Text,total_pieces,edit_count + 1,"RECOUNT",LOGIN_MODULE.username,MONTLHY_INVENTORY2_MODULE.assign1,MONTLHY_INVENTORY2_MODULE.assign2,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),LABEL_LOAD_NUMBER.Text,MONTHLY_INVENTORY_MODULE.transaction_id))
				ProgressDialogShow2("Recounting...", False)
				Sleep(500)
				ProgressDialogHide()
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
				TTS1.Speak(EDITTEXT_QUANTITY.Text & " " & unit & " UPDATED and RECOUNTED." , True)
				Sleep(0)
				BUTTON_EDIT.Text = " Edit"
				BUTTON_EDIT.TextColor = Colors.Blue
				Sleep(0)
				BUTTON_DELETE.Text = " Delete"
				BUTTON_DELETE.TextColor = Colors.Red
				LOAD_INVENTORY_TABLE
				Sleep(0)
				NEXT_ITEM
			End If
		End If
	Else
		Msgbox2Async("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
End Sub
Sub BUTTON_DELETE_Click
	If BUTTON_DELETE.Text = " Delete" Then
		Msgbox2Async("Are you sure you want to delete this item?", "Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Dim insert_query As String = "INSERT INTO inventory_disc_table_trail SELECT *,? as 'edit_by','DELETED' as 'edit_type',? as edit_date,? as edit_time from inventory_disc_table WHERE transaction_number = ? AND transaction_id = ?"
			connection.ExecNonQuery2(insert_query,Array As String(LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),LABEL_LOAD_NUMBER.Text,MONTHLY_INVENTORY_MODULE.transaction_id))
			Sleep(0)
			Dim query As String = "DELETE from inventory_disc_table WHERE transaction_number = ? AND transaction_id = ?"
			connection.ExecNonQuery2(query,Array As String(LABEL_LOAD_NUMBER.Text,MONTHLY_INVENTORY_MODULE.transaction_id))
			ProgressDialogShow2("Deleting...", False)
			Sleep(1500)
			ProgressDialogHide
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
			TTS1.Speak(EDITTEXT_QUANTITY.Text & " " & unit & " DELETED." , True)
			PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)
			Sleep(0)
			LOAD_INVENTORY_TABLE
			ToastMessageShow("Updated Successfully", True)
		Else
		
		End If
	Else
		Dim insert_query As String = "INSERT INTO inventory_disc_table_trail SELECT *,? as 'edit_by','MODIFIED' as 'edit_type',? as edit_date,? as edit_time from inventory_disc_table WHERE transaction_number = ? AND transaction_id = ?"
		connection.ExecNonQuery2(insert_query,Array As String(LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),LABEL_LOAD_NUMBER.Text,MONTHLY_INVENTORY_MODULE.transaction_id))
		
		If CMB_UNIT.cmbBox.SelectedIndex = -1 Then CMB_UNIT.cmbBox.SelectedIndex = 0
		Sleep(0)
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
		
		
		Dim query As String = "UPDATE inventory_disc_table SET unit = ? , quantity = ?, total_pieces = ?, edit_count = ? WHERE transaction_number = ? AND transaction_id = ?"
		connection.ExecNonQuery2(query,Array As String(CMB_UNIT.cmbBox.SelectedItem,EDITTEXT_QUANTITY.Text,total_pieces,edit_count + 1,LABEL_LOAD_NUMBER.Text,MONTHLY_INVENTORY_MODULE.transaction_id))
		ProgressDialogShow2("Updating...", False)
		Sleep(1500)
		ProgressDialogHide
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
		TTS1.Speak(EDITTEXT_QUANTITY.Text & " " & unit & " UPDATED." , True)
		Sleep(0)
		PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)
		Sleep(0)
		LOAD_INVENTORY_TABLE
		ToastMessageShow("Updated Successfully", True)
	End If
End Sub

Sub NEXT_ITEM
	Msgbox2Async("Item #("&LABEL_LOAD_NUMBER.Text&"), Product : "&LABEL_LOAD_DESCRIPTION.Text&", "&EDITTEXT_QUANTITY.Text&" "&CMB_UNIT.cmbBox.SelectedItem&"  been recounted. Would you like to proceed to next item?.", "Next Item", "Next", "", "Back", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		ProgressDialogShow2("Loading next item...", False)
		Sleep(1000)
		Dim query As String = "SELECT * FROM inventory_disc_table WHERE transaction_id = ? ORDER BY inventory_status ASC LIMIT 1"
		cursor3 = connection.ExecQuery2(query,Array As String(MONTHLY_INVENTORY_MODULE.transaction_id))
		If cursor3.RowCount = 1 Then
			For i = 0 To cursor3.RowCount - 1
				Sleep(0)
				cursor3.Position = i
				LABEL_LOAD_PRINCIPAL.Text = cursor3.GetString("principal_name")
				LABEL_LOAD_VARIANT.Text = cursor3.GetString("product_variant")
				LABEL_LOAD_DESCRIPTION.Text = cursor3.GetString("product_description")
				CMB_UNIT.cmbBox.Clear
				CMB_UNIT.cmbBox.Add(cursor3.GetString("unit"))
				EDITTEXT_QUANTITY.Text = cursor3.GetString("quantity")
				LABEL_LOAD_NUMBER.Text = cursor3.GetString("transaction_number")
		
				If cursor3.GetString("inventory_status") = "COUNT" Then
					LABEL_LOAD_STATUS.Text = " COUNTED"
					LABEL_LOAD_STATUS.Color = Colors.Red
				Else
					LABEL_LOAD_STATUS.Text = " RECOUNT"
					LABEL_LOAD_STATUS.Color = Colors.Green
				End If
			Next
			ProgressDialogHide()
			CMB_UNIT.cmbBox.Enabled = False
			EDITTEXT_QUANTITY.Enabled = False
		Else
			Msgbox2Async("You've reach the last number of transaction.", "", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
			PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)
		End If
	Else
		PANEL_BG_RECOUNT.SetVisibleAnimated(300,False)
		ToastMessageShow("Exit next item.", False)
	End If
End Sub

Sub TTS1_Ready (Success As Boolean)
	If Success Then
		'enable all views
'		Cell_Click 'play first sentence
	Else
'		Msgbox("Error initializing TTS engine.", "")
	End If
End Sub

Sub BUTTON_EDIT_Click
	If BUTTON_EDIT.Text = " Edit" Then
		Dim my_unit As String = CMB_UNIT.cmbBox.SelectedItem
		BUTTON_EDIT.Text = " Cancel Edit"
		BUTTON_EDIT.TextColor = Colors.Red
		Sleep(0)
		BUTTON_DELETE.Text = " Update"
		BUTTON_DELETE.TextColor = Colors.Blue
		cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
		For qrow = 0 To cursor3.RowCount - 1
			cursor3.Position = qrow
			LABEL_LOAD_VARIANT.Text = cursor3.GetString("product_variant")
			LABEL_LOAD_DESCRIPTION.Text = cursor3.GetString("product_desc")
			
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
			
			Sleep(10)
			CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf(my_unit)
		Next
		CMB_UNIT.cmbBox.Enabled = True
		EDITTEXT_QUANTITY.Enabled = True
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
	Else
		BUTTON_EDIT.Text = " Edit"
		BUTTON_EDIT.TextColor = Colors.Blue
		Sleep(0)
		BUTTON_DELETE.Text = " Delete"
		BUTTON_DELETE.TextColor = Colors.Red
		CMB_UNIT.cmbBox.Clear
		CMB_UNIT.cmbBox.Add(orig_unit)
		CMB_UNIT.cmbBox.Enabled = False
		EDITTEXT_QUANTITY.Text = orig_quantity
		EDITTEXT_QUANTITY.Enabled = False
		ToastMessageShow("Editing Canceled",False)
	End If
End Sub

Sub CMB_UNIT_SelectedIndexChanged (Index As Int)
	EDITTEXT_QUANTITY.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
	Sleep(0)
	EDITTEXT_QUANTITY.SelectAll
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
	StartActivity(MONTLHY_INVENTORY2_MODULE)
	SetAnimation("left_to_center", "center_to_right")
	MONTLHY_INVENTORY2_MODULE.cmb_trigger = 0
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
		Msgbox2Async("Are you sure you want to UPLOAD THIS TRANSACTION?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Delete_Inventory_Ref
		Else
		
		End If
	End If
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
Sub Delete_Inventory_Ref
	error_trigger = 0
	PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)
	PANEL_BG_MSGBOX.BringToFront
	Sleep(0)
	Dim cmd As DBCommand = CreateCommand("delete_inventory_ref", Array(MONTHLY_INVENTORY_MODULE.transaction_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Insert_Inventory_Ref
	Else
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		error_trigger = 1
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Delete_Inventory_Ref
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub Insert_Inventory_Ref
	Dim Date As String = DateTime.Date(DateTime.Now)
	Dim Time As String = DateTime.Time(DateTime.Now)
	cursor1 = connection.ExecQuery("SELECT * FROM inventory_ref_table WHERE transaction_id = '"&MONTHLY_INVENTORY_MODULE.transaction_id&"'")
	If cursor1.RowCount > 0 Then
		For i1 = 0 To cursor1.RowCount - 1
			cursor1.Position = i1
			Dim cmd As DBCommand = CreateCommand("insert_inventory_ref", Array(cursor1.GetString("transaction_id"),cursor1.GetString("principal_name"),cursor1.GetString("principal_id"),cursor1.GetString("inventory_date"), _
			cursor1.GetString("warehouse"),cursor1.GetString("area"),cursor1.GetString("user_info"),cursor1.GetString("tab_id"),cursor1.GetString("date_registered"),cursor1.GetString("time_registered"), _
			cursor1.GetString("edit_count"),cursor1.GetString("date_updated"),cursor1.GetString("time_updated"),cursor1.GetString("transaction_status"),Date & " " & Time, LOGIN_MODULE.username))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Uploading transaction : " & cursor1.GetString("principal_name") & " " & cursor1.GetString("warehouse") & " " & cursor1.GetString("inventory_date") & "..."
			Else
				error_trigger = 1
				Log("ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
			End If
			js.Release
		Next
		If error_trigger = 0 Then
		Delete_Inventory_Ref_Trail
		Else
			Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				Delete_Inventory_Ref
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Updating Failed", False)
			End If
		End If
	End If

End Sub
Sub Delete_Inventory_Ref_Trail
	Dim cmd As DBCommand = CreateCommand("delete_inventory_ref_trail", Array(MONTHLY_INVENTORY_MODULE.transaction_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Insert_Inventory_Ref_Trail
	Else
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		error_trigger = 1
		If Result = DialogResponse.POSITIVE Then
			Delete_Inventory_Ref
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub Insert_Inventory_Ref_Trail
	cursor2 = connection.ExecQuery("SELECT * FROM inventory_ref_table_trail WHERE transaction_id = '"&MONTHLY_INVENTORY_MODULE.transaction_id&"'")
	If cursor2.RowCount > 0 Then
		For i2 = 0 To cursor2.RowCount - 1
			cursor2.Position = i2
			Dim cmd As DBCommand = CreateCommand("insert_inventory_ref_trail", Array(cursor2.GetString("transaction_id"),cursor2.GetString("principal_name"),cursor2.GetString("principal_id"),cursor2.GetString("inventory_date"), _
			cursor2.GetString("warehouse"),cursor2.GetString("area"),cursor2.GetString("user_info"),cursor2.GetString("tab_id"),cursor2.GetString("date_registered"),cursor2.GetString("time_registered"), _
			cursor2.GetString("transaction_status"),cursor2.GetString("edit_count"),cursor2.GetString("edit_type"),cursor2.GetString("edit_by")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Data ready for upload... "
			Else
				Log("ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				error_trigger = 1
			End If
			js.Release
		Next
	End If
	Sleep(0)
	If error_trigger = 0 Then
		Delete_Inventory_Disc
	Else
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Delete_Inventory_Ref
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub Delete_Inventory_Disc
	Dim cmd As DBCommand = CreateCommand("delete_inventory_disc", Array(MONTHLY_INVENTORY_MODULE.transaction_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Insert_Inventory_Disc
	Else
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		error_trigger = 1
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Delete_Inventory_Ref
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub Insert_Inventory_Disc
	cursor3 = connection.ExecQuery("SELECT * FROM inventory_disc_table WHERE transaction_id = '"&MONTHLY_INVENTORY_MODULE.transaction_id&"'")
	If cursor3.RowCount > 0 Then
		For i3 = 0 To cursor3.RowCount - 1
			cursor3.Position = i3
			Dim cmd As DBCommand = CreateCommand("insert_inventory_disc", Array(cursor3.GetString("transaction_id"),cursor3.GetString("transaction_number"),cursor3.GetString("principal_id"),cursor3.GetString("principal_name"), _
			cursor3.GetString("product_id"),cursor3.GetString("product_variant"),cursor3.GetString("product_description"),cursor3.GetString("unit"),cursor3.GetString("quantity"),cursor3.GetString("total_pieces"), _
			cursor3.GetString("position"),cursor3.GetString("input_type"),cursor3.GetString("reason"),cursor3.GetString("scan_code"),cursor3.GetString("inventory_status"),cursor3.GetString("count_by"), _
			cursor3.GetString("count_person1"),cursor3.GetString("count_person2"),cursor3.GetString("recount_by"),cursor3.GetString("recount_person2"),cursor3.GetString("recount_person1"), _
			cursor3.GetString("date_registered"),cursor3.GetString("time_registered"),cursor3.GetString("recount_date"),cursor3.GetString("recount_time"),cursor3.GetString("edit_count"),cursor3.GetString("tab_id")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Uploading Data: " & cursor3.GetString("product_description")
			Else
				Log("ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				error_trigger = 1
			End If
			js.Release
		Next
		If error_trigger = 0 Then
			Delete_Inventory_Disc_Trail
		Else
			Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				Delete_Inventory_Ref
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Updating Failed", False)
			End If
		End If
	End If
End Sub
Sub Delete_Inventory_Disc_Trail
	Dim cmd As DBCommand = CreateCommand("delete_inventory_disc_trail", Array(MONTHLY_INVENTORY_MODULE.transaction_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Insert_Inventory_Disc_Trail
	Else
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
	End If
	js.Release
End Sub
Sub Insert_Inventory_Disc_Trail
	cursor4 = connection.ExecQuery("SELECT * FROM inventory_disc_table_trail WHERE transaction_id = '"&MONTHLY_INVENTORY_MODULE.transaction_id&"'")
	If cursor4.RowCount > 0 Then
		For i4 = 0 To cursor4.RowCount - 1
			cursor4.Position = i4
			Dim cmd As DBCommand = CreateCommand("insert_inventory_disc_trail", Array(cursor4.GetString("transaction_id"),cursor4.GetString("transaction_number"),cursor4.GetString("principal_id"),cursor4.GetString("principal_name"), _
			cursor4.GetString("product_id"),cursor4.GetString("product_variant"),cursor4.GetString("product_description"),cursor4.GetString("unit"),cursor4.GetString("quantity"),cursor4.GetString("total_pieces"), _
			cursor4.GetString("position"),cursor4.GetString("input_type"),cursor4.GetString("reason"),cursor4.GetString("scan_code"),cursor4.GetString("inventory_status"),cursor4.GetString("count_by"), _
			cursor4.GetString("count_person1"),cursor4.GetString("count_person2"),cursor4.GetString("recount_by"),cursor4.GetString("recount_person1"),cursor4.GetString("recount_person2"), _
			cursor4.GetString("date_registered"),cursor4.GetString("time_registered"),cursor4.GetString("recount_date"),cursor4.GetString("recount_time"),cursor4.GetString("edit_count"),cursor4.GetString("tab_id"), _
			cursor4.GetString("edit_by"),cursor4.GetString("edit_type"),cursor4.GetString("edit_date"),cursor4.GetString("edit_time")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Data uploading complete..."
			Else
				Log("ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				error_trigger = 1
			End If
			js.Release
		Next
	End If
	If error_trigger = 0 Then
	Sleep(0)
	LABEL_MSGBOX2.Text = "Uploading Successful..."
	Sleep(1000)
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
	Sleep(0)
	Dim query As String = "UPDATE inventory_ref_table SET transaction_status = 'UPLOADED' WHERE transaction_id = ?"
	connection.ExecNonQuery2(query,Array As String(MONTHLY_INVENTORY_MODULE.transaction_id))
	Sleep(0)
	Activity.Finish
	StartActivity(MONTHLY_INVENTORY_MODULE)
	SetAnimation("zoom_enter", "zoom_exit")
	Else
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Delete_Inventory_Ref
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
#End Region

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
	PANEL_BG_CALCU.SetVisibleAnimated(300,False)
	
	If EDITTEXT_QUANTITY.Enabled = False Then
	
	Else
		EDITTEXT_QUANTITY.Text = LABEL_LOAD_ANSWER.Text
	End If
	
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
