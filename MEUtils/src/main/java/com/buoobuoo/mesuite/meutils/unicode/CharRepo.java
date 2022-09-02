package com.buoobuoo.mesuite.meutils.unicode;

import lombok.Getter;

@Getter
public enum CharRepo {

    //MISC ICONS
    HEART('\uE001'),
    SPEECH('\uE002'),
    MANA_BALL_FULL('\uE003', 9),
    MANA_BALL_EMPTY('\uE004', 9),
    MANA_BALL_HALF('\uE005', 9),
    HEART_FULL('\uE006', 9),
    HEART_EMPTY('\uE007', 9),
    HEART_HALF('\uE008', 9),

    SLIDER_BAR_EMPTY('\uE009', 81),
    SLIDER_HEALTH_1('\uE00A', 1),
    SLIDER_HEALTH_2('\uE00B', 1),
    SLIDER_HEALTH_GENERIC('\uE00C', 1),
    SLIDER_MANA_1('\uE00D', 1),
    SLIDER_MANA_2('\uE00E', 1),
    SLIDER_MANA_GENERIC('\uE00F', 1),

    TEST_CHAR('\uE011', 1),

    //TAGS
    TAG_NUM_E('\uE401'),
    TAG_NUM_0('\uE402'),
    TAG_NUM_1('\uE403'),
    TAG_NUM_2('\uE404'),
    TAG_NUM_3('\uE405'),
    TAG_NUM_4('\uE406'),
    TAG_NUM_5('\uE407'),
    TAG_NUM_6('\uE408'),
    TAG_NUM_7('\uE409'),
    TAG_NUM_8('\uE40A'),
    TAG_NUM_9('\uE40B'),
    TAG_LEVEL_UP('\uE40C'),

    RANK_ADMIN_TAG('\uE40D'),
    RANK_DEVELOPER_TAG('\uE40E'),

    RARITY_COMMON('\uE415'),
    RARITY_UNCOMMON('\uE416'),
    RARITY_RARE('\uE417'),
    RARITY_ULTRA_RARE('\uE418'),
    RARITY_ULTRA_UNIQUE('\uE419'),

    //STATUS EFFECT ICONS
    STATUS_EFFECT_FLAGELLANT('\uEC01', 9),
    STATUS_EFFECT_BULWARK('\uEC02', 9),

    //DIALOGUE PORTRAITS
    UI_DIALOGUE_BORDER('\uF001'),
    UI_PORTRAIT_QUEST_STARTED('\uF002'),
    UI_PORTRAIT_QUEST_COMPLETE('\uF003'),
    UI_PORTRAIT_QUEST_CHECKPOINT('\uF004'),
    UI_PORTRAIT_CAPTAIN_YVES('\uF005'),
    UI_PORTRAIT_ARAMORE_BLACKSMITH('\uF006'),
    UI_PORTRAIT_JAYCE('\uF007'),

    //INVENTORY OVERLAYS
    UI_INVENTORY_DEFAULT_CRATE_OPEN('\uF401'),
    UI_INVENTORY_PLAYER_SEARCH_OVERLAY('\uF402'),

    UI_INVENTORY_PARTY_DECO_1('\uF406'),
    UI_INVENTORY_PARTY_MAIN_MEMBER('\uF403'),
    UI_INVENTORY_PARTY_MAIN_LEADER('\uF404'),
    UI_INVENTORY_PARTY_EMPTY('\uF405'),

    UI_INVENTORY_PROFILE_SELECT_MAIN('\uF407'),
    UI_INVENTORY_PROFILE_SELECT_FULL('\uF408'),
    UI_INVENTORY_PROFILE_EDIT_MAIN('\uF409'),
    UI_INVENTORY_PROFILE_ICON_EDIT('\uF40A'),
    UI_INVENTORY_PROFILE_CONFIRM_DELETE('\uF40B'),

    UI_INVENTORY_LOGOUT('\uF40C'),

    UI_INVENTORY_PLAYERMENU_ABILITIES('\uF40D'),
    UI_INVENTORY_PLAYERMENU_ABILITY_CASTTYPE('\uF40E'),
    UI_INVENTORY_PLAYERMENU_SELF_MAIN('\uF40F'),
    UI_INVENTORY_PLAYERMENU_OTHER_MAIN('\uF410'),
    UI_INVENTORY_PLAYERMENU_SETTINGS_1('\uF41F'),

    UI_INVENTORY_PLAYERMENU_HELMET_ICON('\uF411'),
    UI_INVENTORY_PLAYERMENU_CHESTPLATE_ICON('\uF412'),
    UI_INVENTORY_PLAYERMENU_LEGGINGS_ICON('\uF413'),
    UI_INVENTORY_PLAYERMENU_BOOTS_ICON('\uF414'),

    UI_INVENTORY_PLAYERMENU_ADD_FRIEND('\uF415'),
    UI_INVENTORY_PLAYERMENU_ADD_PARTY('\uF416'),
    UI_INVENTORY_PLAYERMENU_WHISPER('\uF417'),
    UI_INVENTORY_PLAYERMENU_TRADE('\uF418'),

    //letters for width calcs

    A('A', 5),
    a('a', 5),
    B('B', 5),
    b('b', 5),
    C('C', 5),
    c('c', 5),
    D('D', 5),
    d('d', 5),
    E('E', 5),
    e('e', 5),
    F('F', 5),
    f('f', 4),
    G('G', 5),
    g('g', 5),
    H('H', 5),
    h('h', 5),
    I('I', 3),
    i('i', 1),
    J('J', 5),
    j('j', 5),
    K('K', 5),
    k('k', 4),
    L('L', 5),
    l('l', 1),
    M('M', 5),
    m('m', 5),
    N('N', 5),
    n('n', 5),
    O('O', 5),
    o('o', 5),
    P('P', 5),
    p('p', 5),
    Q('Q', 5),
    q('q', 5),
    R('R', 5),
    r('r', 5),
    S('S', 5),
    s('s', 5),
    T('T', 5),
    t('t', 4),
    U('U', 5),
    u('u', 5),
    V('V', 5),
    v('v', 5),
    W('W', 5),
    w('w', 5),
    X('X', 5),
    x('x', 5),
    Y('Y', 5),
    y('y', 5),
    Z('Z', 5),
    z('z', 5),
    NUM_1('1', 5),
    NUM_2('2', 5),
    NUM_3('3', 5),
    NUM_4('4', 5),
    NUM_5('5', 5),
    NUM_6('6', 5),
    NUM_7('7', 5),
    NUM_8('8', 5),
    NUM_9('9', 5),
    NUM_0('0', 5),
    EXCLAMATION_POINT('!', 1),
    AT_SYMBOL('@', 6),
    NUM_SIGN('#', 5),
    DOLLAR_SIGN('$', 5),
    PERCENT('%', 5),
    UP_ARROW('^', 5),
    AMPERSAND('&', 5),
    ASTERISK('*', 5),
    LEFT_PARENTHESIS('(', 4),
    RIGHT_PERENTHESIS(')', 4),
    MINUS('-', 5),
    UNDERSCORE('_', 5),
    PLUS_SIGN('+', 5),
    EQUALS_SIGN('=', 5),
    LEFT_CURL_BRACE('{', 4),
    RIGHT_CURL_BRACE('}', 4),
    LEFT_BRACKET('[', 3),
    RIGHT_BRACKET(']', 3),
    COLON(':', 1),
    SEMI_COLON(';', 1),
    DOUBLE_QUOTE('"', 3),
    SINGLE_QUOTE('\'', 1),
    LEFT_ARROW('<', 4),
    RIGHT_ARROW('>', 4),
    QUESTION_MARK('?', 5),
    SLASH('/', 5),
    BACK_SLASH('\\', 5),
    LINE('|', 1),
    TILDE('~', 5),
    TICK('`', 2),
    PERIOD('.', 1),
    COMMA(',', 1),
    SPACE(' ', 3),
    DEFAULT('\u0000', 0),

    //spacing

   NEG1('\uF801', -1, -2),
   NEG2('\uF802', -2, -3),
   NEG4('\uF804', -4, -5),
   NEG8('\uF808', -8, -9),
   NEG16('\uF809', -16, -17),
   NEG32('\uF80A', -32, -33),
   NEG64('\uF80B', -64, -65),
   NEG128('\uF80C', -128, -129),
   NEG256('\uF80D', -256, -257),
   NEG512('\uF80E', -512, -513),
   NEG1024('\uF80F', -1024, -1025),

   POS1('\uF821', 1, 0),
   POS2('\uF822', 2, 1),
   POS4('\uF824', 4, 3),
   POS8('\uF828', 8, 7),
   POS16('\uF829', 16, 15),
   POS32('\uF82A', 32, 31),
   POS64('\uF82B', 64, 63),
   POS128('\uF82C', 128, 127),
   POS256('\uF82D', 256, 255),
   POS512('\uF82E', 512, 511),
   POS1024('\uF82F', 1024, 1023);


    private char ch;
    private int width;
    private int effectiveWidth;
    CharRepo(char ch){
        this.ch = ch;
    }

    CharRepo(char ch, int width){
        this.ch = ch;
        this.width = width;
        this.effectiveWidth = width;
    }

    CharRepo(char ch, int width, int effectiveWidth){
        this.ch = ch;
        this.width = width;
        this.effectiveWidth = effectiveWidth;
    }


    @Override
    public String toString(){
        return ch + "";
    }

    public static CharRepo numAsString(char c){
        return switch (c) {
            case '1' -> TAG_NUM_1;
            case '2' -> TAG_NUM_2;
            case '3' -> TAG_NUM_3;
            case '4' -> TAG_NUM_4;
            case '5' -> TAG_NUM_5;
            case '6' -> TAG_NUM_6;
            case '7' -> TAG_NUM_7;
            case '8' -> TAG_NUM_8;
            case '9' -> TAG_NUM_9;
            default -> TAG_NUM_0;
        };
    }

    public static String numToTagString(int n){
        String str = Integer.toString(n);
        StringBuilder sb = new StringBuilder();

        sb.append(TAG_NUM_E);
        for(char c : str.toCharArray()){
            sb.append(UnicodeSpaceUtil.getNeg(1));
            sb.append(numAsString(c));
        }

        return sb.toString();
    }

    public int getBoldLength() {
        if (this == CharRepo.SPACE) return this.width;
        return this.width + 1;
    }

    public static CharRepo getDefaultFontInfo(char c) {
        for (CharRepo dFI : CharRepo.values()) {
            if (dFI.getCh() == c) return dFI;
        }
        return CharRepo.DEFAULT;
    }

    public static int getPixelWidth(String str){
        int val = 0;
        for(char ch : str.toCharArray()){
            val += CharRepo.getDefaultFontInfo(ch).getEffectiveWidth();
            val += 1; //acount for spacing
        }
        return val;
    }
}
