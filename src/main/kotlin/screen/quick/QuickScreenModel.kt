package screen.quick

import bean.QuickBean
import bean.QuickChildBean
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuickScreenModel : ScreenModel {

    init {
        buildData()
    }

    private val _quickData = MutableStateFlow<List<QuickBean>>(emptyList())
    val quickData
        get() = _quickData.asStateFlow()

   private fun buildData() {
        screenModelScope.launch(Dispatchers.IO) {
            val quickList = mutableListOf<QuickBean>()

            quickList.add(
                QuickBean(
                    "常用功能", mutableListOf(
                        QuickChildBean("安装应用", 0xe693),
                        QuickChildBean("输入文本", 0xe816),
                        QuickChildBean("截图保存电脑", 0xe931),
                        QuickChildBean("查看当前Activity", 0xe607)
                    )
                )
            )

            quickList.add(
                QuickBean(
                    "应用相关", mutableListOf(
                        QuickChildBean("卸载应用", 0xe740),
                        QuickChildBean("启动应用", 0xe6af),
                        QuickChildBean("停止运行", 0xe875),
                        QuickChildBean("重启应用", 0xe7d6),
                        QuickChildBean("清除数据", 0xe613),
                        QuickChildBean("清除数据并重启应用", 0xe816),
                        QuickChildBean("重置权限", 0xe647),
                        QuickChildBean("重置权限并重启应用", 0xe628),
                        QuickChildBean("授权所有权限", 0xe66c),
                        QuickChildBean("查看应用安装路径", 0xe76d),
                        QuickChildBean("保存Apk到电脑", 0xe60e),
                    )
                )
            )

            quickList.add(
                QuickBean(
                    "系统相关", mutableListOf(
                        QuickChildBean("开始录屏", 0xe695),
                        QuickChildBean("结束录屏保存到电脑", 0xe71d),
                        QuickChildBean("查看AndroidId", 0xe881),
                        QuickChildBean("查看系统版本", 0xe617),
                        QuickChildBean("查看IP地址", 0xe632),
                        QuickChildBean("查看Mac地址", 0xe65d),
                        QuickChildBean("重启手机", 0xe6b2),
                        QuickChildBean("查看系统属性", 0xe61e)
                    )
                )
            )


            quickList.add(
                QuickBean(
                    "按键相关", mutableListOf(
                        QuickChildBean("Home键", 0xe68e),
                        QuickChildBean("Back键", 0xe616),
                        QuickChildBean("Menu键", 0xe605),
                        QuickChildBean("Power键", 0xe615),
                        QuickChildBean("增加音量", 0xe76e),
                        QuickChildBean("降低音量", 0xe771),
                        QuickChildBean("静音", 0xe612),
                        QuickChildBean("切换应用", 0xe658),
                        QuickChildBean("遥控器", 0xe796)
                    )
                )
            )

            quickList.add(
                QuickBean(
                    "屏幕输入", mutableListOf(
                        QuickChildBean("向上滑动", 0xe795),
                        QuickChildBean("向下滑动", 0xe603),
                        QuickChildBean("向左滑动", 0xe60a),
                        QuickChildBean("向右滑动", 0xe6ca),
                        QuickChildBean("屏幕点击", 0xe697)
                    )
                )
            )
            _quickData.value = quickList
        }
    }


}