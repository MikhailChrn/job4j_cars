package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.dto.PostCreateDto;
import ru.job4j.cars.dto.PostFullViewDto;
import ru.job4j.cars.dto.PriceEditDto;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.entity.carcomponents.BodyType;
import ru.job4j.cars.entity.carcomponents.CarBrand;
import ru.job4j.cars.entity.carcomponents.Engine;
import ru.job4j.cars.service.RegularService;
import ru.job4j.cars.service.PostService;

import java.util.Optional;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    private final RegularService<CarBrand> carBrandService;

    private final RegularService<BodyType> bodyTypeService;

    private final RegularService<Engine> engineService;

    /**
     * Показать страницу со списком ВСЕХ объявлений
     */
    @GetMapping("/all")
    public String getAllPosts(Model model) {

        model.addAttribute("posts",
                postService.findAllDesc());
        model.addAttribute("pageTitle", "Все объявления");

        return "/posts/list";
    }

    /**
     * Показать страницу со списком всех объявлений текущего пользователя
     */
    @GetMapping("/allmy")
    public String getAllMyPosts(Model model,
                                HttpServletRequest request) {

        Optional<User> optionalUser = Optional.of(
                (User) request.getAttribute("user"));

        model.addAttribute("posts",
                postService.findAllByUserIdDesc(optionalUser.get().getId()));
        model.addAttribute("pageTitle", "Все мои объявления");

        return "/posts/list";
    }

    /**
     * Показать страницу со списком всех объявлений с прикреплёнными фото
     */
    @GetMapping("/withphoto")
    public String getAllPostsWithPhoto(Model model) {

        model.addAttribute("posts",
                postService.findWithPhotoDesc());
        model.addAttribute("pageTitle", "Все объявления с фотографией");

        return "/posts/list";
    }

    /**
     * Показать страницу со списком всех объявлений за прошлую неделю
     */
    @GetMapping("/lastweek")
    public String getAllPostsLastWeek(Model model) {

        model.addAttribute("posts",
                postService.findByLastWeekDesc());
        model.addAttribute("pageTitle", "Все объявления за прошлый день");

        return "/posts/list";
    }

    /**
     * Показать страницу с использованием фильтров
     * (в настоящий момент функция не реализована)
     * TODO требуется разработать HTML-thymeleaf форму с фильтрами
     */
    @GetMapping("/filter")
    public String getFilterForm(Model model) {

        model.addAttribute("posts",
                postService.findByLastWeekDesc());
        model.addAttribute("brands", carBrandService.findAll());
        model.addAttribute("pageTitle", "Фильтр");

        return "/posts/list";
    }

    /**
     * Показать полную карточку объявления
     */
    @GetMapping("/{id}")
    public String getPageById(@PathVariable int id,
                              Model model,
                              HttpServletRequest request) {

        Optional<PostFullViewDto> postFullDto = postService.getPostById(id);

        if (postFullDto.isEmpty()) {
            model.addAttribute("message",
                    "Объявление с указанным идентификатором не найдено");
            return "/errors/404";
        }

        model.addAttribute("postFullDto", postFullDto.get());

        Optional<User> optionalUser = Optional.of(
                (User) request.getAttribute("user"));

        model.addAttribute("user", optionalUser.get());

        return "/posts/one";
    }

    /**
     * Показать страницу для создания объявления
     */
    @GetMapping("/create")
    public String getCreationPage(Model model) {

        model.addAttribute("carBrands", carBrandService.findAll());
        model.addAttribute("bodyTypes", bodyTypeService.findAll());
        model.addAttribute("engines", engineService.findAll());

        return "/posts/create";
    }

    /**
     * Создать новое объявление из данных пользователя
     */
    @PostMapping("/create")
    public String create(@ModelAttribute PostCreateDto dto,
                         HttpServletRequest request,
                         Model model) {

        Optional<User> optionalUser = Optional.of(
                (User) request.getSession().getAttribute("user"));
        dto.setUser(optionalUser.get());

        if (postService.add(dto)) {
            return "redirect:/posts/all";
        }

        model.addAttribute("message",
                "Не удалось создать объявление с указанным идентификатором");

        return "/errors/404";
    }

    /**
     * Показать страницу для редактирования цены объявления
     */
    @GetMapping("/price/{id}")
    public String getEditPricePageById(@PathVariable int id,
                                       Model model) {
        Optional<PriceEditDto> editPostDto = postService.getEditPriceDtoById(id);

        if (editPostDto.isEmpty()) {
            model.addAttribute("message",
                    "Объявление с указанным идентификатором не найдено");
            return "/errors/404";
        }

        model.addAttribute("editPostDto", editPostDto.get());

        return "posts/price";
    }

    /**
     * Редактировать цену объявление в соответствии с данными пользователя
     */
    @PostMapping("/price")
    public String update(@ModelAttribute PriceEditDto priceEditDto,
                         HttpServletRequest request,
                         Model model) {

        Optional<User> optServletUser = Optional.of(
                (User) request.getAttribute("user"));
        int postUserId = postService.getPostById(priceEditDto.getPostId()).get().getUserId();

        if (optServletUser.get().getId() == postUserId
                && postService.updatePrice(priceEditDto)) {
            return "redirect:/posts/" + priceEditDto.getPostId();
        }
        model.addAttribute("message",
                "Не удалось изменить цену в объявлении с указанным идентификатором");

        return "/errors/404";
    }

    /**
     * Удалить объявление
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id,
                         HttpServletRequest request,
                         Model model) {

        Optional<User> optServletlUser = Optional.of(
                (User) request.getAttribute("user"));
        int postUserId = postService.getPostById(id).get().getUserId();

        if (optServletlUser.get().getId() == postUserId
                && postService.deleteById(id)) {
            return "redirect:/posts/all";
        }
        model.addAttribute("message",
                "Не удалось удалить объявление с указанным идентификатором");

        return "/errors/404";
    }

    /**
     * Изменить статус объявление
     */
    @GetMapping("/sold/{id}")
    public String changeDoneToFalse(@PathVariable int id,
                                    HttpServletRequest request,
                                    Model model) {

        Optional<User> optServletlUser = Optional.of(
                (User) request.getAttribute("user"));
        int postUserId = postService.getPostById(id).get().getUserId();

        if (optServletlUser.get().getId() == postUserId
                && postService.updateStatusById(id, true)) {
            return "redirect:/posts/" + id;
        }
        model.addAttribute("message",
                "Не удалось обновить статус объявления");

        return "/errors/404";
    }
}
