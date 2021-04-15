(ns app.ui.antd.components
  (:require
    ["antd" :refer [Drawer Form Button Col Row Input Input.Password Select
                    Select.Option Modal Form.Item Card Spin Avatar Menu
                    Menu.Item Menu.ItemGroup Dropdown Space Divider Layout
                    Layout.Header Layout.Content Breadcrumb Breadcrumb.Item
                    Layout.Footer Layout.Sider Menu.SubMenu DatePicker
                    DatePicker.RangePicker Card.Meta message List List.Item
                    Switch Radio Radio.Group]]
    ["@ant-design/icons" :refer [UserOutlined LockOutlined LineChartOutlined
                                 BarChartOutlined DotChartOutlined AreaChartOutlined
                                 HomeOutlined SettingOutlined
                                 CaretDownOutlined CaretUpOutlined
                                 CheckOutlined RedoOutlined]]
    ["@ant-design/colors" :refer [blue.primary]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]))

(def drawer (interop/react-factory Drawer))
(def form (interop/react-factory Form))
(def form-item (interop/react-factory Form.Item))
(def button (interop/react-factory Button))
(def col (interop/react-factory Col))
(def row (interop/react-factory Row))
(def input (interop/react-factory Input))
(def input-password (interop/react-factory Input.Password))
(def select (interop/react-factory Select))
(def select-option (interop/react-factory Select.Option))
(def modal (interop/react-factory Modal))
(def user-outlined (interop/react-factory UserOutlined))
(def lock-outlined (interop/react-factory LockOutlined))
(def card (interop/react-factory Card))
(def spin (interop/react-factory Spin))
(def avatar (interop/react-factory Avatar))
(def menu (interop/react-factory Menu))
(def menu-item (interop/react-factory Menu.Item))
(def dropdown (interop/react-factory Dropdown))
(def menu-item-group (interop/react-factory Menu.ItemGroup))
(def space (interop/react-factory Space))
(def divider (interop/react-factory Divider))
(def layout (interop/react-factory Layout))
(def breadcrumb (interop/react-factory Breadcrumb))
(def breadcrumb-item (interop/react-factory Breadcrumb.Item))
(def line-chart-outlined (interop/react-factory LineChartOutlined))
(def area-chart-outlined (interop/react-factory AreaChartOutlined))
(def bar-chart-outlined (interop/react-factory BarChartOutlined))
(def dot-chart-outlined (interop/react-factory DotChartOutlined))
(def home-outlined (interop/react-factory HomeOutlined))
(def setting-outlined (interop/react-factory SettingOutlined))
(def caret-down (interop/react-factory CaretDownOutlined))
(def caret-up (interop/react-factory CaretUpOutlined))
(def check-outlined (interop/react-factory CheckOutlined))
(def redo-outlined (interop/react-factory RedoOutlined))
(def layout-header (interop/react-factory Layout.Header))
(def layout-content (interop/react-factory Layout.Content))
(def layout-footer (interop/react-factory Layout.Footer))
(def layout-sider (interop/react-factory Layout.Sider))
(def menu-submenu (interop/react-factory Menu.SubMenu))
(def date-picker (interop/react-factory DatePicker))
(def range-picker (interop/react-factory DatePicker.RangePicker))
(def card-meta (interop/react-factory Card.Meta))
(def list-ant (interop/react-factory List))
(def list-item-ant (interop/react-factory List.Item))
(def switch (interop/react-factory Switch))
(def radio (interop/react-factory Radio))
(def radio-group (interop/react-factory Radio.Group))

(def blue-primary blue.primary)
(def msg message)