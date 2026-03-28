package com.lostlink.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lostlink.app.ui.navigation.Screen
import com.lostlink.app.ui.screens.*
import com.lostlink.app.ui.screens.auth.LoginScreen
import com.lostlink.app.ui.screens.auth.RegisterScreen
import com.lostlink.app.ui.theme.LostLinkTheme
import com.lostlink.app.ui.viewmodel.AuthViewModel
import com.lostlink.app.ui.viewmodel.AuthState
import com.lostlink.app.ui.viewmodel.AIViewModel
import com.lostlink.app.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LostLinkApp()
        }
    }
}

@Composable
fun LostLinkApp() {
    val authViewModel: AuthViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    
    val authState by authViewModel.authState.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val error by authViewModel.error.collectAsState()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    
    LostLinkTheme(darkTheme = isDarkMode) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val isLoggedIn = authState is AuthState.LoggedInWithUid

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val showDrawer = isLoggedIn && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route

        if (showDrawer) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    LostLinkDrawerContent(
                        navController = navController,
                        currentRoute = currentRoute,
                        onCloseDrawer = { scope.launch { drawerState.close() } },
                        onLogout = {
                            authViewModel.signOut()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            ) {
                Scaffold { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        LostLinkNavHost(
                            navController = navController,
                            isLoggedIn = isLoggedIn,
                            authViewModel = authViewModel,
                            isLoading = isLoading,
                            errorMessage = error,
                            onOpenDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                }
            }
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LostLinkNavHost(
                    navController = navController,
                    isLoggedIn = isLoggedIn,
                    authViewModel = authViewModel,
                    isLoading = isLoading,
                    errorMessage = error,
                    onOpenDrawer = { }
                )
            }
        }
    }
}

@Composable
fun LostLinkDrawerContent(
    navController: NavHostController,
    currentRoute: String?,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(16.dp))
        Text(
            "LostLink",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Divider()
        
        DrawerItem("Dashboard", Screen.Dashboard.route, Icons.Default.Dashboard, currentRoute) {
            navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Dashboard.route) { inclusive = true } }
            onCloseDrawer()
        }
        DrawerItem("Report Item", Screen.Report.route, Icons.Default.AddCircle, currentRoute) {
            navController.navigate(Screen.Report.route)
            onCloseDrawer()
        }
        DrawerItem("Messages", Screen.Messaging.route, Icons.AutoMirrored.Filled.Message, currentRoute) {
            navController.navigate(Screen.Messaging.route)
            onCloseDrawer()
        }
        DrawerItem("AI Support", Screen.AISupport.route, Icons.Default.SupportAgent, currentRoute) {
            navController.navigate(Screen.AISupport.route)
            onCloseDrawer()
        }
        DrawerItem("AI Assistant", Screen.AIChat.route, Icons.Default.AutoAwesome, currentRoute) {
            navController.navigate(Screen.AIChat.route)
            onCloseDrawer()
        }
        DrawerItem("Campus Map", Screen.CampusMap.route, Icons.Default.Map, currentRoute) {
            navController.navigate(Screen.CampusMap.route)
            onCloseDrawer()
        }
        DrawerItem("Saved Items", Screen.SavedItems.route, Icons.Default.Bookmark, currentRoute) {
            navController.navigate(Screen.SavedItems.route)
            onCloseDrawer()
        }
        
        Spacer(Modifier.weight(1f))
        Divider()
        
        DrawerItem("Labs (New Features)", Screen.Labs.route, Icons.Default.Science, currentRoute) {
            navController.navigate(Screen.Labs.route)
            onCloseDrawer()
        }
        DrawerItem("Settings", Screen.Settings.route, Icons.Default.Settings, currentRoute) {
            navController.navigate(Screen.Settings.route)
            onCloseDrawer()
        }
        DrawerItem("Policies", Screen.Policies.route, Icons.Default.Policy, currentRoute) {
            navController.navigate(Screen.Policies.route)
            onCloseDrawer()
        }
        
        NavigationDrawerItem(
            label = { Text("Logout", color = Color.Red) },
            selected = false,
            onClick = onLogout,
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.Red) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun DrawerItem(
    label: String,
    route: String,
    icon: ImageVector,
    currentRoute: String?,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = currentRoute == route,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
private fun LostLinkNavHost(
    navController: NavHostController,
    isLoggedIn: Boolean,
    authViewModel: AuthViewModel,
    isLoading: Boolean,
    errorMessage: String?,
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Dashboard.route else Screen.Login.route,
        modifier = Modifier.fillMaxSize(),
        enterTransition = {
            fadeIn(animationSpec = tween(400)) + slideInHorizontally(initialOffsetX = { 300 })
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { -300 })
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(400)) + slideInHorizontally(initialOffsetX = { -300 })
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { 300 })
        }
    ) {
        // ==================== Authentication ====================
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.signIn(
                        email = email,
                        password = password,
                        onSuccess = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                },
                onAdminLoginClick = {
                    authViewModel.signInAsAdmin(
                        onSuccess = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                },
                onSignUpClick = { 
                    authViewModel.clearError()
                    navController.navigate(Screen.Register.route) 
                },
                onForgotPasswordClick = { },
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = { name, email, phone, id, password ->
                    authViewModel.signUp(
                        name = name,
                        email = email,
                        phone = phone,
                        idNumber = id,
                        password = password,
                        onSuccess = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                },
                onLoginClick = {
                    authViewModel.clearError()
                    navController.popBackStack()
                },
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        }
        
        // ==================== Main Screens ====================
        composable(Screen.Home.route) {
            HomeScreen(
                onBrowseClick = { navController.navigate(Screen.Dashboard.route) },
                onReportClick = { navController.navigate(Screen.Report.route) },
                onLoginClick = { navController.navigate(Screen.Dashboard.route) }
            )
        }
        
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onReportClick = { navController.navigate(Screen.Report.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onMenuClick = onOpenDrawer
            )
        }
        
        composable(Screen.Report.route) {
            ReportScreen(
                onBackClick = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }
        
        // ==================== Item Details ====================
        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            ItemDetailScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                onContactClick = { 
                    navController.navigate(Screen.ChatDetail.createRoute("direct", itemId))
                },
                onFindMatchesClick = { id ->
                    navController.navigate(Screen.AIMatches.createRoute(id))
                }
            )
        }
        
        // ==================== Profile ====================
        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogoutClick = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onPoliciesClick = { navController.navigate(Screen.Policies.route) },
                onBackClick = { navController.popBackStack() },
                onMenuClick = onOpenDrawer
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onPoliciesClick = { navController.navigate(Screen.Policies.route) },
                onLabsClick = { navController.navigate(Screen.Labs.route) }
            )
        }

        // ==================== Features ====================
        composable(Screen.CampusMap.route) {
            CampusMapScreen(
                onItemLocationSelected = { location ->
                    navController.navigate(Screen.Dashboard.route)
                },
                onBackClick = { navController.popBackStack() },
                onMenuClick = onOpenDrawer
            )
        }
        
        composable(Screen.SavedItems.route) {
            SavedItemsScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onBackClick = { navController.popBackStack() },
                onMenuClick = onOpenDrawer
            )
        }

        composable(Screen.AIChat.route) {
            AIChatScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onMenuClick = onOpenDrawer
            )
        }

        composable(Screen.AISupport.route) {
            AISupportScreen(
                onMenuClick = onOpenDrawer
            )
        }
        
        composable(Screen.Messaging.route) {
            MessagingScreen(
                onConversationClick = { convId ->
                    navController.navigate(Screen.ChatDetail.createRoute(convId))
                },
                onBackClick = { navController.popBackStack() },
                onMenuClick = onOpenDrawer
            )
        }
        
        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
                navArgument("itemId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val convId = backStackEntry.arguments?.getString("conversationId") ?: ""
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            ChatDetailScreen(
                conversationId = convId,
                itemId = itemId,
                personName = "Support / Member",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AIMatches.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            AIMatchScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                onMatchClick = { matchId ->
                    navController.navigate(Screen.ItemDetail.createRoute(matchId))
                }
            )
        }

        composable(Screen.Policies.route) {
            PoliciesScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Labs.route) {
            LabsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
