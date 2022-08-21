package org.example.plus;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.plus.domain.Employee;
import org.example.plus.factory.PlusSessionFactory;
import org.example.plus.mapper.EmployeeMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class PlusUsingTest {
    public static void main(String[] args) throws IOException {
        SqlSessionFactory sessionFactory = PlusSessionFactory.getSessionFactory();

        SqlSession sqlSession = sessionFactory.openSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        // 1、@link com.baomidou.mybatisplus.core.enums.SqlMethod
        Employee employee = mapper.selectById(2);
        System.out.println(employee);
        LambdaQueryWrapper<Employee> in = Wrappers.<Employee>lambdaQuery().in(Employee::getId, Arrays.asList(1, 2, 3));
        List<Employee> id = mapper.selectList(in);

        System.out.println(id);
    }

}
