package org.example.plus.factory;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PlusSessionFactory {
    public static SqlSessionFactory getSessionFactory() throws IOException {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        MybatisConfiguration configuration = new MybatisConfiguration();
        initConfiguration(configuration);
        configuration.addInterceptor(initInterceptor());
        //扫描mapper接口所在包
        configuration.addMappers("org.example.plus.mapper");
        //配置日志实现
//        configuration.setLogImpl(Slf4jImpl.class);
        configuration.setLogImpl(Log4jImpl.class);
//        configuration.setLogImpl(StdOutImpl.class);

//        configuration.setLogPrefix();
        //构建mybatis-plus需要的globalconfig
        GlobalConfig globalConfig = new GlobalConfig();
        //此参数会自动生成实现baseMapper的基础方法映射
        globalConfig.setSqlInjector(new DefaultSqlInjector());
        //设置id生成器
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        //设置超类mapper
        globalConfig.setSuperMapperClass(BaseMapper.class);

        //给configuration注入GlobalConfig里面的配置
//        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);
        configuration.setGlobalConfig(globalConfig);
        //设置数据源
        Environment environment = new Environment("1", new JdbcTransactionFactory(), initDataSource());
        configuration.setEnvironment(environment);

        registryMapperXml(configuration, "mapper/plus");
        //构建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = builder.build(configuration);
//————————————————
//        版权声明：本文为CSDN博主「小小白鸽」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/qq_42413011/article/details/118640420
        return sqlSessionFactory;
    }

    private static void initConfiguration(MybatisConfiguration configuration) {
        //开启驼峰大小写转换
        configuration.setMapUnderscoreToCamelCase(true);
        //配置添加数据自动返回数据主键
        configuration.setUseGeneratedKeys(true);
    }


    private static Interceptor initInterceptor() {
        //创建mybatis-plus插件对象
        //构建分页插件
        PaginationInterceptor pagInter = new PaginationInterceptor();
        pagInter.setDbType(DbType.MYSQL);
        pagInter.setOverflow(true);
        pagInter.setLimit(500L);
        return pagInter;
    }

    /**
     * 解析mapper.xml文件
     * @param configuration
     * @param classPath
     * @throws IOException
     */
    private static void registryMapperXml(MybatisConfiguration configuration, String classPath) throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> mapper = contextClassLoader.getResources(classPath);
        while (mapper.hasMoreElements()) {
            URL url = mapper.nextElement();
            if (url.getProtocol().equals("file")) {
                String path = url.getPath();
                File file = new File(path);
                File[] files = file.listFiles();
                for (File f : files) {
                    FileInputStream in = new FileInputStream(f);
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, f.getPath(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                    in.close();
                }
            } else {
                JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = urlConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.getName().endsWith(".xml")) {
                        InputStream in = jarFile.getInputStream(jarEntry);
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, jarEntry.getName(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                        in.close();
                    }
                }
            }
        }
    }

    private static DataSource initDataSource() {
//        driver=com.mysql.jdbc.Driver
//        url=jdbc:mysql://localhost:3306/mybatis
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis?allowPublicKeyRetrieval=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8&useUnicode=true");
        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
//        dataSource.setIdleTimeout(60000);
//        dataSource.setAutoCommit(true);
//        dataSource.setMaximumPoolSize(5);
//        dataSource.setMinimumIdle(1);
//        dataSource.setMaxLifetime(60000 * 10);
//        dataSource.setConnectionTestQuery("SELECT 1");
        return dataSource;
    }
}
