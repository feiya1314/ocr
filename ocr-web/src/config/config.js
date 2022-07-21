// env 中的配置必须要 VUE_APP_ 开头

let homeUrl = process.env.VUE_APP_OCR_BASE_DOMAIN_URL;
let ocrServiceUrl = process.env.VUE_APP_OCR_SERVICE_API;
console.log("ocrServiceUrl : " + ocrServiceUrl)

export default {
    homeUrl: homeUrl,
    ocrUrl: ocrServiceUrl,
}