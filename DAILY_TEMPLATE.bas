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

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight


	Private XSelections As B4XTableSelections
	Private NameColumn(3) As B4XTableColumn

	Private Dialog As B4XDialog
	Private Base As B4XView
	Private SearchTemplate As B4XSearchTemplate
	Private SearchTemplate2 As B4XSearchTemplate
	
	Private cvs As B4XCanvas
	Private xui As XUI
	Private LABEL_LOAD_GROUP As Label
	Private PANEL_TEMPLATE As Panel
	Private PRODUCT_TEMPLATE_TABLE As B4XTable
	Private LABEL_LOAD_MONTH As Label
	Private LABEL_LOAD_YEAR As Label
	Private BUTTON_ADD As Button
	Private PANEL_BG_COPY As Panel
	Private LABEL_LOAD_COPY_GROUP As Label
	Private CMB_COPY_YEAR As B4XComboBox
	Private CMB_COPY_MONTH As B4XComboBox
	Private PANEL_COPY As Panel
	Private BUTTON_SAVE As Button
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("daily_create")
	
	
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
	
	GET_TEMPLATE
	Sleep(0)
	LOAD_TABLE_HEADER
	Sleep(0)
	LOAD_PRODUCT
	Sleep(0)
	LOAD_INVENTORY_TABLE
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

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
	SetAnimation("left_to_center", "center_to_right")
End Sub

Sub OpenSpinner(se As Spinner)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
End Sub

Sub OpenButton(se As Button)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
End Sub

Sub GET_TEMPLATE
	cursor1 = connection.ExecQuery("SELECT * FROM daily_template_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"'")
	If cursor1.RowCount > 0 Then
		For i = 0 To cursor1.RowCount - 1
			Sleep(0)
			cursor1.Position = i
			LABEL_LOAD_GROUP.Text = cursor1.GetString("group_name")
			LABEL_LOAD_YEAR.Text = cursor1.GetString("year")
			LABEL_LOAD_MONTH.Text = cursor1.GetString("month")
		Next
	Else
		
	End If
End Sub

Sub LOAD_TABLE_HEADER
	NameColumn(0)=PRODUCT_TEMPLATE_TABLE.AddColumn("Principal", PRODUCT_TEMPLATE_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(1)=PRODUCT_TEMPLATE_TABLE.AddColumn("Product Variant", PRODUCT_TEMPLATE_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(2)=PRODUCT_TEMPLATE_TABLE.AddColumn("Product Description", PRODUCT_TEMPLATE_TABLE.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_INVENTORY_TABLE
	Sleep(0)
	Dim Data As List
	Data.Initialize
	Dim rs As ResultSet = connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"&DAILY_INVENTORY_MODULE.group_id&"' ORDER BY principal_name, product_variant, product_description ASC")
	Do While rs.NextRow
		Dim row(3) As Object
		row(0) = rs.GetString("principal_name")
		row(1) = rs.GetString("product_variant")
		row(2) = rs.GetString("product_description")
		Data.Add(row)
	Loop
	rs.Close
	PRODUCT_TEMPLATE_TABLE.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(PRODUCT_TEMPLATE_TABLE)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
End Sub
Sub PRODUCT_TEMPLATE_TABLE_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0), NameColumn(1), NameColumn(2))
		Dim MaxWidth As Int
		For i = 0 To PRODUCT_TEMPLATE_TABLE.VisibleRowIds.Size
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
		PRODUCT_TEMPLATE_TABLE.Refresh
		XSelections.Clear
		XSelections.Refresh
	End If
End Sub
Sub PRODUCT_TEMPLATE_TABLE_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Log(ColumnId & RowId)
End Sub
Sub PRODUCT_TEMPLATE_TABLE_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Log(ColumnId & RowId)
	
	Dim RowData As Map = PRODUCT_TEMPLATE_TABLE.GetRow(RowId)
	
	Msgbox2Async("Do you want to delete this product of this template?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Dim query As String = "DELETE from daily_inventory_ref_table WHERE product_description = ?"
		connection.ExecNonQuery2(query,Array As String(RowData.Get("Product Description")))
		ProgressDialogShow2("Deleting...", False)
		Sleep(1500)
		ProgressDialogHide
		Sleep(0)
		ToastMessageShow("Product Deleted", False)
		Sleep(0)
		LOAD_INVENTORY_TABLE
	Else
		
	End If
End Sub

Sub LOAD_PRODUCT
	SearchTemplate2.CustomListView1.Clear
	Dialog.Title = "Find Product"
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE flag_deleted = '0' ORDER BY product_desc ASC")
	For i = 0 To cursor2.RowCount - 1
		Sleep(0)
		cursor2.Position = i
		Items.Add(cursor2.GetString("product_desc"))
	Next
	SearchTemplate2.SetItems(Items)
End Sub
Sub BUTTON_ADD_Click
	ProgressDialogShow2("Loading...", False)
	Sleep(500)
	ProgressDialogHide
	Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate2, "", "", "CANCEL")
	Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
	Wait For (rs) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
#Region Find Product
		cursor4 = connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE product_description ='"&SearchTemplate2.SelectedItem&"' and group_id = '"&DAILY_INVENTORY_MODULE.group_id&"'")
		If cursor4.RowCount > 0 Then
			Msgbox2Async("The product you adding is already exisiting in the template. Cannot proceed.", "Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)	
			OpenButton(BUTTON_ADD)
		Else
		Dim Data As List
		Data.Initialize
		cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&SearchTemplate2.SelectedItem&"'")
		For qrow = 0 To cursor3.RowCount - 1
			cursor3.Position = qrow
			cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & cursor3.GetString("principal_id") & "'")
			If cursor6.RowCount > 0 Then
				For row = 0 To cursor6.RowCount - 1
					cursor6.Position = row
					Dim query As String = "INSERT INTO daily_inventory_ref_table VALUES (?,?,?,?,?,?,?,?,?,?)"
					connection.ExecNonQuery2(query,Array As String(DAILY_INVENTORY_MODULE.group_id,cursor3.Getstring("principal_id"),cursor6.Getstring("principal_name"), _
					cursor3.Getstring("product_id"),cursor3.Getstring("product_variant"),cursor3.Getstring("product_desc"), _
					DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),LOGIN_MODULE.tab_id,LOGIN_MODULE.username))
					Sleep(0)
					ToastMessageShow("Product added", False)
					Sleep(0)
					LOAD_INVENTORY_TABLE
					Msgbox2Async("The product is added to your template. Do you want to add a product again?", "Notice", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					Wait For Msgbox_Result (Result As Int)
					If Result = DialogResponse.POSITIVE Then
						OpenButton(BUTTON_ADD)
					Else
					End If
				Next
				
			End If
		Next
	End If
#End Region		
	End If
End Sub
Sub BUTTON_DELETE_Click
	Msgbox2Async("Are you sure you want to delete this template? All data in this template will be lost.", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Dim insert_query As String = "INSERT INTO daily_template_table_trail SELECT *,? as 'edit_by','DELETED' as 'edit_type',? as 'edit_date_time' from daily_template_table WHERE group_id = ?"
		connection.ExecNonQuery2(insert_query,Array As String(LOGIN_MODULE.username, DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now), DAILY_INVENTORY_MODULE.group_id))
		Sleep(0)
		Dim query As String = "DELETE FROM daily_template_table WHERE group_id = ?"
		connection.ExecNonQuery2(query,Array As String(DAILY_INVENTORY_MODULE.group_id))
		Sleep(0)
		Dim query2 As String = "DELETE FROM daily_inventory_ref_table WHERE group_id = ?"
		connection.ExecNonQuery2(query2,Array As String(DAILY_INVENTORY_MODULE.group_id))
		ToastMessageShow("Deleted Successfully", True)
		Sleep(0)
		Msgbox2Async("Template deleted successfully.", "Notice", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Activity.Finish
			StartActivity(DAILY_INVENTORY_MODULE)
			SetAnimation("left_to_center", "center_to_right")
			DAILY_INVENTORY_MODULE.clear_trigger = 1
		End If
	End If
End Sub
Sub BUTTON_COPY_Click
	PANEL_BG_COPY.BringToFront
	PANEL_BG_COPY.SetVisibleAnimated(300, True)
	
	LABEL_LOAD_COPY_GROUP.Text = LABEL_LOAD_GROUP.Text
	
	LOAD_COPY_YEAR
	Sleep(10)
	CMB_COPY_YEAR.SelectedIndex = -1
	Sleep(10)
	OpenSpinner(CMB_COPY_YEAR.cmbBox)
End Sub
Sub LOAD_COPY_YEAR
	CMB_COPY_YEAR.cmbBox.Clear
	CMB_COPY_MONTH.cmbBox.Clear
	cursor2 = connection.ExecQuery("SELECT year FROM daily_template_table WHERE group_name = '"&LABEL_LOAD_GROUP.Text&"' GROUP BY year ORDER BY year ASC")
	If cursor2.RowCount > 0 Then
		For i = 0 To cursor2.RowCount - 1
			Sleep(0)
			cursor2.Position = i
			CMB_COPY_YEAR.cmbBox.Add(cursor2.GetString("year"))
		Next
	Else

	End If
End Sub
Sub LOAD_COPY_MONTH
	CMB_COPY_MONTH.cmbBox.Clear
	cursor3 = connection.ExecQuery("SELECT group_id, month FROM daily_template_table WHERE group_name = '"&LABEL_LOAD_COPY_GROUP.Text&"' and year = '"&CMB_COPY_YEAR.cmbBox.SelectedItem&"' and month <> '"&LABEL_LOAD_MONTH.Text&"' ORDER BY group_id ASC")
	If cursor3.RowCount > 0 Then
		For i = 0 To cursor3.RowCount - 1
			Sleep(0)
			cursor3.Position = i
			CMB_COPY_MONTH.cmbBox.Add(cursor3.GetString("month"))
		Next
	Else

	End If
End Sub
Sub BUTTON_CANCEL_Click
	PANEL_BG_COPY.SetVisibleAnimated(300,False)
End Sub
Sub BUTTON_SAVE_Click
	If CMB_COPY_YEAR.SelectedIndex = CMB_COPY_YEAR.cmbBox.IndexOf("") Or CMB_COPY_MONTH.SelectedIndex = CMB_COPY_MONTH.cmbBox.IndexOf("") Then
		Msgbox2Async("Please choose year and month. Cannot proceed.", "Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
	Else
		If CMB_COPY_YEAR.SelectedIndex = -1 Then CMB_COPY_YEAR.SelectedIndex = 0
		If CMB_COPY_MONTH.SelectedIndex = -1 Then CMB_COPY_MONTH.SelectedIndex = 0
		
		Msgbox2Async("Are you sure you want to update this template? This will change your data in this template.", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			ProgressDialogShow2("Copying products to template...", False)
			cursor6 = connection.ExecQuery("SELECT group_id FROM daily_template_table WHERE group_name = '"&LABEL_LOAD_COPY_GROUP.Text&"' and year = '"&CMB_COPY_YEAR.cmbBox.SelectedItem&"' and month = '"&CMB_COPY_MONTH.cmbBox.SelectedItem&"'")
			If cursor6.RowCount > 0 Then
				For i = 0 To cursor6.RowCount - 1
					Sleep(0)
					cursor6.Position = i
					cursor5 = connection.ExecQuery("SELECT * FROM daily_inventory_ref_table WHERE group_id = '"&cursor6.GetString("group_id")&"' AND product_id NOT IN (SELECT product_id FROM daily_inventory_ref_table WHERE group_id = '"& DAILY_INVENTORY_MODULE.group_id &"')")
					If cursor5.RowCount > 0 Then
						For x = 0 To cursor5.RowCount - 1
							Sleep(0)
							cursor5.Position = x
							Dim insert_query As String = "INSERT INTO daily_inventory_ref_table VALUES (?,?,?,?,?,?,?,?,?,?)"
							connection.ExecNonQuery2(insert_query,Array As String(DAILY_INVENTORY_MODULE.group_id,cursor5.GetString("principal_id") _
							,cursor5.GetString("principal_name"),cursor5.GetString("product_id"),cursor5.GetString("product_variant"),cursor5.GetString("product_description") _ 
							,DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),LOGIN_MODULE.tab_id,LOGIN_MODULE.username))
							Sleep(0)
						Next
					Else

					End If
				Next
				ProgressDialogHide
				Sleep(0)
				ToastMessageShow("Copied Successfully", False)
				Sleep(0)
				PANEL_BG_COPY.SetVisibleAnimated(300,False)
				Sleep(0)
				LOAD_INVENTORY_TABLE
			Else

			End If
			
		Else
			
		End If
	End If
End Sub
Sub CMB_COPY_YEAR_SelectedIndexChanged (Index As Int)
	LOAD_COPY_MONTH
	Sleep(10)
	CMB_COPY_MONTH.SelectedIndex = -1
	Sleep(10)
	OpenSpinner(CMB_COPY_MONTH.cmbBox)
End Sub