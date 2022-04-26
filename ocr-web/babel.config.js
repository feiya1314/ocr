module.exports = {
    presets: [
        '@vue/app',
        '@vue/cli-plugin-babel/preset',
    ],
    // 动态导入
    plugins: [
        [ // 按需导入elementui
            "component",
            {
                "libraryName": "element-ui",
                "styleLibraryName": "theme-chalk"
            }
        ], '@babel/plugin-syntax-dynamic-import' //懒加载插件
    ]
}