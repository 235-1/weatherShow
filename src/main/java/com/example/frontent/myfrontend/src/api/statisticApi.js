import request from '@/api/index'

export const getKpi = async () => {
    const res = await request.get('/kpi')
    return res.data
}

// 返回气温趋势图
export const getTrendYear = async () => {
    const res = await request.get('/trendYear')
    return res.data
}

//月度气温趋势图

export const getTrendMonth = async () => {
    const res = await request.get('/trendMonth')
    return res.data
}
