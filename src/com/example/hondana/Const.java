
package com.example.hondana;

public class Const {

    public static final String BOOK_ONCLICK = "book_onclick";

    public static final String BOOKS_SELECTED = "books_selected";

    public static final String FROM_ALL_OR_SEL = "from_all_or_sel";

    // shelf style
    /** 本棚表示スタイル */
    public static final String SHELF_STYLE = "shelf_style";
    /** 本棚表示スタイル　本棚:　各行に複数コンテンツ */
    public static final int SHELF_STYLE_GRID = 1;
    /** 本棚表示スタイル　リスト: 各行にコンテンツ1個のみ、コンテンツ詳細情報 */
    public static final int SHELF_STYLE_LIST = 2;

    // shelf mode
    /** 本棚表示モード */
    public static final String SHELF_MODE = "shelf_mode";
    /** 本棚表示モード　ノーマル */
    public static final int SHELF_MODE_NORMAL = 1;
    /** 本棚表示モード　編集 */
    public static final int SHELF_MODE_EDIT = 2;

    /** コンテンツ長さ */
    public static final int CONTENT_WIDTH = 180;
    /** Navigation Drawerのアイテム数 */
    public static final int NUMS_IN_NAVI = 5;

    // Sort
    /** コンテンツソート順 */
    public static final String SORT_TYPE = "sort_type";
    /** コンテンツタイトル順でソート */
    public static final int SORT_BY_CONTENT_TITLE = 1;
    /** コンテンツ著者順でソート */
    public static final int SORT_BY_CONTENT_AUTHOR = 2;

    // Cache
    /** percent of memory to use in cache */
    public static final int PERCENT_TO_USE = 25;
}
