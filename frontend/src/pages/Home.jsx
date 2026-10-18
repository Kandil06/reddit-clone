import { useEffect, useState } from 'react'
import api from '../api/axios'
import PostCard from '../components/PostCard'

export default function Home() {
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(true)

  async function load() {
    setLoading(true)
    const { data } = await api.get('/posts', { params: { page: 0, size: 25 } })
    setPosts(data.content)
    setLoading(false)
  }

  useEffect(() => { load() }, [])

  return (
    <div className="container">
      <h2>Home feed</h2>
      {loading && <p className="muted">Loading...</p>}
      {!loading && posts.length === 0 && <p className="muted">No posts yet. Be the first to post!</p>}
      {posts.map((p) => <PostCard key={p.id} post={p} onChange={load} />)}
    </div>
  )
}
