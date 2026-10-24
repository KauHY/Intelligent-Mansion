/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-23 10:45:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-28 20:23:37
 */

type ElementContent<T> = T extends 'radio'|'checkbox'? string[]: string
export interface ConstraintCondition {
    required?:boolean,
    maxlength?:number,
    minlength?:number,
}

export type ElementType = 'radio'|'input'|'textarea'|'upload'|'checkbox'

export interface QuestionnaireConf {
    elementType: ElementType, // 标签类型
    constraintCondition?:ConstraintCondition, // 约束条件
    elementContent: ElementContent<QuestionnaireConf['elementType']>, // 选项
    id?:string, // id
    value:string,
    name:string, // 题目
}
