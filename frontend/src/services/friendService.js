import api from './api';

/**
 * 好友服务
 */
const friendService = {
  /**
   * 发送好友请求
   * @param {Object} params - 请求参数
   * @param {string} params.username - 发送请求的用户名
   * @param {string} params.friendUsername - 好友用户名
   * @param {string} [params.message] - 请求消息
   * @returns {Promise}
   */
  sendFriendRequest: async (params) => {
    try {
      return await api.post('/friend/request', params);
    } catch (error) {
      console.error('发送好友请求失败:', error);
      throw error;
    }
  },

  /**
   * 处理好友请求
   * @param {Object} params - 请求参数
   * @param {string} params.username - 当前用户名
   * @param {number} params.requestId - 请求ID
   * @param {number} params.status - 状态（1-接受，2-拒绝）
   * @returns {Promise}
   */
  handleFriendRequest: async (params) => {
    try {
      return await api.post('/friend/handle', params);
    } catch (error) {
      console.error('处理好友请求失败:', error);
      throw error;
    }
  },

  /**
   * 获取好友列表
   * @param {string} username - 当前用户名
   * @returns {Promise}
   */
  getFriendList: async (username) => {
    try {
      return await api.get('/friend/list', {
        params: { username }
      });
    } catch (error) {
      console.error('获取好友列表失败:', error);
      throw error;
    }
  },

  /**
   * 获取好友申请列表
   * @param {string} username - 当前用户名
   * @returns {Promise}
   */
  getFriendApplications: async (username) => {
    try {
      return await api.get('/friend/applications', {
        params: { username }
      });
    } catch (error) {
      console.error('获取好友申请列表失败:', error);
      throw error;
    }
  },

  /**
   * 删除好友
   * @param {Object} params - 请求参数
   * @param {string} params.username - 当前用户名
   * @param {number} params.friendId - 好友ID
   * @returns {Promise}
   */
  deleteFriend: async (params) => {
    try {
      return await api.post('/friend/delete', params);
    } catch (error) {
      console.error('删除好友失败:', error);
      throw error;
    }
  }
};

export default friendService;