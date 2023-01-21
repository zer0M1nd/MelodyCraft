## MelodyCraft

在 Minecraft 里打音游

鸽了三年了，谁爱写谁写

---

## 如何编译

- clone 此仓库

- clone [HCIU's Utils](https://github.com/zer0M1nd/HCIUUtils)

- gradlew setUpDecompWorkspace & gradlew eclipse 两个仓库都需要

- 将两个仓库都导入进 eclipse

- 将 HCIU's Utils 加入该项目的 Build Path

- 将 libs 里的包都加入该项目的 Build Path

- 现在你应该可以在 Eclipse 里编译运行了。如果你需要导出，请将 HCIU's Utils 打包（gradlew build），扔进 libs 里。

## 添加谱面

- 谱面格式暂时懒得写，想了解谱面格式可以去翻源代码

- 谱面放在 .minecraft/melodycraft 文件夹下

- 启动或使用指令 /mec reload 会从文件夹重新读取

- 使用指令 /mec convert 然后选择一个文件夹，可以将这个文件夹的 malody 谱面转换为 MelodyCraft 的谱面，并放在默认文件夹下。

- 在 [MelodyCraft-Maps](https://github.com/zer0M1nd/MelodyCraft-Maps) 仓库里扔了一些 melodycraft 的谱面和它们的原谱面，如果你急需测试可以直接下载。谱面从作者本人的 Malody 里直接抠的，如有侵权请联系删除。

## 如何打歌

- 在创造模式物品栏里找到街机，摆下来，右键

## TODO

完整的单机 4k 模式还缺失以下内容：

- 结算界面

- ~客户端设置界面（谱面流速……）~

- 分数保存及显示

界面及系统优化：

- 更好的打歌界面（判定效果……）

- 游戏内显示曲绘，延迟统计（或结算界面）

- 延迟校准辅助

- Mods （Const，变速，Auto）

未来计划：

- 在 TileEntity 上显示打歌界面

- 允许离开 Gui 直接对着 TileEntity 打歌

- 多人联机系统

  - 服务器上传/下载谱面

  - 服务器排行榜

  - 联机模式

- 其他游戏模式

  - 5k, 6k, 7k

  - 4k+ （添加bomb，必perfect note等特殊玩法，小节线，timinggroup甚至camera等特殊机制）

  - 自创其他游戏模式
