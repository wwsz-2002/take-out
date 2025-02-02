package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("员工登陆")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());//放入员工的id
        String token = JwtUtil.createJWT(//调用工具类中的JWT生成
                jwtProperties.getAdminSecretKey(),//传入密钥
                jwtProperties.getAdminTtl(),//传入过期时间
                claims);//传入数据

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工登出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    //新建员工
    @ApiOperation("新建员工")
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工");

        employeeService.save(employeeDTO);
        return Result.success();
    }

    //查询员工
    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("搜索员工数据");
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    //修改员工启用禁用状态
    @ApiOperation("启用禁用员工账号")
    @PostMapping("status/{status}")
    public Result startOrStop(Long id,@PathVariable Integer status){
        log.info("启用禁用员工账号,id:{},status:{}",id,status);
        employeeService.startOrStop(id,status);
        return Result.success();
    }

    //根据id查询员工信息
    @ApiOperation("根据id查询员工信息")
    @GetMapping("{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    //修改员工信息

    @ApiOperation("编辑员工信息")
    @PutMapping
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息：{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
