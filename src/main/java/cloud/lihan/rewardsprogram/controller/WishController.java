package cloud.lihan.rewardsprogram.controller;

import cloud.lihan.rewardsprogram.common.constants.IntegerConstant;
import cloud.lihan.rewardsprogram.common.controller.BaseController;
import cloud.lihan.rewardsprogram.common.core.Base;
import cloud.lihan.rewardsprogram.dto.WishDTO;
import cloud.lihan.rewardsprogram.service.inner.WishService;
import cloud.lihan.rewardsprogram.vo.WishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 愿望相关控制器
 *
 * @author hanyun.li
 * @createTime 2022/06/29 15:16:00
 */
@Controller("wishController")
@RequestMapping("/wish")
public class WishController extends BaseController {

    @Autowired
    private WishService wishService;

    @PutMapping()
    @ResponseBody
    public Base savaWish(@RequestBody WishVO wishVO) {
        try {
            if (Objects.isNull(wishVO)) {
                return apiErr("输入的愿望参数为空！");
            }
            wishService.saveWish(wishVO);
            return apiOk();
        } catch (IOException e) {
            return apiErr(e.getMessage());
        }
    }

    @PutMapping("/bulk")
    @ResponseBody
    public Base bulkSavaWish(@RequestBody List<WishVO> wishVOs) {
        try {
            if (CollectionUtils.isEmpty(wishVOs)) {
                return apiErr("输入的愿望参数集合为空！");
            }
            wishService.bulkSaveWish(wishVOs);
            return apiOk();
        } catch (IOException e) {
            return apiErr(e.getMessage());
        }
    }

    @DeleteMapping()
    @ResponseBody
    public Base deleteWishById(@RequestParam("id") String id) {
        try {
            wishService.deleteWishDocumentById(id);
            return apiOk();
        } catch (IOException e) {
            return apiErr(e.getMessage());
        }
    }

    @PostMapping("/fulfillment")
    @ResponseBody
    public Base fulfillmentWishById(@RequestParam("id") String id) {
        try {
            wishService.fulfillmentWishById(id);
            return apiOk();
        } catch (IOException e) {
            return apiErr(e.getMessage());
        }
    }

    @GetMapping()
    @ResponseBody
    public Base getSingeRandomWish() {
        try {
            return apiOk(wishService.getSingeRandomWish());
        } catch (IOException e) {
            return apiErr(e.getMessage());
        }
    }

    @GetMapping("/all")
    @ResponseBody
    public Base getAllWish() {
        try {
            return apiOk(wishService.getMultipleRandomWish(IntegerConstant.ONE + IntegerConstant.NINE));
        } catch (IOException e) {
            return apiErr(e.getMessage());
        }
    }

    @GetMapping("/single")
    @ResponseBody
    public Base getWishById(@RequestParam("id") String id) {
        try {
            WishDTO wishByWishDocumentId = wishService.getWishByWishDocumentId(id);
            return apiOk(wishByWishDocumentId);
        } catch (IOException e) {
            return apiErr(e.getMessage());
        }
    }

}
