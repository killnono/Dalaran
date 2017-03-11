package com.example.killnono.dalaran.domain.task.strategy;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 17/3/8
 * Time: 下午8:12
 * Version: 1.0
 */
public enum StrategyType {
    LOCAL,/* 拿本地数据 */
    NO_LOCAL_NET_SAVE,/* 只返回一份数据:拿本地数据,有的话直接返回,没有返回网络,并保存数据 */
    LOCAL_REFRSH_CACHE,/*一直n*/
    NET,
    NET_CACHE,
    LOCAL_NET_SAVE,
    GROUP,

}
