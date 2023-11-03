package bean

/**
 * 快捷功能
 */
data class QuickBean(val title: String,val child:List<QuickChildBean>)

data class QuickChildBean(val title: String, val icon: Int)

