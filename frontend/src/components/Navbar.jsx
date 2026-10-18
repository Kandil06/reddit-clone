import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const [q, setQ] = useState('')
  const navigate = useNavigate()

  function handleSearch(e) {
    e.preventDefault()
    if (q.trim()) navigate(`/search?q=${encodeURIComponent(q.trim())}`)
  }

  return (
    <div className="navbar">
      <a className="brand" href="/">reddit-clone</a>
      <form onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Search posts, communities, users..."
          value={q}
          onChange={(e) => setQ(e.target.value)}
        />
      </form>
      <div className="nav-links">
        <a href="/communities/new">Create community</a>
        <a href="/submit">Post</a>
        {user ? (
          <>
            <a href={`/u/${user.username}`}>{user.username}</a>
            <button className="btn secondary" onClick={logout}>Log out</button>
          </>
        ) : (
          <a href="/login"><button className="btn">Log in</button></a>
        )}
      </div>
    </div>
  )
}
