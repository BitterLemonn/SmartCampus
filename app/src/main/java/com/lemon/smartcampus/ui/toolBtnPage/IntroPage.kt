package com.lemon.smartcampus.ui.toolBtnPage

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lemon.smartcampus.ui.theme.AppTheme
import com.lemon.smartcampus.ui.widges.ColoredTitleBar
import com.lemon.smartcampus.ui.widges.IntroduceTitle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun IntroPage(
    onBack: () -> Unit
) {
    val lazyState = rememberLazyListState()
    val listTitleCN = listOf(
        "学校简介", "历史沿革", "校区情况", "师资队伍", "学科发展", "人才培养", "科学研究", "国（境）外合作与交流"
    )
    var title by remember { mutableStateOf("") }
    LaunchedEffect(key1 = lazyState) {
        snapshotFlow { lazyState.firstVisibleItemIndex }.onEach {
            title = if (it != 0) listTitleCN[it / 2]
            else ""
        }.collect()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        ColoredTitleBar(color = Color.Transparent, text = title) { onBack.invoke() }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp), state = lazyState
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IntroduceTitle(titleCN = listTitleCN[0], titleEN = "Intro of GDUFS")
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.gdufs.edu.cn/images/n_about_fl01_img1.jpg").build(),
                    contentDescription = "campus image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray, BlendMode.Multiply
                    ) else null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "广东外语外贸大学是广东省高水平大学重点建设高校。学校具有鲜明的国际化特色，" + "是华南地区国际化人才培养，外国语言文学、全球经济治理、涉外法治研究及中华文化国际传播的重要基地。" + "现有在校全日制本科生20421人，博士、硕士研究生5323人，" + "外国留学生850人，各类成人本专科生、进修及培训生10000多人。",
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IntroduceTitle(titleCN = listTitleCN[1], titleEN = "History")
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.gdufs.edu.cn/images/n_about_fl01_img2.jpg").build(),
                    contentDescription = "campus image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray, BlendMode.Multiply
                    ) else null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = """
                    广东外语外贸大学的前身是1964年11月设立、1965年7月正式招生的广州外国语学院（1970年10月至1977年11月曾称“广东外国语学院”）和1980年12月成立的广州对外贸易学院。

                    广州对外贸易学院和广州外国语学院先后分别于1994年9月和1995年1月由对外经济贸易合作部和国家教育委员会划转广东省管理。1995年5月，广东省人民政府将两校合并组建广东外语外贸大学。

                    2008年10月，广东省人民政府将1996年4月成立的广东财经职业学院（1996年4月至2001年7月曾称“广东财税高等专科学校”）划入广东外语外贸大学。
                """.trimIndent(),
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IntroduceTitle(titleCN = listTitleCN[2], titleEN = "Campus")
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "学校地处中国历史文化名城和华南地区经济中心广州，辖4个校区，总面积2442亩(含规划)，" + "其中白云山校区位于广州市白云山北麓，占地939亩；大学城校区位于广州大学城，占地1095亩；" + "大朗校区位于广州市大朗，占地258亩；知识城校区位于中新广州知识城，规划占地约150亩。" + "校园内绿树成荫，小桥流水，鸟语花香，环境幽雅。",
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.3f)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://www.gdufs.edu.cn/__local/1/0E/ED/83A4BCF51BEF5A4BE5357B98DA6_0B766A62_1DB13.jpg")
                                .build(),
                            contentDescription = "campus image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.6f)
                                .clip(RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)),
                            contentScale = ContentScale.Crop,
                            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                                Color.Gray, BlendMode.Multiply
                            ) else null
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "白云山校区",
                            color = AppTheme.colors.hintDarkColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.3f)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://www.gdufs.edu.cn/__local/E/30/9E/7750AC4DB7A484EC30AA984154C_CB04C506_29DDE.jpg")
                                .build(),
                            contentDescription = "campus image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.6f)
                                .clip(RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)),
                            contentScale = ContentScale.Crop,
                            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                                Color.Gray, BlendMode.Multiply
                            ) else null
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "大学城校区",
                            color = AppTheme.colors.hintDarkColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.3f)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://www.gdufs.edu.cn/__local/9/7B/64/71FDAFC69EBB93C12A5318A17DB_2BB152D5_3AA20.jpg")
                                .build(),
                            contentDescription = "campus image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.6f)
                                .clip(RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)),
                            contentScale = ContentScale.Crop,
                            colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                                Color.Gray, BlendMode.Multiply
                            ) else null
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "知识城校区",
                            color = AppTheme.colors.hintDarkColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IntroduceTitle(titleCN = listTitleCN[3], titleEN = "Teaching Staff")
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.gdufs.edu.cn/images/n_about_fl04_img1.jpg").build(),
                    contentDescription = "campus image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray, BlendMode.Multiply
                    ) else null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "建校以来，梁宗岱、桂诗春、李筱菊、黄建华等名师大家荟萃学校，执教治学，" + "为学校积累了丰厚的精神文化财富。学校现有教职工总数2166人，专任教师1380人，" + "其中具有高级职称比例为55.07%，具有硕士以上学位比例为96.3%。教师队伍中，" + "教育部专业教学指导委员会委员14人，享受国务院政府特殊津贴55人，" + "入选全国文化名家暨“四个一批”人才工程5人，入选中宣部宣传思想文化青年英才2人，" + "入选“万人计划”哲学社会科学领军人才3人，入选“百千万人才工程”国家级人选4人，" + "入选教育部“新世纪优秀人才支持计划”13人，获国家外专局“高端外国专家”项目资助1人、" + "国家科技部“外国青年计划”项目资助3人。有国家级教学团队2个、国家级创新团队1个、" + "省级教学团队30个，国家级教学名师1人、省级教学名师10人，广东省“珠江学者”特聘教授4人、" + "“珠江学者”讲座教授4人，“青年珠江学者”6人，入选广东“特支计划”4人、" + "广东省“珠江人才计划”1人、广东省理论宣传青年优秀人才2人、广东省“千百十人才培养工程”" + "国家级培养对象1人、省级培养对象44人次，入选省优秀青年教师培养计划23名，" + "先后聘用“云山学者”426人。此外，学校还聘有22位客座教授和近100位长期外教。",
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IntroduceTitle(titleCN = listTitleCN[4], titleEN = "Discipline Development")
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.gdufs.edu.cn/images/n_about_fl05_img1.jpg").build(),
                    contentDescription = "campus image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray, BlendMode.Multiply
                    ) else null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "学校形成了外语学科与非外语学科“双轮驱动”、多学科协同发展的学科格局，下辖25个教学单位，" + "1个独立学院（南国商学院）。现有68个本科专业，分属文学、经济学、管理学、法学、工学、理学、" + "教育学、艺术学等8大学科门类。其中有37个国家级一流本科专业建设点，" + "29个省级一流本科专业建设点，2个国家级专业综合改革试点，14个省级专业综合改革试点，" + "有8个国家级特色专业建设点（含11个专业），24个省级特色专业建设点（含27个专业），" + "6个省级重点专业。共有28个外语语种，是华南地区外语语种最多的学校。" + "学校1981年获硕士学位授予权，1986年获博士学位授予权，是中国恢复研究生制度后较早获得硕士、" + "博士授予权的单位。学校现有1个国家级重点学科和8个省级重点学科。" + "拥有外国语言文学和应用经济学2个博士后科研流动站；另设有广东国际战略研究院博士后科研工作站，" + "在知识城校区设黄埔研究院博士后科研工作站分站，3个一级学科博士点和23个二级学科博士点，" + "13个一级学科硕士点和55个二级学科硕士点，17个专业学位硕士点。" + "在教育部学位与研究生教育发展中心组织开展的几轮学科评估中，" + "我校外国语言文学学科均位居全国高校前列。" + "2021年学校入选广东省“冲补强”提升计划高水平大学重点建设高校，" + "5个学科入选高水平大学重点建设学科。社会科学总论学科位居ESI全球前1%。",
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IntroduceTitle(titleCN = listTitleCN[5], titleEN = "Personnel Training")
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.gdufs.edu.cn/images/n_about_fl06_img1.jpg").build(),
                    contentDescription = "campus image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray, BlendMode.Multiply
                    ) else null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "学校以培养全球化高素质公民为使命，着力推进专业教学与外语教学的深度融合，着力培养具有家国情怀、" + "国际视野、过硬本领、创新能力、担当精神的高素质复合型国际化人才。" + "学校是联合国高端翻译人才培养大学外延计划的中国合作院校，国际大学翻译学院联合会（CIUTI）" + "和国际译联（FIT）联席会员，列入国际会议口译员协会学校名录（AIIC），" + "教育部高等学校翻译专业教学协作组秘书处所在单位，世界翻译教育联盟（WITTA）首创单位，" + "是入选中日韩三国首脑倡导的“亚洲校园”计划唯一一所外语类院校，入选教育部、" + "司法部法律硕士专业学位（涉外法律、国际仲裁）研究生项目，" + "拥有教育部普通高校外语非通用语种本科人才培养基地——非通用语种教学与研究中心、" + "教育部人才培养模式创新实验区——国际化商务人才培养模式创新实验区、" + "教育部高层次国际化人才培养创新实践基地、国家级实验教学示范中心——同声传译实验教学中心、" + "广东省协同育人平台——多语种高级翻译人才协同育人基地和国际服务外包人才协同育人基地，" + "“基础韩国语课程群虚拟教研室” “日语专业虚拟教研室”（日语专业建设系列全国唯一）" + "入选全国虚拟教研室建设试点。41门次在线开放课程入选国家高等教育智慧教育平台。" + "毕业生就业市场需求稳中有升,高质量就业持续向好,就业满意度继续保持高位。" + "学校面向全国20余个省、自治区、直辖市和港、澳、台地区招生，招生批次均为重点本科批。",
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IntroduceTitle(titleCN = listTitleCN[6], titleEN = "Scientific Research")
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "学校聚焦理论前沿和国家（广东）发展重大需求，构建高层次科研平台和智库体系，展现广外担当作为。建有3个国家级平台，分别是1个教育部普通高校人文社会科学重点研究基地（外国语言学及应用语言学研究中心），1个教育部战略研究基地(21世纪海上丝绸之路与区域创新国际战略研究中心),1个教育部国别和区域研究培育基地（加拿大研究中心）；6个教育部备案的国别和区域研究中心(非洲研究院、国际移民研究中心、印度尼西亚研究中心、拉丁美洲研究中心、欧洲研究中心、中南半岛研究中心)；组建外国语言学及应用语言学研究中心、广东国际战略研究院（海上丝绸之路研究院）、阐释学研究院、广东法治研究院（涉外法治研究院）、外国文学文化研究院、区域国别研究院、粤港澳大湾区研究院、国际经济贸易研究院等8个“大平台”，集中优势资源奋力推进学校科研事业发展。建有8个省级人文社会科学重点研究基地（外国文学文化研究院、国际经济贸易研究院、翻译学研究中心、粤商研究中心、党内法规研究中心、跨国并购与创新战略研究中心、金融开放与资产管理研究中心、区域法治研究院），3个广东省普通高校特色新型智库（广东省社会组织研究中心、亚太安全与经济合作研究中心、地方立法研究基地），4个广东普通高校哲学社会科学重点实验室（语言工程与计算重点实验室、双语认知与发展实验室、语言与人工智能重点实验室、全球产业链大数据实验室），2个广东省哲学社会科学重点实验室（全球治理与人类命运共同体建设重点实验室、大湾区建设与区域协调发展重点实验室），3个广东省决策咨询研究基地（区域法治研究院、国际服务经济研究院、阐释学研究院），1个广东省习近平新时代中国特色社会主义思想研究中心研究基地；4个广州市人文社科重点研究基地（广州国际商贸中心重点研究基地、粤港澳大湾区会计与经济发展研究中心、广州城市舆情治理与国际形象传播研究中心、广州华南财富管理中心研究基地）；1个广州市决策咨询研究基地（对外开放研究基地）。设有词典学研究中心、太平洋岛国战略研究中心、土地法制研究院、青少年法治教育中心等一批智库及研究机构。学校牵头组建的21世纪海上丝绸之路协同创新中心被认定为省部共建协同创新中心，外语研究与语言服务协同创新中心入选广东省国家级“2011协同创新中心”培育建设规划项目。学校承担有国家社科基金重大项目、教育部重大课题攻关项目和创新团队项目等一系列重大、重点项目，一批高质量研究成果获高校科学研究优秀成果...",
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IntroduceTitle(titleCN = listTitleCN[7], titleEN = "Cooperation & Communication")
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.gdufs.edu.cn/images/n_about_fl08_img1.jpg").build(),
                    contentDescription = "campus image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(
                        Color.Gray, BlendMode.Multiply
                    ) else null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = """
                        学校坚持开放办学，着力引进优质教育资源，积极开展与国（境）外大学、科研机构、国际组织以及企业的实质性合作，吸引国（境）外知名学者和杰出人才来校工作，积极探索联合培养高素质国际化人才的途径，共同开辟学术研究新的方向领域，大力发展留学生教育，积极提升学生、人才、教学、科研、管理五个方面的国际化水平。

                        学校加强全方位国（境）外教育合作与交流。已与60个国家和地区的488所大学和学术文化机构建立了合作交流关系。建有7所海外孔子学院和孔子课堂（日本札幌大学孔子学院、俄罗斯乌拉尔联邦大学孔子学院、秘鲁圣玛利亚天主教大学孔子学院、佛得角大学孔子学院、埃及艾因夏姆斯大学孔子学院、葡萄牙波尔图大学孔子学院以及澳大利亚紫薇孔子课堂）。与英国利兹大学合作举办英语教学硕士学位教育项目，与英国雷丁大学合作举办英语教育硕士学位教育项目，与澳门理工大学合作举办翻译专业本科教育项目。深度参与“一带一路”沿线国家和粤港澳大湾区高等教育合作，大力推进“亚洲校园”“湾区校园”建设；开展粤港澳联合培养研究生专项计划，着力打造国家留基委“国际治理创新研究生项目”“OECD人才培养项目”“高端传译人才培养项目”等国际组织后备人才项目以及“欧亚校园项目”“RCEP人才培养计划”等创新型人才培养项目；首倡成立“粤港澳大湾区葡语教育联盟”“粤港澳大湾区孔子学院合作大学联盟”，推动大湾区葡语教育及国际中文教育事业发展。注重提升管理国际化水平，实施海外高校挂职学习项目，已共派出8批合计40多名干部到海外高校挂职。
                    """.trimIndent(),
                    color = AppTheme.colors.textDarkColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }
        }

    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
private fun IntroPagePreview() {
    IntroPage {}
}