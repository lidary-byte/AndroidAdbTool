package bean

data class MainNavBean(val svgName: String, val labelText: String)

fun createMainNavData() = mutableListOf(
    MainNavBean("ic_quick_future.svg","快捷功能"),
    MainNavBean("ic_folder.svg","文件管理"),
    MainNavBean("ic_log.svg","Logcat"),
    MainNavBean("ic_settings.svg","设置"),
)