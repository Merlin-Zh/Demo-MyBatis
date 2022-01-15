# 自定义数据库持久层框架

这是一个用于学习MyBatis源码时创建的一个自定义持久层框架， 和MyBatis核心功能一样。

---
## 设计思路

### 使用端

使用端需要引入自定义框架的jar包，为简便开发，此项目中将使用端和框架端做两个模块，放在同一个工程中。

使用端需要以配置文件的形式提供配置信息，包括：

- 数据库配置信息（sqlMapConfig.xml）：数据库地址、驱动类、用户名、密码、mapper.xml路径等
- SQL配置信息（Mapper.xml）：SQL语句、参数类型、返回值类型等


### 框架端

本质就是对JDBC代码的封装。

1. 加载配置文件：根据配置文件的路径，加载配置文件成字节输入流存储在内存中
   - 创建Resources类，方法InputStream getResourcesAsStream(String path)
2. 创建两个JavaBean（容器对象）：存放配置文件解析出来的内容
   - Configuration：核心配置类，存放sqlMapConfig.xml解析的内容
   - MappedStatement：映射配置类，存放mapper.xml解析的内容
3. 解析配置文件：使用dom4j进行xml文件解析
   - 创建SqlSessionFactoryBuilder类，方法build(InputStream is)
   - 使用dom4j解析配置文件并封装到对象中
   - 创建SqlSessionFactory对象，用于生产SqlSession会话对象（`工厂模式`）
4. 基于开闭原则创建SqlSessionFactory接口及实现类DefaultSqlSessionFactory
   - 方法：openSession()，打开会话
5. 创建SqlSession接口及实现类DefaultSqlSession，定义对数据库的CRUD操作
6. 创建Executor接口及实现类SimpleExecutor
   - 方法query(Configuration config, MappedStatement ms, Object... params)执行JDBC代码
   
