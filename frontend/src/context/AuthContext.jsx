import { createContext, useContext, useEffect, useState } from 'react'
import api from '../api/axios'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const stored = localStorage.getItem('user')
    if (stored) setUser(JSON.parse(stored))
    setLoading(false)
  }, [])

  async function login(username, password) {
    const { data } = await api.post('/auth/login', { username, password })
    persist(data)
  }

  async function register(username, email, password) {
    const { data } = await api.post('/auth/register', { username, email, password })
    persist(data)
  }

  function persist(data) {
    localStorage.setItem('token', data.token)
    const userObj = { id: data.userId, username: data.username }
    localStorage.setItem('user', JSON.stringify(userObj))
    setUser(userObj)
  }

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
