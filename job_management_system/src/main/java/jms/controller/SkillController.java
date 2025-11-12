// SkillController.java
package jms.controller;

import jms.entity.Skill;
import jms.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/skills")
@Slf4j
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public String listSkills(Model model) {
        List<Skill> skills = skillService.getAllSkills();
        model.addAttribute("skills", skills);
        model.addAttribute("currentPath", "/skills");
        return "skills/list";
    }

    @PostMapping("/create")
    public String createSkill(@RequestParam String name,
                              RedirectAttributes redirectAttributes) {
        try {
            Skill skill = Skill.builder().name(name).build();
            skillService.createSkill(skill);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo kỹ năng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo kỹ năng: " + e.getMessage());
        }
        return "redirect:/skills";
    }

    @PostMapping("/{id}/edit")
    public String updateSkill(@PathVariable Long id,
                              @RequestParam String name,
                              RedirectAttributes redirectAttributes) {
        try {
            Skill skill = Skill.builder().name(name).build();
            skillService.updateSkill(id, skill);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật kỹ năng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật kỹ năng: " + e.getMessage());
        }
        return "redirect:/skills";
    }

    @PostMapping("/{id}/delete")
    public String deleteSkill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            skillService.deleteSkill(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa kỹ năng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa kỹ năng: " + e.getMessage());
        }
        return "redirect:/skills";
    }
}