通过微信公众号的 API 接口实现部分功能，这里主要是存放自动回复的消息。

我们的 API [后端代码](https://github.com/jenkins-zh/wechat-backend)是基于 Golang 编写的。

支持的消息类型包括：

* 文本
* 图片
* 文章（公众号中的）

上面提到的消息类型，也就是字段 `msgType` 的值，包括：`text`、`image`、`news`。
更多的细节可以[参考源码](https://github.com/jenkins-zh/wechat-backend/blob/master/pkg/reply/core.go)。
