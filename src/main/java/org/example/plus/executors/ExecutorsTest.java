package org.example.plus.executors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.plus.domain.Employee;
import org.example.plus.factory.PlusSessionFactory;
import org.example.plus.mapper.EmployeeMapper;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/8/22
 */
public class ExecutorsTest {
    public static void main(String[] args) throws IOException {
        SqlSessionFactory sessionFactory = PlusSessionFactory.getSessionFactory();

        // 核心点，根据不同的使用场景。
        SqlSession sqlSession = sessionFactory.openSession(ExecutorType.REUSE);
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        // 1、@link com.baomidou.mybatisplus.core.enums.SqlMethod
        Employee employee = mapper.selectById(2);
        System.out.println(employee);
        LambdaQueryWrapper<Employee> in = Wrappers.<Employee>lambdaQuery().in(Employee::getId, Arrays.asList(1, 2, 3));
        mapper.selectList(in).forEach(System.out::println);
    }
}
