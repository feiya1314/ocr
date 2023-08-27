package cn.easyocr.uni.auth.wx;

import cn.easyocr.common.helper.HttpClientHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author : feiya
 * @date : 2023/8/10
 * @description :
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WxServiceTest {
    private WxService wxService;
    private static String token =
            "71_bqyNLrClf_zdSlj2Ci6kyDFr70NketBSkwSgVDzOhbGuNLDUvnVSXY28EFbu3zIo5wmhCNkVj6MBLgxgHRyAUGBDehbxhtN1aOPYkFR0k25n9EXC" +
            "-TTvzH3N3BgTWLhAHANQF";

    @BeforeEach
    public void init() throws Exception {
        wxService = new WxService(new HttpClientHelper());
    }

    @Test
    @Order(1)
    public void testGetAccessToken() throws Exception {
        GetTokenReq req = new GetTokenReq();
        req.setAppid("wxd871a180e4e7fa06");
        req.setSecret("1adab0d304582e59f1ddb68b8a2b0f56");

        GetTokenResp resp = wxService.getAccessToken(req);
        Assertions.assertTrue(resp.success() && resp.getExpires_in() > 0);
        token = resp.getAccess_token();
    }

    @Test
    @Order(2)
    public void testQr() throws Exception {
        GetQrReq req = new GetQrReq();
//        req.setAccess_token
//        ("71_8XXyCfGDatO0ugm-eYeGc4MWpsYrv
//        -T4uAxeXdv_rZcM7eXtD1ApjZM3WZalEykxQO76dhGVeabvvDhPu7AKT6_6hy_WqLKJtqpsdYBalnkrzpTdAnLughfBmWoRIVjABAXOB");
        req.setAccess_token(token);
        req.setScene("ac=sdhaskjahkjfgsa");

        GetQrResp resp = wxService.getUnlimitedQRCode(req);
        Assertions.assertFalse(resp.success());
    }

    @Test
    @Order(3)
    public void testCode2Session() throws Exception {
        GetSessionReq req = new GetSessionReq();
        req.setAppid("wxd871a180e4e7fa06");
        req.setSecret("1adab0d304582e59f1ddb68b8a2b0f56");
        req.setJs_code("asdhasuhfu");

        GetSessionResp resp = wxService.code2Session(req);
        Assertions.assertFalse(resp.success());
    }
}
