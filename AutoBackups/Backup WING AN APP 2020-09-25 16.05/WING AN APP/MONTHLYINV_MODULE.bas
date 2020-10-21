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

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Dim ABHelper As ACActionBar
	Private ACTB_MONTHLY_INV As ACToolBarDark
	Private SV_MONTHLY_INV As ScrollView
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
	
'	ACTB_MONTHLY_INV.ShowUpIndicator = True
	
	ABHelper.Initialize
	ABHelper.ShowUpIndicator = True
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Activity_CreateMenu(Menu As ACMenu)
'	Menu.Clear
'	Menu.Add(1, 1, "Overflow1", Null)
'	Menu.Add(2, 2, "Overflow2", Null)
'	Menu.Add(3, 3, "Overflow3", Null)
'	Dim item As ACMenuItem = ACToolBarDark1.Menu.Add2(0, 0, "cart", Null)
'	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	''	UpdateIcon("cart", AddBadgeToIcon(cartBitmap, badge))
	
'	sv.Initialize2("Search", sv.THEME_LIGHT)
'	sv.IconifiedByDefault = True
	'
'	'Clear the menu
'	Menu.Clear
'	
'	'Add a menu item and assign the SearchView to it
'	si = Menu.Add2(2, 1, "Search", Null)
'	si.SearchView = sv
End Sub
