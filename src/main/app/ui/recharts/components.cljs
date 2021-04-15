(ns app.ui.recharts.components
  (:require
    ["recharts" :refer [LineChart Line XAxis YAxis CartesianGrid
                        Tooltip Legend ResponsiveContainer LinearGradient
                        AreaChart Area Label ScatterChart Scatter BarChart Bar]]
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]))

(def line-chart (interop/react-factory LineChart))
(def line (interop/react-factory Line))
(def x-axis (interop/react-factory XAxis))
(def y-axis (interop/react-factory YAxis))
(def cartesian-grid (interop/react-factory CartesianGrid))
(def tooltip (interop/react-factory Tooltip))
(def legend (interop/react-factory Legend))
(def responsive-container (interop/react-factory ResponsiveContainer))
(def linear-gradient (interop/react-factory LinearGradient))
(def area-chart (interop/react-factory AreaChart))
(def area (interop/react-factory Area))
(def scatter-chart (interop/react-factory ScatterChart))
(def scatter (interop/react-factory Scatter))
(def bar-chart (interop/react-factory BarChart))
(def bar (interop/react-factory Bar))
(def label (interop/react-factory Label))