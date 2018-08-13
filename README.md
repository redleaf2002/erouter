# erouter

为了组件化开发 主要提供了下面两个用途 
1. 多module之间跳转activity 
2. 多module之间提供发送事件的能力 


## 特点
实现了arouter和eventbus的主要功能 有利于各个module的解耦

## 使用:
#### 在需要使用的module的build gradle中增加两个依赖

    defaultConfig {
          javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath = true
                arguments = [moduleName: project.getName()]
            }
        }
    }
    annotationProcessor "com.leaf:eroutercompiler:1.0.1"
    implementation 'com.leaf:erouterapi:1.0.1'
    
### 1. activity跳转功能 

#### 1.  在目标activity中增加

@EasyRouter(path = "/test/SkipActivity")
public class SkipActivity extends AppCompatActivity {

#### 2. 在需要的地方跳转
 ERouter.getInstance().build(MainActivity.this).setIntent(null).skipTo
                        ("/test/SkipActivity");

### 2. post事件数据

#### 1. 定义自己的数据对象（如EventInfoBean）并在需要的地方发送 
  ERouter.getInstance().post(new EventInfoBean(content));

#### 2. 在接收的地方增加注解和解除注解

  ERouter.getInstance().register(this);
  ERouter.getInstance().unregister(this);
  
###### 增加注解方法：
   @EasyRegister()
   public void onEventInfoBean(final EventInfoBean event) {
   }
 








