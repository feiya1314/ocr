// const { defineConfig } = require('@vue/cli-service')
// 代码进行压缩插件
const CompressionPlugin = require('compression-webpack-plugin');

console.log("cur node env : " + process.env.NODE_ENV)
console.log("base url : " + process.env.VUE_APP_OCR_SERVICE_API)

module.exports = {
    // 选项...
    productionSourceMap: false,
    // assetsDir 放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录。
    assetsDir: 'static',
    // 静态资源打包后的路径，默认根路径， ./ 相对路径 
    publicPath: "/",

    // 生产环境混淆代码  process.env.NODE_ENV 对应 package.json 中 vue-cli-service build ，build默认对应 production  ，development 模式用于 vue-cli-service serve
    configureWebpack: (process.env.NODE_ENV === 'production') ? {
        plugins: [
            new CompressionPlugin({
                test: /\.(js|css|svg)?$/i, // 哪些文件要压缩
                filename: '[path].gz[query]',
                // 压缩后的文件名
                algorithm: 'gzip',
                // 使用gzip压缩
                minRatio: 1,
                // 压缩率小于1才会压缩
                deleteOriginalAssets: false // 删除未压缩的文件，谨慎设置，如果希望提供非gzip的资源，可不设置或者设置为false
            })
        ]
    } : {},
    devServer: {
        // proxy: {
        //     // '/ocr': {
        //     //     // ocr这个请求代理到http://127.0.0.1:8000/ocr
        //     //     target: 'http://127.0.0.1:8000/'
        //     // }
        // }
    }
}