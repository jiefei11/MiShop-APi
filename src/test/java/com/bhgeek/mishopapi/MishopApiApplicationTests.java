package com.bhgeek.mishopapi;

import com.bhgeek.mishopapi.utils.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MishopApiApplicationTests {

	@Autowired
	private EmailUtil emailUtil;

	@Test
	void contextLoads() {

	}

	@Test
	void sendStringEmail() {
		// 测试文本邮件发送（无附件）
		String to = "bhgeek@qq.com"; // 这是个假邮箱，写成你自己的邮箱地址就可以
		String title = "文本邮件发送测试";
		String content = "文本邮件发送测试";
		emailUtil.sendMessage(to, title, content);
	}
}
